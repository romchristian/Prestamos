/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.servicio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.persistencia.CobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamoHistorico;
import py.gestionpymes.prestamos.contabilidad.persistencia.Efectivo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuotaFactura;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
import py.gestionpymes.prestamos.prestamos.dao.DetCuentaClienteDAO;
import py.gestionpymes.prestamos.prestamos.dao.NumeroInvalidoException;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.PrestamoHistorico;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Secuencia;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccionCaja;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TransaccionCobraCuota;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TransaccionDesembolso;

/**
 *
 * @author christian
 */
@Stateless

public class CobranzaDAO {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private DetCuentaClienteDAO detCuentaClienteDAO;
    @EJB
    private PagoDAO pagoDAO;
    @EJB
    private TransaccionDAO transaccionDAO;

    public void creaFactura(FacturaVenta f) {
        em.persist(f);
    }

    public FacturaVenta create(FacturaVenta f, SesionTPV s) throws PagoExcedidoException, NumeroInvalidoException {
        // Creo el medio de pago Efectivo por defecto
//        Efectivo efe = new Efectivo();
//        efe.setFecha(new Date());
//        efe.setMoneda(f.getMoneda());
//        efe.setMonto(f.getTotal());
//        efe.setFacturaVenta(f);
//        f.setPagos(new ArrayList<Pago>());
//        f.getPagos().add(efe);

        em.persist(f);

        Long numeroFactura = null;

        try {
            numeroFactura = Long.parseLong(f.getNumero());
        } catch (Exception e) {
            throw new NumeroInvalidoException("El numero de la factura es invalido");
        }
        
        if (numeroFactura != null) {
            Secuencia secuencia = s.getPuntoVenta().getSecuencia();
            secuencia.setUltimoNumero(numeroFactura);
            System.out.println("Ultimo Nro en Cobranza: " + secuencia.getUltimoNumero());
            em.merge(secuencia);
        }

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", f.getCliente()).getSingleResult();

        BigDecimal saldoMoraAux = new BigDecimal(BigInteger.ZERO);

        Prestamo prestamo = null;

        for (FacturaVentaDetalle df : f.getDetalles()) {
            saldoMoraAux = df.getDetPrestamo().devuelveMontoMora();
            prestamo = df.getDetPrestamo().getPrestamo();

            break;
        }

        if (prestamo != null) {
            Transaccion tr = new TransaccionCobraCuota(f, prestamo, s,
                    "Cobro de cuota del prestamo #" + prestamo.getId()+" - "+prestamo.getCliente().devuelveNombreCompleto(), f.getTotal(),
                    f.getMoneda());

            TipoTransaccionCaja ttc = null;
            try {
                ttc = (TipoTransaccionCaja) em.createQuery("select t from TipoTransaccionCaja t where t.descripcion= ?1")
                        .setParameter(1, "COBRO DE CUOTA").getSingleResult();
            } catch (Exception e) {
            }

            if (ttc != null) {
                tr.setTipoTransaccionCaja(ttc);
            }

            transaccionDAO.create(tr);
            //em.persist(tr);

        }

        for (FacturaVentaDetalle df : f.getDetalles()) {
            System.out.println("Ahora voy a comparar...");

            BigDecimal monto = (df.getGravada10() == null ? new BigDecimal(BigInteger.ZERO) : df.getGravada10()).add(df.getGravada05() == null ? new BigDecimal(BigInteger.ZERO) : df.getGravada05())
                    .add(df.getExenta() == null ? new BigDecimal(BigInteger.ZERO) : df.getExenta());

            System.out.println("MONTO: " + monto);
            System.out.println("DESCUENTO: " + df.getDetPrestamo().getDescuento());
            System.out.println("DEVUELVE SALDO: " + saldoMoraAux);
            System.out.println("TIENE DESCUENTO: " + df.getDetPrestamo().isTieneDescuento());
            System.out.println("REF MONTO: " + df.getRefMonto());

            if ((df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0
                    || df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0)
                    && df.getDetPrestamo().isTieneDescuento() && df.getDetPrestamo().getDescuento().compareTo(saldoMoraAux) == 0) {
                System.out.println("Entre al primer IF, no debo hacer nada!");
            } else if ((df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0
                    || df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0)
                    && df.getDetPrestamo().isTieneDescuento() && df.getDetPrestamo().getDescuento().compareTo(saldoMoraAux) < 0) {
                System.out.println("Estoy en el segundo IF debo ver si es Moratorio o punitorio refMonto: " + df.getRefMonto());

                BigDecimal diffDescuento = saldoMoraAux.subtract(df.getDetPrestamo().getDescuento()).setScale(0, RoundingMode.HALF_EVEN);

                if ((df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0)) {
                    OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df, new BigDecimal(BigInteger.ZERO), diffDescuento.multiply(new BigDecimal(0.8)));
                    occ.setCuentaCliente(cc);
                    occ.setFecha(df.getFacturaVenta().getFechaEmision());
                    detCuentaClienteDAO.create(occ);
                    System.out.println("estoy en IF moratorio: " + df.getRefMonto());
                } else {
                    OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df, new BigDecimal(BigInteger.ZERO), diffDescuento.multiply(new BigDecimal(0.2)));
                    occ.setCuentaCliente(cc);
                    occ.setFecha(df.getFacturaVenta().getFechaEmision());
                    detCuentaClienteDAO.create(occ);
                    System.out.println("deberia ser el ELSE de punitorio: " + df.getRefMonto());
                }

            } else if ((df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_DESCUENTO) == 0)
                    && df.getDetPrestamo().isTieneDescuento() && df.getDetPrestamo().getDescuento().compareTo(saldoMoraAux) <= 0) {

                OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df, new BigDecimal(BigInteger.ZERO), monto.multiply(new BigDecimal(-1)));
                occ.setCuentaCliente(cc);
                occ.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ);
                System.out.println("Hay descuento el monto es: " + df.getRefMonto());
            } else {
                OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df);
                occ.setCuentaCliente(cc);
                occ.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ);
                System.out.println("Caso por default: " + df.getRefMonto());
            }

            if (df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0
                    || df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0) {

                OperacionCobroCuotaFactura occ2 = new OperacionCobroCuotaFactura(df, true);
                occ2.setCuentaCliente(cc);
                occ2.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ2);
            }

            DetPrestamo dp = df.getDetPrestamo();

            if (!dp.afectaSaldoCuota(monto, df.getRefMonto())) {
                throw new PagoExcedidoException("El monto no puede ser mayor al saldo de la cuota");
            } else {
                Prestamo p = dp.getPrestamo();
                dp.setUltimoPago(f.getFechaEmision());
                p.setUltimoPago(f.getFechaEmision());
                em.merge(dp);
                em.merge(p);
            }
        }

        return f;
    }

    public CobroCuota create(TreeCuota t) throws PagoExcedidoException {

        CobroCuota cobro = new CobroCuota(t.getPrestamo());

        if (t.getMontoPago().compareTo(t.getMontoMora().add(t.getSaldoCuota())) > 0) {
            throw new PagoExcedidoException("El monto de la cuota #" + t.getNroCuota() + " esta excedido");
        }

        Efectivo efe = new Efectivo();
        efe.setFecha(new Date());
        efe.setMoneda(t.getMoneda());
        efe.setMonto(t.getMontoPago());
        pagoDAO.create(efe);

        System.out.println("Paga 2");
        DetCobroCuota dcc = new DetCobroCuota();
        dcc.setCobroCuota(cobro);
        dcc.setPago(efe);
        dcc.setMoneda(t.getMoneda());
        dcc.setMonto(efe.getMonto());
        System.out.println("Paga 3");

        if (!t.isEsPrestamo()) {
            dcc.setDetPrestamo(t.getDetPrestamo());
        }

        dcc.setFecha(new Date());
        cobro.getDetalles().add(dcc);

        DetPrestamo dp = t.getDetPrestamo();
        cobro.setConcepto("Pago cuota " + dp.getNroCuota() + " Prestamo nro " + cobro.getPrestamo().getId());

        Integer ultmoid = 0;
        try {
            ultmoid = (Integer) em.createQuery("SELECT MAX(r.id) FROM Recibo r").getSingleResult();
        } catch (Exception e) {
        }

        if (ultmoid == null) {
            ultmoid = 0;
        }
        cobro.setNro((ultmoid + 1) + "");

        em.persist(cobro);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", cobro.getCliente()).getSingleResult();

        for (DetCobroCuota d : cobro.getDetalles()) {
            OperacionCobroCuota occ = new OperacionCobroCuota(d);
            occ.setCuentaCliente(cc);
            occ.setFecha(new Date());
            detCuentaClienteDAO.create(occ);

//            if (!dp.afectaSaldoCuota(d.getMonto())) {
//                throw new PagoExcedidoException("El monto no puede ser mayor al saldo de la cuota");
//            } else {
//                Prestamo p = dp.getPrestamo();
//                p.setUltimoPago(dp.getUltimoPago());
//                em.merge(dp);
//                em.merge(p);
//            }
        }

        return cobro;
    }

    public CobroCuota create(DetPrestamoHistorico d) throws PagoExcedidoException {

        CobroCuota cobro = new CobroCuota(d.getPrestamo());

        Efectivo efe = new Efectivo();
        efe.setFecha(new Date());
        efe.setMoneda(d.getPrestamo().getMoneda());
        efe.setMonto(d.getMontoPago());

        pagoDAO.edit(efe);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", d.getPrestamo().getCliente()).getSingleResult();

        DetCobroCuota dcc = new DetCobroCuota();
        dcc.setCobroCuota(cobro);
        dcc.setPago(efe);
        dcc.setMoneda(efe.getMoneda());
        dcc.setMonto(efe.getMonto().subtract(d.getMontoMora() == null ? new BigDecimal(BigInteger.ZERO) : d.getMontoMora()));
        dcc.setDetPrestamoHistorico(d);
        dcc.setFecha(d.getUltimoPago());
        dcc.setRefMonto(FacturaVentaDetalle.MONTO_CUOTA);
        cobro.getDetalles().add(dcc);

        if (d.getMontoMora() != null && d.getMontoMora().compareTo(BigDecimal.ZERO) > 0) {
            DetCobroCuota dcc2 = new DetCobroCuota();
            dcc2.setCobroCuota(cobro);
            dcc2.setPago(efe);
            dcc2.setMoneda(efe.getMoneda());
            dcc2.setMonto(d.getMontoMora());
            dcc2.setDetPrestamoHistorico(d);
            dcc2.setFecha(d.getUltimoPago());
            dcc2.setRefMonto(FacturaVentaDetalle.MONTO_MORATORIO);
            cobro.getDetalles().add(dcc2);
        }

        cobro.setConcepto("Pago cuota " + d.getNroCuota() + " Prestamo Historico nro " + d.getPrestamo().getId());

        Integer ultmoid = 0;

        try {
            ultmoid = (Integer) em.createQuery("SELECT MAX(r.id) FROM Recibo r").getSingleResult();
        } catch (Exception e) {
        }

        if (ultmoid == null) {
            ultmoid = 0;
        }
        cobro.setNro((ultmoid + 1) + "");

        em.merge(cobro);

        boolean hayMora = false;
        DetCobroCuota dtMora;
        for (DetCobroCuota dt : cobro.getDetalles()) {
            OperacionCobroCuota occ = new OperacionCobroCuota(dt, false);
            occ.setCuentaCliente(cc);
            occ.setFecha(new Date());
            em.merge(occ);

            if (dt.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0) {

                System.out.println("Entre en Moratorio 1");
                OperacionCobroCuota occ2 = new OperacionCobroCuota(dt, true);
                occ2.setCuentaCliente(cc);
                occ2.setFecha(new Date());
                em.merge(occ2);
            }

        }

        return cobro;
    }

    public List<DetPrestamo> findVencientos(Date start, Date end) {
        return em.createQuery("SELECT d FROM DetPrestamo d where d.saldoCuota > 0  and d.fechaVencimiento BETWEEN :start and :end "
                + "ORDER BY d.fechaVencimiento")
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}

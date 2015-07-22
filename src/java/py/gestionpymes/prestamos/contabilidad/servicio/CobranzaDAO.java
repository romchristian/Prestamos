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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.modelo.CobroCuota;
import py.gestionpymes.prestamos.prestamos.modelo.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamoHistorico;
import py.gestionpymes.prestamos.contabilidad.modelo.Efectivo;
import py.gestionpymes.prestamos.prestamos.modelo.OperacionCobroCuota;
import py.gestionpymes.prestamos.prestamos.modelo.OperacionCobroCuotaFactura;
import py.gestionpymes.prestamos.contabilidad.modelo.Pago;
import py.gestionpymes.prestamos.prestamos.dao.DetCuentaClienteDAO;
import py.gestionpymes.prestamos.prestamos.dao.MontoCancelacionIncorrectoException;
import py.gestionpymes.prestamos.prestamos.dao.NumeroInvalidoException;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.modelo.AplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.DescuentoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;
import py.gestionpymes.prestamos.prestamos.modelo.PrestamoHistorico;
import py.gestionpymes.prestamos.prestamos.modelo.TipoDescuento;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoAplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;
import py.gestionpymes.prestamos.tesoreria.modelo.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccionCaja;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;
import py.gestionpymes.prestamos.tesoreria.modelo.TransaccionCobraCuota;
import py.gestionpymes.prestamos.tesoreria.modelo.TransaccionDesembolso;

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

    public void aplicaDescuento(DescuentoCuota dc, TipoDescuento tipo, AplicacionPagoCuota a) {
        if (dc.getTipo() == tipo && !dc.isAplicado()) {
            dc.setAplicado(true);
            em.merge(dc);
        }
    }

    public void creaAplicacionesPago(List<AplicacionPagoCuota> aplicacionPagoCuotas) {
        for (AplicacionPagoCuota a : aplicacionPagoCuotas) {
            em.merge(a);
            DetPrestamo dt = a.getDetPrestamo();
            
            
            for (DescuentoCuota dc : dt.getDescuentoCuotas()) {
                switch (a.getTipo()) {
                    case DESCUENTO_MORA:
                        aplicaDescuento(dc, TipoDescuento.MORA, a);
                        break;
                    case DESCUENTO_INTERES:
                        aplicaDescuento(dc, TipoDescuento.INTERES, a);
                        break;
                    case DESCUENTO_CARGO:
                        aplicaDescuento(dc, TipoDescuento.CARGOS, a);
                        break;
                }
            }
        }
    }

    public FacturaVenta paga(FacturaVenta f, SesionTPV s) throws PagoExcedidoException, NumeroInvalidoException, MontoCancelacionIncorrectoException {
        BigDecimal totalPagado = new BigDecimal(BigInteger.ZERO);
        for (Pago p : f.getPagos()) {
            totalPagado = totalPagado.add(p.getMonto());
        }

        if (f.getTotal().compareTo(totalPagado) == 0) {
            f.setTotalPagado(totalPagado);
        } else {
            throw new MontoCancelacionIncorrectoException("El monto de cancelación no cubre la factura");
        }

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
                    "Cobro de cuota del prestamo #" + prestamo.getId() + " - " + prestamo.getCliente().devuelveNombreCompleto(), f.getTotal(),
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

            Transaccion ta = transaccionDAO.create(tr);

            for (Pago p : f.getPagos()) {
                p.setTransaccion(ta);
                em.merge(p);
            }

        }

        Map<DetPrestamo, List<BigDecimal>> totalesPorCuota = new HashMap<>();

        for (FacturaVentaDetalle df : f.getDetalles()) {
            System.out.println("Ahora voy a comparar...");

            BigDecimal monto = (df.getGravada10() == null ? new BigDecimal(BigInteger.ZERO) : df.getGravada10()).add(df.getGravada05() == null ? new BigDecimal(BigInteger.ZERO) : df.getGravada05())
                    .add(df.getExenta() == null ? new BigDecimal(BigInteger.ZERO) : df.getExenta());

//            System.out.println("MONTO: " + monto);
//            System.out.println("DESCUENTO: " + df.getDetPrestamo().getDescuento());
//            System.out.println("DEVUELVE SALDO: " + saldoMoraAux);
//            System.out.println("TIENE DESCUENTO: " + df.getDetPrestamo().isTieneDescuento());
//            System.out.println("REF MONTO: " + df.getRefMonto());
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

                }

            } else if ((df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_DESCUENTO) == 0)
                    && df.getDetPrestamo().isTieneDescuento() && df.getDetPrestamo().getDescuento().compareTo(saldoMoraAux) <= 0) {

                OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df, new BigDecimal(BigInteger.ZERO), monto.multiply(new BigDecimal(-1)));
                occ.setCuentaCliente(cc);
                occ.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ);

            } else {
                OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df);
                occ.setCuentaCliente(cc);
                occ.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ);

            }

            if (df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0
                    || df.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0) {

                OperacionCobroCuotaFactura occ2 = new OperacionCobroCuotaFactura(df, true);
                occ2.setCuentaCliente(cc);
                occ2.setFecha(df.getFacturaVenta().getFechaEmision());
                detCuentaClienteDAO.create(occ2);
            }

            DetPrestamo dp = df.getDetPrestamo();

            if (!dp.afectaMora(monto, df.getRefMonto())) {
                System.out.println("No era un detalle de mora");
            }

            if (totalesPorCuota.get(dp) == null) {
                totalesPorCuota.put(dp, new ArrayList<BigDecimal>());
                totalesPorCuota.get(dp).add(monto);

            } else {
                totalesPorCuota.get(dp).add(monto);
            }

        }

        for (Entry<DetPrestamo, List<BigDecimal>> entry : totalesPorCuota.entrySet()) {
            DetPrestamo dp = entry.getKey();
            BigDecimal totalMonto = BigDecimal.ZERO;
            for (BigDecimal d : entry.getValue()) {
                totalMonto = totalMonto.add(d);
            }

            if (!dp.afectaSaldoCuota(totalMonto)) {
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

    public FacturaVenta create(FacturaVenta f, SesionTPV s, List<AplicacionPagoCuota> lista) throws PagoExcedidoException, NumeroInvalidoException, MontoCancelacionIncorrectoException {
        FacturaVenta R = paga(f, s);
        creaAplicacionesPago(lista);
        return R;
    }

    public FacturaVenta create(FacturaVenta f, SesionTPV s) throws PagoExcedidoException, NumeroInvalidoException, MontoCancelacionIncorrectoException {
        return paga(f, s);
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

    public BigDecimal getAplicacionesMontoAcumulado(DetPrestamo dt, TipoAplicacionPagoCuota tipo) {
        BigDecimal R = BigDecimal.ZERO;
        try {
            R = (BigDecimal) em.createQuery("SELECT SUM(a.monto) FROM AplicacionPagoCuota a where a.detPrestamo = :detprestamo and a.tipo = :tipo ")
                    .setParameter("detprestamo", dt)
                    .setParameter("tipo", tipo)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (R == null) {
            R = BigDecimal.ZERO;
        }
        return R;
    }

    public BigDecimal getDescuentoAcumulado(DetPrestamo dt, TipoDescuento tipo) {
        BigDecimal R = BigDecimal.ZERO;
        try {
            R = (BigDecimal) em.createQuery("SELECT SUM(d.monto) FROM DescuentoCuota d where d.detPrestamo = :detprestamo and d.tipo = :tipo and d.aplicado = :aplicado")
                    .setParameter("detprestamo", dt)
                    .setParameter("tipo", tipo)
                    .setParameter("aplicado", false)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (R == null) {
            R = BigDecimal.ZERO;
        }
        return R;
    }

    public BigDecimal getDescuentoAcumuladoTotal(DetPrestamo dt) {
        BigDecimal R = BigDecimal.ZERO;
        try {
            R = (BigDecimal) em.createQuery("SELECT SUM(d.monto) FROM DescuentoCuota d where d.detPrestamo = :detprestamo and d.aplicado = :aplicado")
                    .setParameter("detprestamo", dt)
                    .setParameter("aplicado", false)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (R == null) {
            R = BigDecimal.ZERO;
        }
        return R;
    }

    public List<DetPrestamo> findVencientos(Date start, Date end) {
        return em.createQuery("SELECT d FROM DetPrestamo d where d.saldoCuota > 0  and d.fechaVencimiento BETWEEN :start and :end "
                + "ORDER BY d.fechaVencimiento")
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}

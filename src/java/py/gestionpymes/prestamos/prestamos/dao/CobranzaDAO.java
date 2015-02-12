/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.contabilidad.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.persistencia.CobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Efectivo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuotaFactura;
import py.gestionpymes.prestamos.prestamos.persistencia.Pago;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;

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

    public FacturaVenta create(FacturaVenta f) throws PagoExcedidoException {
        // Creo el medio de pago Efectivo por defecto
        Efectivo efe = new Efectivo();
        efe.setFecha(new Date());
        efe.setMoneda(f.getMoneda());
        efe.setMonto(f.getTotal());
        efe.setFacturaVenta(f);
        f.setPagos(new ArrayList<Pago>());
        f.getPagos().add(efe);

        em.persist(f);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", f.getCliente()).getSingleResult();

        for (FacturaVentaDetalle df : f.getDetalles()) {
            OperacionCobroCuotaFactura occ = new OperacionCobroCuotaFactura(df);
            occ.setCuentaCliente(cc);
            occ.setFecha(new Date());
            detCuentaClienteDAO.create(occ);

            DetPrestamo dp = df.getDetPrestamo();

            BigDecimal monto = (df.getGravada10() == null ? new BigDecimal(BigInteger.ZERO):df.getGravada10()).add(df.getGravada05() == null ? new BigDecimal(BigInteger.ZERO) : df.getGravada05())
                    .add(df.getExenta() == null ? new BigDecimal(BigInteger.ZERO) : df.getExenta());

            if (!dp.afectaSaldoCuota(monto, df.getRefMonto())) {
                throw new PagoExcedidoException("El monto no puede ser mayor al saldo de la cuota");
            } else {
                Prestamo p = dp.getPrestamo();
                p.setUltimoPago(dp.getUltimoPago());
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

    public List<DetPrestamo> findVencientos(Date start, Date end) {
        return em.createQuery("SELECT d FROM DetPrestamo d where d.saldoCuota > 0  and d.fechaVencimiento BETWEEN :start and :end "
                + "ORDER BY d.fechaVencimiento")
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}

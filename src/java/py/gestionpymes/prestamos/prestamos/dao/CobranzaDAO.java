/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.prestamos.persistencia.CobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Efectivo;
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.Pago;
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

    public CobroCuota create(TreeCuota t) throws PagoExcedidoException {

        CobroCuota cobro = new CobroCuota(t.getPrestamo());

        
            if (t.getMontoPago() > (t.getMontoMora() + t.getSaldoCuota())) {
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

                if (!dp.afectaSaldoCuota(d.getMonto())) {
                    throw new PagoExcedidoException("El monto no puede ser mayor al saldo de la cuota");
                } else {
                    em.merge(dp);
                }

            }
        

        return cobro;
    }
}

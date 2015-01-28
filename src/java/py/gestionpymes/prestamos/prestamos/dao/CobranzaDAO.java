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
import py.gestionpymes.prestamos.prestamos.persistencia.OperacionCobroCuota;

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

    public CobroCuota create(CobroCuota cobro) {

        DetPrestamo dp = cobro.getDetalles().get(0).getDetPrestamo();
        cobro.setConcepto("Pago cuota " + dp.getNroCuota() + " Prestamo nro " + cobro.getPrestamo().getId());
        
        Integer ultmoid = 0;
        try {
            ultmoid = (Integer) em.createQuery("SELECT MAX(r.id) FROM Recibo r").getSingleResult();
        } catch (Exception e) {
        }
       
       if(ultmoid == null){
           ultmoid = 0;
       }
        cobro.setNro((ultmoid+1)+"");

        em.persist(cobro);

        CuentaCliente cc = (CuentaCliente) em.createQuery("select c from CuentaCliente c where c.cliente = :cliente")
                .setParameter("cliente", cobro.getCliente()).getSingleResult();
        for (DetCobroCuota d : cobro.getDetalles()) {
            OperacionCobroCuota occ = new OperacionCobroCuota(d);
            occ.setCuentaCliente(cc);
            occ.setFecha(new Date());
            detCuentaClienteDAO.create(occ);
            if (!d.getDetPrestamo().afectaSaldoCuota(d.getMonto())) {
                throw new RuntimeException("El monto no puede ser mayor al saldo de la cuota");
            }

        }

        return cobro;
    }
}

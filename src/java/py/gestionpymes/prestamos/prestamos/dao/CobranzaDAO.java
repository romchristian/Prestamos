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
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
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
        CobroCuota R = em.merge(cobro);
        for (DetCobroCuota d : cobro.getDetalles()) {
            OperacionCobroCuota cc = new OperacionCobroCuota(d);
            cc.setFecha(new Date());
            detCuentaClienteDAO.create(cc);
            if (!d.getDetPrestamo().afectaSaldoCuota(d.getMonto())) {
                throw new RuntimeException("El monto no puede ser mayor al saldo de la cuota");
            }

        }

        return R;
    }
}

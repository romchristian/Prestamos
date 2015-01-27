/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.prestamos.persistencia.Pago;

/**
 *
 * @author christian
 */
@Stateless
public class PagoDAO extends AbstractFacade<Pago> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagoDAO() {
        super(Pago.class);
    }
}

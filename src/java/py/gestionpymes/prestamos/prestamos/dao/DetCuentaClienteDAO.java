/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.AbstractFacade;
import py.gestionpymes.prestamos.prestamos.modelo.DetCuentaCliente;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DetCuentaClienteDAO extends AbstractFacade<DetCuentaCliente> {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
    @EJB
    private DetCuentaClienteDAO detCuentaClienteDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetCuentaClienteDAO() {
        super(DetCuentaCliente.class);
    }

}

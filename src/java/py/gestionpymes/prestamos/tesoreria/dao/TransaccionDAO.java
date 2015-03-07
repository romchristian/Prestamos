/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;


/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransaccionDAO extends AbstractDAO<Transaccion> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Transaccion create(Transaccion entity) {
        return abmService.create(entity);
    }

    @Override
    public Transaccion edit(Transaccion entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Transaccion entity) {
        abmService.delete(entity);
    }

    @Override
    public Transaccion find(Object id) {
        return abmService.find(id, Transaccion.class);
    }

    @Override
    public List<Transaccion> findAll() {
        return abmService.getEM().createQuery("select obj from Transaccion obj").getResultList();
    }

    @Override
    public List<Transaccion> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

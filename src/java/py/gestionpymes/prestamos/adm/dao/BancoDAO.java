/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.modelo.Banco;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BancoDAO extends AbstractDAO<Banco> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Banco create(Banco entity) {
        return abmService.create(entity);
    }

    @Override
    public Banco edit(Banco entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Banco entity) {
         abmService.delete(entity);
    }

    @Override
    public Banco find(Object id) {
        return abmService.find(id, Banco.class);
    }

    @Override
    public List<Banco> findAll() {
        return abmService.getEM().createQuery("select obj from Banco obj").getResultList();
    }

    @Override
    public List<Banco> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

   
}

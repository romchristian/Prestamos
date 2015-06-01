/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.dao;

import py.gestionpymes.prestamos.tesoreria.dao.*;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.finanza.persistencia.TransaccionBancaria;



/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransaccionBancariaDAO extends AbstractDAO<TransaccionBancaria> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public TransaccionBancaria create(TransaccionBancaria entity) {
        return abmService.create(entity);
    }

    @Override
    public TransaccionBancaria edit(TransaccionBancaria entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(TransaccionBancaria entity) {
        abmService.delete(entity);
    }

    @Override
    public TransaccionBancaria find(Object id) {
        return abmService.find(id, TransaccionBancaria.class);
    }

    @Override
    public List<TransaccionBancaria> findAll() {
        return abmService.getEM().createQuery("select obj from TransaccionBancaria obj").getResultList();
    }

    @Override
    public List<TransaccionBancaria> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
    
    
    
}

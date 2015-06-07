/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.finanza.modelo.ChequeEmitido;



/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ChequeEmitidoDAO extends AbstractDAO<ChequeEmitido> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public ChequeEmitido create(ChequeEmitido entity) {
        return abmService.create(entity);
    }

    @Override
    public ChequeEmitido edit(ChequeEmitido entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(ChequeEmitido entity) {
        abmService.delete(entity);
    }

    @Override
    public ChequeEmitido find(Object id) {
        return abmService.find(id, ChequeEmitido.class);
    }

    @Override
    public List<ChequeEmitido> findAll() {
        return abmService.getEM().createQuery("select obj from ChequeEmitido obj").getResultList();
    }

    @Override
    public List<ChequeEmitido> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.prestamos.modelo.DescuentoCuota;



/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DescuentoCuotaDAO extends AbstractDAO<DescuentoCuota> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public DescuentoCuota create(DescuentoCuota entity) {
        return abmService.create(entity);
    }

    @Override
    public DescuentoCuota edit(DescuentoCuota entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(DescuentoCuota entity) {
        abmService.delete(entity);
    }

    @Override
    public DescuentoCuota find(Object id) {
        return abmService.find(id, DescuentoCuota.class);
    }

    @Override
    public List<DescuentoCuota> findAll() {
        return abmService.getEM().createQuery("select obj from DescuentoCuota obj")
               .getResultList();
    }

    @Override
    public List<DescuentoCuota> findAll(String query, QueryParameter params) {
       return null;
    }
    
}

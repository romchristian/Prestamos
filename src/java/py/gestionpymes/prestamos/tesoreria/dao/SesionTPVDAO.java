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
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;


/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class SesionTPVDAO extends AbstractDAO<SesionTPV> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    

    @Override
    public SesionTPV create(SesionTPV entity) {
        return abmService.create(entity);
    }

    @Override
    public SesionTPV edit(SesionTPV entity) {
        abmService.getEM().merge(entity.getPuntoVenta());
        return abmService.update(entity);
    }

    @Override
    public void remove(SesionTPV entity) {
        abmService.delete(entity);
    }

    @Override
    public SesionTPV find(Object id) {
        return abmService.find(id, SesionTPV.class);
    }

    @Override
    public List<SesionTPV> findAll() {
        return abmService.getEM().createQuery("select obj from SesionTPV obj").getResultList();
    }

    @Override
    public List<SesionTPV> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

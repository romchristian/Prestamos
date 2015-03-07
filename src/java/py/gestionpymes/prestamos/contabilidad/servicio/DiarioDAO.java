/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestion.contabilidad.servicio;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.contabilidad.persistencia.Diario;


/**
 *
 * @author christian
*/
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DiarioDAO extends AbstractDAO<Diario> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Diario create(Diario entity) {
        return abmService.create(entity);
    }

    @Override
    public Diario edit(Diario entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Diario entity) {
        abmService.delete(entity);
    }

    @Override
    public Diario find(Object id) {
        return abmService.find(id, Diario.class);
    }

    @Override
    public List<Diario> findAll() {
        return abmService.getEM().createQuery("select obj from Diario obj").getResultList();
    }

    @Override
    public List<Diario> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
    
    public List<Diario> findAllRoots() {
        return abmService.getEM().createQuery("SELECT d from Diario d where d.padre IS NULL").getResultList();
    }
    
    public List<Diario> findAllChildren(Diario d) {
        return abmService.getEM().createQuery("SELECT d from Diario d where d.padre = :padre").setParameter("padre", d).getResultList();
    }
    
    
}

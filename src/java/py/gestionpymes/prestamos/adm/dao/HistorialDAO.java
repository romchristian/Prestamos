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
import py.gestionpymes.prestamos.adm.modelo.Historial;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HistorialDAO extends AbstractDAO<Historial> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Historial create(Historial entity) {
        return abmService.create(entity);
    }

    @Override
    public Historial edit(Historial entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Historial entity) {
         abmService.delete(entity);
    }

    @Override
    public Historial find(Object id) {
        return abmService.find(id, Historial.class);
    }

    @Override
    public List<Historial> findAll() {
        return abmService.getEM().createQuery("select obj from Historial obj").getResultList();
    }

    @Override
    public List<Historial> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

   
}

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
import py.gestionpymes.prestamos.adm.persistencia.Usuario;
import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PuntoVentaDAO extends AbstractDAO<PuntoVenta> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public PuntoVenta create(PuntoVenta entity) {
        return abmService.create(entity);
    }

    @Override
    public PuntoVenta edit(PuntoVenta entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(PuntoVenta entity) {
        abmService.delete(entity);
    }

    @Override
    public PuntoVenta find(Object id) {
        return abmService.find(id, PuntoVenta.class);
    }

    @Override
    public List<PuntoVenta> findAll() {
        return abmService.getEM().createQuery("SELECT p from PuntoVenta p").getResultList();
    }

    @Override
    public List<PuntoVenta> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
    
    public PuntoVenta find(Usuario u) {
        PuntoVenta R = null;
        try {
            R = (PuntoVenta) abmService.getEM().createQuery("SELECT p FROM PuntoVenta p WHERE p.usuario = :u")
                    .setParameter("u", u)
                    .getSingleResult();
        } catch (Exception e) {
        }
        return R;
    }
}

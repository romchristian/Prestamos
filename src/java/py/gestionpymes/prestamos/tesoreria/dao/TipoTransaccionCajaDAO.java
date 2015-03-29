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
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccionCaja;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TipoTransaccionCajaDAO extends AbstractDAO<TipoTransaccionCaja> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public TipoTransaccionCaja create(TipoTransaccionCaja entity) {
        return abmService.create(entity);
    }

    @Override
    public TipoTransaccionCaja edit(TipoTransaccionCaja entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(TipoTransaccionCaja entity) {
        abmService.delete(entity);
    }

    @Override
    public TipoTransaccionCaja find(Object id) {
        return abmService.find(id, TipoTransaccionCaja.class);
    }

    @Override
    public List<TipoTransaccionCaja> findAll() {
        return abmService.getEM().createQuery("select obj from TipoTransaccionCaja obj").getResultList();
    }

   
    @Override
    public List<TipoTransaccionCaja> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

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
import py.gestionpymes.prestamos.tesoreria.modelo.ValorMoneda;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ValorMonedaDAO extends AbstractDAO<ValorMoneda> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public ValorMoneda create(ValorMoneda entity) {
        return abmService.create(entity);
    }

    @Override
    public ValorMoneda edit(ValorMoneda entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(ValorMoneda entity) {
        abmService.delete(entity);
    }

    @Override
    public ValorMoneda find(Object id) {
        return abmService.find(id, ValorMoneda.class);
    }

    @Override
    public List<ValorMoneda> findAll() {
        return abmService.getEM().createQuery("select obj from ValorMoneda obj").getResultList();
    }

    @Override
    public List<ValorMoneda> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

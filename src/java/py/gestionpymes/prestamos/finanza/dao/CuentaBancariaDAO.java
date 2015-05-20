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
import py.gestionpymes.prestamos.finanza.persistencia.CuentaBancaria;



/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CuentaBancariaDAO extends AbstractDAO<CuentaBancaria> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public CuentaBancaria create(CuentaBancaria entity) {
        return abmService.create(entity);
    }

    @Override
    public CuentaBancaria edit(CuentaBancaria entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(CuentaBancaria entity) {
        abmService.delete(entity);
    }

    @Override
    public CuentaBancaria find(Object id) {
        return abmService.find(id, CuentaBancaria.class);
    }

    @Override
    public List<CuentaBancaria> findAll() {
        return abmService.getEM().createQuery("select obj from CuentaBancaria obj").getResultList();
    }

    @Override
    public List<CuentaBancaria> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

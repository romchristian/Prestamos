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
import javax.inject.Inject;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.finanza.persistencia.BoletaDeposito;
import py.gestionpymes.prestamos.finanza.persistencia.TransaccionDepositoEfectivo;
import py.gestionpymes.prestamos.finanza.persistencia.BoletaDeposito;

/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BoletaDepositoDAO extends AbstractDAO<BoletaDeposito> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;
    @EJB
    private TransaccionBancariaDAO transaccionBancariaDAO;
    @Inject
    private Credencial credencial;

    @Override
    public BoletaDeposito create(BoletaDeposito entity) {
        return abmService.create(entity);
    }

    @Override
    public BoletaDeposito edit(BoletaDeposito entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(BoletaDeposito entity) {
        abmService.delete(entity);
    }

    @Override
    public BoletaDeposito find(Object id) {
        return abmService.find(id, BoletaDeposito.class);
    }

    @Override
    public List<BoletaDeposito> findAll() {
        return abmService.getEM().createQuery("select obj from BoletaDeposito obj").getResultList();
    }

    @Override
    public List<BoletaDeposito> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

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
import py.gestionpymes.prestamos.finanza.modelo.CuentaBancaria;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoSecuencia;


/**
 *
 * @author christian
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class SecuenciaDAO extends AbstractDAO<Secuencia> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Secuencia create(Secuencia entity) {
        return abmService.create(entity);
    }

    @Override
    public Secuencia edit(Secuencia entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Secuencia entity) {
        abmService.delete(entity);
    }

    @Override
    public Secuencia find(Object id) {
        return abmService.find(id, Secuencia.class);
    }

    @Override
    public List<Secuencia> findAll() {
        return abmService.getEM().createQuery("select obj from Secuencia obj WHERE OBJ.tipoSecuencia <> :tipo")
                .setParameter("tipo", TipoSecuencia.CHEQUE)
                .getResultList();
    }
    
    public List<Secuencia> findAllChequeras() {
        return abmService.getEM().createQuery("select obj from Secuencia obj WHERE OBJ.tipoSecuencia = :tipo")
                .setParameter("tipo", TipoSecuencia.CHEQUE)
                .getResultList();
    }
    
    public List<Secuencia> findAllChequeras(CuentaBancaria cuentaBancaria) {
        return abmService.getEM().createQuery("select obj from Secuencia obj WHERE OBJ.tipoSecuencia = :tipo and OBJ.cuentaBancaria = :cuenta")
                .setParameter("tipo", TipoSecuencia.CHEQUE)
                .setParameter("cuenta", cuentaBancaria)
                .getResultList();
    }

    @Override
    public List<Secuencia> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }
}

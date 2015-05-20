/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.seguridad.dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.dao.ABMService;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditoria;

/**
 *
 * @author Acer
 */
@Stateless
public class AuditoriaDAO extends AbstractDAO<Auditoria> {

    @EJB(beanName = "ABMServiceBean")
    private ABMService abmService;

    @Override
    public Auditoria create(Auditoria entity) {
        return abmService.create(entity);
    }

    public void crear(Auditoria aud) {
        System.out.println("crear : ");
        EntityManager em = abmService.getEM();
        System.out.println("EM : " + em);
        System.out.println("Mi objeto : " + aud);
        System.out.println("desc : " + aud.getTablaAfectada());
        em.persist(aud);
        System.out.println("Mi objeto despues de persist: " + aud);
    }

    @Override
    public Auditoria edit(Auditoria entity) {
        return abmService.update(entity);
    }

    @Override
    public void remove(Auditoria entity) {
        abmService.delete(entity);
    }

    @Override
    public Auditoria find(Object id) {
        return abmService.find(id, Auditoria.class);
    }

    @Override
    public List<Auditoria> findAll() {
        return abmService.getEM().createQuery("select obj from Auditoria obj").getResultList();
    }

    @Override
    public List<Auditoria> findAll(String query, QueryParameter params) {
        return abmService.findByQuery(query, params.parameters());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditoria;

/**
 *
 * @author Acer
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
    @Inject
    private Credencial credencial;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        getEntityManager().refresh(entity);
        Object id = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
        preparaRegistro(entity, "CREATE",id);
    }

    public void edit(T entity) {
       getEntityManager().merge(entity);
        //getEntityManager().flush();
       // getEntityManager().refresh(entity);
       
        Object id = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
        preparaRegistro(entity, "CREATE/UPDATE",id);
    }

    public void remove(T entity) {
        //preparaRegistro(entity, "REMOVE");
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public void preparaRegistro(Object obj, String tipoOperacion,Object id) {
        if (obj instanceof Auditable) {
            Auditable a = (Auditable) obj;
            registrar(a, tipoOperacion,id);
        }
    }

    public void registrar(Auditable a, String tipoOperacion, Object id) {
        Auditoria aud = new Auditoria();
        
     
        
        aud.setFecha(new Date());
        
        aud.setTablaAfectada(a.getClass().getSimpleName() + ": id = " + id);
        aud.setTipoOperacion(tipoOperacion);
        aud.setUsuarioLogeado(credencial.getUsuario() == null ? "" : credencial.getUsuario().getUsuario());
        getEntityManager().persist(aud);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditoria;

/**
 *
 * @author cromero
 */
@Stateless
@Local(ABMService.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ABMServiceBean implements ABMService {

    @Inject
    private Credencial credencial;
    @PersistenceContext(unitName = "PrestamosPU")
    EntityManager em;

    @Override
    public EntityManager getEM() {
        return em;
    }

    @Override
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);

        preparaRegistro(t, "CREATE");

        return t;
    }

    @Override
    public void delete(Object t) {
        preparaRegistro(t, "REMOVE");
        this.em.merge(t);
        this.em.remove(t);
    }

    @Override
    public <T> T update(T t) {
        try {
            t = this.em.merge(t);
            this.em.flush();
            this.em.refresh(t);
            preparaRegistro(t, "CREATE/UPDATE");

        } catch (OptimisticLockException e) {
            return null;
        }
        return t;
    }

    @Override
    public <T> T find(Object id, Class<T> type) {
        return (T) this.em.find(type, id);
    }

    @Override
    public <T> T findByNamedQuerySingleResult(String queryName, Map<String, Object> params) {
        T R = null;
        try {
            Set<Entry<String, Object>> rawParameters = params.entrySet();
            Query query = this.em.createNamedQuery(queryName);
            for (Entry<String, Object> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            R = (T) query.getSingleResult();
        } catch (Exception e) {
        }
        return R;
    }

    @Override
    public List findByNamedQuery(String queryName) {
        return this.em.createNamedQuery(queryName).getResultList();
    }

    @Override
    public List findByNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    @Override
    public List findByNamedQuery(String queryName, Map<String, Object> params) {
        return findByNamedQuery(queryName, params, 0);
    }

    @Override
    public List findByNamedQuery(String queryName, Map<String, Object> params, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = params.entrySet();
        Query query = this.em.createNamedQuery(queryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public Long count(String nativeQuery) {
        Long R = 0L;
        try {
            R = (Long) em.createNativeQuery(nativeQuery).getSingleResult();
        } catch (Exception e) {
        }

        return R;
    }

    @Override
    public List findByQuery(String query, int resultLimit) {
        return this.em.createQuery(query).
                setMaxResults(resultLimit).
                getResultList();
    }

    @Override
    public List findByQuery(String query, Map<String, Object> params) {
        return findByQuery(query, params, 0);
    }

    @Override
    public List findByQuery(String consulta, Map<String, Object> params, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = params.entrySet();
        Query query = this.em.createQuery(consulta);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public Double findTotal(String consulta, Map<String, Object> params) {
        Query query = this.em.createQuery(consulta);
        Set<Entry<String, Object>> rawParameters = params.entrySet();

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        Object R = null;
        try {
            R = query.getSingleResult();
        } catch (Exception ex) {
        }

        if (R == null) {
            return 0d;
        } else {
            return R instanceof BigDecimal ? ((BigDecimal) R).doubleValue() : R instanceof Double ? (Double) R : R instanceof Long ? (Long) R : (Double) R;
        }
    }

    public void preparaRegistro(Object obj, String tipoOperacion) {
        if (obj instanceof Auditable) {
            Auditable a = (Auditable) obj;
            registrar(a, tipoOperacion);
        }
    }

    public void registrar(Auditable a, String tipoOperacion) {
        Auditoria aud = new Auditoria();
        aud.setFecha(new Date());
        aud.setTablaAfectada(a.getClass().getSimpleName()+": id = " + a.getId());
        aud.setTipoOperacion(tipoOperacion);
        aud.setUsuarioLogeado(credencial.getUsuario() == null ? "" : credencial.getUsuario().getUsuario());
        em.persist(aud);
    }
}

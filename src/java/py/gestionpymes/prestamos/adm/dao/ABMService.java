/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author cromero
 */
public interface ABMService {

    <T> T create(T t);

    void delete(Object t);

    <T> T update(T t);
    
    <T> T find(Object id, Class<T> type);

    <T> T findByNamedQuerySingleResult(String queryName,Map<String, Object> params);
    
    List findByNamedQuery(String queryName);

    List findByNamedQuery(String queryName, int resultLimit);

    List findByNamedQuery(String queryName, Map<String, Object> params);

    List findByNamedQuery(String queryName, Map<String, Object> params, int resultLimit);
    
    Long count(String nativeQuery);
    
    List findByQuery(String query, int resultLimit);

    List findByQuery(String query, Map<String, Object> params);
    
    List findByQuery(String query, Map<String, Object> params, int resultLimit);

    EntityManager getEM();
    
    Double findTotal(String query, Map<String, Object> params);
    
}

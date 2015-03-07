/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.util.List;

/**
 *
 * @author christian
 */
public abstract class AbstractDAO<T> {

    public abstract T create(T entity);

    public abstract T edit(T entity);

    public abstract void remove(T entity);

    public abstract T find(Object id);

    public abstract List<T> findAll();

    public abstract List<T> findAll(String query, QueryParameter params);
}

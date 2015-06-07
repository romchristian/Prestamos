/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.modelo.Moneda;

/**
 *
 * @author Acer
 */
@Stateless
public class MonedaFacade extends AbstractFacade<Moneda> {
    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MonedaFacade() {
        super(Moneda.class);
    }
    
    
    public Moneda findNombre(String nombre){
        return (Moneda) em.createQuery("SELECT m FROM Moneda m WHERE UPPER(TRIM(m.nombre)) = ?1")
                .setParameter(1, nombre.trim().toUpperCase())
                .getSingleResult();
    }
}

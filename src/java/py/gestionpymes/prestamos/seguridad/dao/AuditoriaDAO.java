/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.seguridad.dao;

import py.gestionpymes.prestamos.prestamos.dao.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditoria;

/**
 *
 * @author Acer
 */
@Stateless
public class AuditoriaDAO extends AbstractFacade<Auditoria> {
    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuditoriaDAO() {
        super(Auditoria.class);
    }
    
}

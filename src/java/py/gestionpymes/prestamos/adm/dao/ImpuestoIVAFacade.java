/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.adm.modelo.ImpuestoIVA;

/**
 *
 * @author Acer
 */
@Stateless
public class ImpuestoIVAFacade extends AbstractFacade<ImpuestoIVA> {
    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImpuestoIVAFacade() {
        super(ImpuestoIVA.class);
    }
    
}

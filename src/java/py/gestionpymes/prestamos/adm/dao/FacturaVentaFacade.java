/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.contabilidad.FacturaVenta;

/**
 *
 * @author Acer
 */
@Stateless
public class FacturaVentaFacade extends AbstractFacade<FacturaVenta> {
    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturaVentaFacade() {
        super(FacturaVenta.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.prestamos.dao.CuentaClienteDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;

/**
 *
 * @author Acer
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {
    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
     @EJB
    private CuentaClienteDAO cuentaClienteDAO;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    @Override
    public void create(Cliente entity) {
        super.create(entity); 
        cuentaClienteDAO.create(entity);
    }
    
    
    
}

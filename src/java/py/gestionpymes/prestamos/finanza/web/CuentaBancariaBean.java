/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.web;


import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.finanza.dao.CuentaBancariaDAO;
import py.gestionpymes.prestamos.finanza.modelo.CuentaBancaria;



/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class CuentaBancariaBean extends BeanGenerico<CuentaBancaria>{

    @EJB private CuentaBancariaDAO ejb;
    
    
    @Override
    public AbstractDAO<CuentaBancaria> getEjb() {
        return ejb;
    }

    @Override
    public CuentaBancaria getNuevo() {
        return new CuentaBancaria();
    }

    
}

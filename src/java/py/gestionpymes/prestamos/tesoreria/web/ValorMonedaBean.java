/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.tesoreria.modelo.ValorMoneda;
import py.gestionpymes.prestamos.tesoreria.dao.ValorMonedaDAO;


/**
 *
 * @author christian
 */
@ManagedBean
@RequestScoped
public class ValorMonedaBean extends BeanGenerico<ValorMoneda>{

    @EJB private ValorMonedaDAO ejb;
    
    
    @Override
    public AbstractDAO<ValorMoneda> getEjb() {
        return ejb;
    }

    @Override
    public ValorMoneda getNuevo() {
        return new ValorMoneda();
    }

    
}

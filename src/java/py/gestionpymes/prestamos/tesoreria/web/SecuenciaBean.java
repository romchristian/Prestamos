/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;


import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Secuencia;
import py.gestionpymes.prestamos.tesoreria.dao.SecuenciaDAO;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class SecuenciaBean extends BeanGenerico<Secuencia>{

    @EJB private SecuenciaDAO ejb;
    
    
    @Override
    public AbstractDAO<Secuencia> getEjb() {
        return ejb;
    }

    @Override
    public Secuencia getNuevo() {
        return new Secuencia();
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web;


import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.CargoPorMoraDAO;
import py.gestionpymes.prestamos.adm.modelo.CargoPorMora;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;



/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class CargoPorMoraBean extends BeanGenerico<CargoPorMora>{

    @EJB private CargoPorMoraDAO ejb;
    
    
    @Override
    public AbstractDAO<CargoPorMora> getEjb() {
        return ejb;
    }

    @Override
    public CargoPorMora getNuevo() {
        return new CargoPorMora();
    }

    
}

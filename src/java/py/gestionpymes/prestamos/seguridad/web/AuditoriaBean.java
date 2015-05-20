/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.seguridad.web;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.seguridad.dao.AuditoriaDAO;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditoria;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class AuditoriaBean extends BeanGenerico<Auditoria> {

    @EJB
    private AuditoriaDAO ejb;

    @Override
    public AbstractDAO<Auditoria> getEjb() {
        return ejb;
    }

    @Override
    public Auditoria getNuevo() {
        return new Auditoria();
    }

    public void guardar(Auditoria a) {
        ejb.crear(a);
      
    }
    
    
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestion.contabilidad.servicio.DiarioDAO;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.contabilidad.persistencia.Diario;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class DiarioBean extends BeanGenerico<Diario> {

    @EJB
    private DiarioDAO ejb;

    @Override
    public AbstractDAO<Diario> getEjb() {
        return ejb;
    }

    @Override
    public Diario getNuevo() {
        return new Diario();
    }

}

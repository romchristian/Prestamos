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
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class TransaccionBean extends BeanGenerico<Transaccion>{

    @EJB private TransaccionDAO ejb;
    
    
    @Override
    public AbstractDAO<Transaccion> getEjb() {
        return ejb;
    }

    @Override
    public Transaccion getNuevo() {
        return new Transaccion();
    }

    
}

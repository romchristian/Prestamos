/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;


import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class TransaccionBean extends BeanGenerico<Transaccion>{

    @EJB private TransaccionDAO ejb;
    @Inject
    private SesionTPVBean sesionTPVBean;
    
    
    @Override
    public AbstractDAO<Transaccion> getEjb() {
        return ejb;
    }

    @Override
    public Transaccion getNuevo() {
        return new Transaccion();
    }

    @Override
    public String create() {
        
        getActual().setSesionTPV(sesionTPVBean.getActual());
        getActual().setPuntoVenta(sesionTPVBean.getActual().getPuntoVenta());
        if (getEjb().create(getActual()) != null) {
            JsfUtil.addSuccessMessage("Se creó exitosamente!");
            setActual(null);
            sesionTPVBean.actualizaTotalTransacciones();
            return "terminalCaja.xhtml";
        } else {
            return  null;
        }
    }

    
    
    
}

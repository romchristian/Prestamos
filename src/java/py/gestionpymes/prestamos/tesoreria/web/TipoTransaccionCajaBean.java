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
import py.gestionpymes.prestamos.tesoreria.dao.TipoTransaccionCajaDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccionCaja;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class TipoTransaccionCajaBean extends BeanGenerico<TipoTransaccionCaja>{

    @EJB private TipoTransaccionCajaDAO ejb;
    
    
    
    @Override
    public AbstractDAO<TipoTransaccionCaja> getEjb() {
        return ejb;
    }

    @Override
    public TipoTransaccionCaja getNuevo() {
        return new TipoTransaccionCaja();
    }

      
    
}

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
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;

import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.dao.PuntoVentaDAO;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class PuntoVentaBean extends BeanGenerico<PuntoVenta>{

    @EJB private PuntoVentaDAO ejb;
    private MetodoPago metodoSeleccionado;

    public MetodoPago getMetodoSeleccionado() {
        return metodoSeleccionado;
    }

    public void setMetodoSeleccionado(MetodoPago metodoSeleccionado) {
        this.metodoSeleccionado = metodoSeleccionado;
    }
   
    public void agregaMetodoPago(){
        getActual().addMetodoPago(metodoSeleccionado);
    }
    
    public void remueveMetodoPago(MetodoPago m){
        getActual().removeMetodoPago(m);
    }
    
    @Override
    public AbstractDAO<PuntoVenta> getEjb() {
        return ejb;
    }

    @Override
    public PuntoVenta getNuevo() {
        return new PuntoVenta();
    }

    
}

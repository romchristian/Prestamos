/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.servicio.MetodoPagoDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorMoneda;


/**
 *
 * @author christian
 */
@ManagedBean
@SessionScoped
public class MetodoPagoBean extends BeanGenerico<MetodoPago> {
    
    @EJB
    private MetodoPagoDAO ejb;
    private ValorMoneda valorMoneda;
    
    @Override
    public AbstractDAO<MetodoPago> getEjb() {
        return ejb;
    }
    
    @Override
    public MetodoPago getNuevo() {
        return new MetodoPago();
    }
    
    public ValorMoneda getValorMoneda() {
        if (valorMoneda == null) {
            valorMoneda = new ValorMoneda();
        }
        return valorMoneda;
    }
    
    public void setValorMoneda(ValorMoneda valorMoneda) {
        this.valorMoneda = valorMoneda;
    }
    
    public String reinit() {
        valorMoneda = new ValorMoneda();
        return null;
    }
    
    public void agregar() {
        if (getActual().getValoresMonedas() == null) {
            getActual().setValoresMonedas(new ArrayList<ValorMoneda>());
        }
        
        valorMoneda.setMetodoPago(getActual());
        getActual().getValoresMonedas().add(valorMoneda);
        valorMoneda = new ValorMoneda();
    }
}

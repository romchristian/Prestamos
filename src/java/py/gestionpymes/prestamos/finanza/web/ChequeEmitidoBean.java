/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.finanza.dao.CambioEstadoChequeEmitidoException;
import py.gestionpymes.prestamos.finanza.dao.ChequeEmitidoDAO;
import py.gestionpymes.prestamos.finanza.modelo.ChequeEmitido;
import py.gestionpymes.prestamos.tesoreria.dao.SecuenciaDAO;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class ChequeEmitidoBean extends BeanGenerico<ChequeEmitido> {

    @EJB
    private ChequeEmitidoDAO ejb;
    @EJB
    private SecuenciaDAO ejbSecuencia;
    
    private String descripcionOperacion;
    

    @Override
    public AbstractDAO<ChequeEmitido> getEjb() {
        return ejb;
    }

    @Override
    public ChequeEmitido getNuevo() {
        return new ChequeEmitido();
    }

    public String getDescripcionOperacion() {
        return descripcionOperacion;
    }

    public void setDescripcionOperacion(String descripcionOperacion) {
        this.descripcionOperacion = descripcionOperacion;
    }

    
    
    
    public void siCambiaChequera() {
        if (getActual() != null && getActual().getSecuencia() != null) {
            getActual().setNumero(getActual().getSecuencia().obtSiguienteNumero() + "");
        }
    }

    public void siCambiaCuenta() {
        if (getActual() != null && getActual().getCuentaBancaria() != null) {
            getActual().setMoneda(getActual().getCuentaBancaria().getMoneda());
        }
    }

    @Override
    public String edit() {
        String R = null;
        Secuencia s = getActual().getSecuencia();

        try {
            s.setUltimoNumero(Long.parseLong(getActual().getNumero()));
            ejbSecuencia.edit(s);
            R = super.edit();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Se espera un número");
        }

        return R;
    }

    
    public void autoriza(){
        try {
            ChequeEmitido ch = ejb.autorizar(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    
    public void compromete(){
        try {
            ChequeEmitido ch = ejb.comprometer(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    public void cobra(){
        try {
            ChequeEmitido ch = ejb.cobrar(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    
    public void anula(){
        try {
            ChequeEmitido ch = ejb.anular(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    public void reversar(){
        try {
            ChequeEmitido ch = ejb.revesar(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    
    public void comentario(){
        try {
            ChequeEmitido ch = ejb.comentario(getActual(), descripcionOperacion);
            descripcionOperacion=null;
        } catch (CambioEstadoChequeEmitidoException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
}

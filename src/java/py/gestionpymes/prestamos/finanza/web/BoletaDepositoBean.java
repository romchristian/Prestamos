/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.web;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.finanza.dao.BoletaDepositoDAO;
import py.gestionpymes.prestamos.finanza.dao.CuentaBancariaDAO;
import py.gestionpymes.prestamos.finanza.dao.TransaccionBancariaNoCreadaException;
import py.gestionpymes.prestamos.finanza.persistencia.BoletaDeposito;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class BoletaDepositoBean extends BeanGenerico<BoletaDeposito> {

    @EJB
    private BoletaDepositoDAO ejb;
    @EJB
    private CuentaBancariaDAO ejbCuenta;

    @Override
    public AbstractDAO<BoletaDeposito> getEjb() {
        return ejb;
    }

    @Override
    public BoletaDeposito getNuevo() {
        return new BoletaDeposito();
    }
    
      public void deposita(){
        try {
            
            ejbCuenta.deposita(getActual());
            
        } catch (TransaccionBancariaNoCreadaException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            
        }
    }

}

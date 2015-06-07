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

    @Override
    public AbstractDAO<ChequeEmitido> getEjb() {
        return ejb;
    }

    @Override
    public ChequeEmitido getNuevo() {
        return new ChequeEmitido();
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

}

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
import py.gestionpymes.prestamos.finanza.modelo.BoletaDeposito;

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

    public void deposita() {
        try {

            ejbCuenta.deposita(getActual());
            JsfUtil.addSuccessMessage("Se confirmó existosamente!");
        } catch (TransaccionBancariaNoCreadaException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());

        }
    }

    public void revertir() {
        try {

            BoletaDeposito b = ejb.revertir(getActual());
            setActual(b);
            JsfUtil.addSuccessMessage("Se revirtió exitosamente!");
        } catch (TransaccionBancariaNoCreadaException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());

        }
    }

    @Override
    public String create() {
        BoletaDeposito b = getEjb().create(getActual());
        if (b != null) {
            JsfUtil.addSuccessMessage("Se creó exitosamente!");
            setActual(null);
            return "vista.xhtml?faces-redirect=true&id=" + b.getId();
        } else {
            return null;
        }

    }

    @Override
    public String edit() {
        if (getEjb().edit(getActual()) == null) {
            JsfUtil.addErrorMessage("Otro usuario realizó una modificación sobre el mismo dato,y pruebe de nuevo");
            return null;
        }

        JsfUtil.addSuccessMessage("Se guardó exitosamente!");
        //setActual(null);
        return "vista.xhtml?faces-redirect=true&id=" + getActual().getId();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.modelo.MetodoPago;

import py.gestionpymes.prestamos.tesoreria.modelo.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.dao.PuntoVentaDAO;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class PuntoVentaBean extends BeanGenerico<PuntoVenta> {

    @EJB
    private PuntoVentaDAO ejb;
    private MetodoPago metodoSeleccionado;
    private BigDecimal saldoAjustado;
    @EJB
    private MonedaFacade monedaFacade;

    public BigDecimal getSaldoAjustado() {
        return saldoAjustado;
    }

    public void setSaldoAjustado(BigDecimal saldoAjustado) {
        this.saldoAjustado = saldoAjustado;
    }

    public MetodoPago getMetodoSeleccionado() {
        return metodoSeleccionado;
    }

    public void setMetodoSeleccionado(MetodoPago metodoSeleccionado) {
        this.metodoSeleccionado = metodoSeleccionado;
    }

    public void agregaMetodoPago() {
        getActual().addMetodoPago(metodoSeleccionado);
    }

    public void remueveMetodoPago(MetodoPago m) {
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

    @Override
    public String create() {
        PuntoVenta creado = getEjb().create(getActual());
        if (creado != null) {
            ajustaSaldo();
            JsfUtil.addSuccessMessage("Se creó exitosamente!");
            setActual(null);
            return "listado.xhtml?faces-redirect=true";
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

         ajustaSaldo();
        JsfUtil.addSuccessMessage("Se guardó exitosamente!");
        setActual(null);
        return "listado.xhtml?faces-redirect=true";
    }

    
    
    public void ajustaSaldo() {
      //  if (getActual() != null && saldoAjustado != null && saldoAjustado.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
            ejb.ajustaSaldo(getActual(), monedaFacade.findNombre("GUARANIES"), saldoAjustado);
       // }

    }

}

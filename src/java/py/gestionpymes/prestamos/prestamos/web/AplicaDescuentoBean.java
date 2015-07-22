/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.modelo.DescuentoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;
import py.gestionpymes.prestamos.prestamos.modelo.TipoDescuento;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class AplicaDescuentoBean implements Serializable {

    @EJB
    private PrestamoDAO ejb;
    private Prestamo actual;
    private long id;
    private DetPrestamo selecccionado;
    private List<DetPrestamo> cuotasPedientes;

    private DescuentoCuota nuevo = new DescuentoCuota();
    private DescuentoTemp descuentoTemp;

    public DescuentoTemp getDescuentoTemp() {
        return descuentoTemp;
    }

    public void setDescuentoTemp(DescuentoTemp descuentoTemp) {
        this.descuentoTemp = descuentoTemp;
    }

    public DetPrestamo getSelecccionado() {
        return selecccionado;
    }

    public void setSelecccionado(DetPrestamo selecccionado) {
        this.selecccionado = selecccionado;
    }

    public List<DetPrestamo> getCuotasPedientes() {
        return cuotasPedientes;
    }

    public void setCuotasPedientes(List<DetPrestamo> cuotasPedientes) {
        this.cuotasPedientes = cuotasPedientes;
    }

    public Prestamo getActual() {
        if (actual == null) {
            actual = new Prestamo();
        }
        return actual;
    }

    public void setActual(Prestamo actual) {
        this.actual = actual;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void cargaDatos() {
        if (id > 0) {
            actual = ejb.find(id);
            cuotasPedientes = ejb.findCuotasPendientes(actual);
        }
    }

    public String guardar() {
        ejb.guardarDescuentos(cuotasPedientes);

        return "List.xhtml";
    }

    public void agregaDescuento() {

        if (descuentoTemp.isAplicaDescuentoMora()
                && descuentoTemp.getDescuentoMora() != null
                && descuentoTemp.getDescuentoMora().compareTo(BigDecimal.ZERO) > 0) {
            agregaDescuentoGenenrico(TipoDescuento.MORA, descuentoTemp.getDescuentoMora());
        }

        if (descuentoTemp.isAplicaDescuentoInteres()
                && descuentoTemp.getDescuentoInteres() != null
                && descuentoTemp.getDescuentoInteres().compareTo(BigDecimal.ZERO) > 0) {
            agregaDescuentoGenenrico(TipoDescuento.INTERES, descuentoTemp.getDescuentoInteres());
        }

        if (descuentoTemp.isAplicaDescuentoCargo()
                && descuentoTemp.getDescuentoCargo() != null
                && descuentoTemp.getDescuentoCargo().compareTo(BigDecimal.ZERO) > 0) {
            agregaDescuentoGenenrico(TipoDescuento.CARGOS, descuentoTemp.getDescuentoCargo());
        }

        descuentoTemp = null;
        
    }

    public void agregaDescuentoGenenrico(TipoDescuento tipo, BigDecimal monto) {

        BigDecimal descuentoAcumulado = selecccionado.obtDescuentoAcumulado(tipo).add(monto);
        BigDecimal cargoActual = BigDecimal.ZERO;

        switch (tipo) {
            case MORA:
                cargoActual = selecccionado.calculaSaldoMoratorio().add(selecccionado.calculaSaldoPunitorio());
                break;
            case INTERES:
                cargoActual = selecccionado.obtCuotaInteresConIva();
                break;
            case CARGOS:
                cargoActual = selecccionado.getPendienteCargo();
                break;
        }

        if (cargoActual.compareTo(descuentoAcumulado) >= 0) {

            DescuentoCuota d = new DescuentoCuota();
            d.setDetPrestamo(descuentoTemp.getDetPrestamo());
            d.setFecha(new Date());
            d.setTipo(tipo);
            d.setMonto(monto);

            if (selecccionado.getDescuentoCuotas() == null) {
                selecccionado.setDescuentoCuotas(new ArrayList<DescuentoCuota>());
            }

            selecccionado.getDescuentoCuotas().add(d);

        } else {
            JsfUtil.addErrorMessage("El monto del descuento excede el maximo!");

        }

    }

    
     public void onRowUnSelect(SelectEvent event) {
        selecccionado = null;
        descuentoTemp = null;
    }
    public void onRowSelect(SelectEvent event) {
        selecccionado = (DetPrestamo) event.getObject();
        descuentoTemp = new DescuentoTemp();
        descuentoTemp.setNroCuota(selecccionado.getNroCuota());
        BigDecimal mora = selecccionado.calculaSaldoMoratorio().add(selecccionado.calculaSaldoPunitorio());
        descuentoTemp.setDescuentoMora(mora.subtract(selecccionado.obtDescuentoAcumulado(TipoDescuento.MORA)));
        descuentoTemp.setDescuentoCargo(selecccionado.getPendienteCargo());
        descuentoTemp.setDescuentoInteres(selecccionado.obtCuotaInteresConIva().subtract(selecccionado.obtDescuentoAcumulado(TipoDescuento.INTERES)));
        //descuentoTemp.setDescuentoInteres(selecccionado.obtCuotaInteresConIva().subtract(selecccionado.obtDescuentoAcumulado(TipoDescuento.INTERES)));
        descuentoTemp.setDetPrestamo(selecccionado);
    }

    public void removerDescuento(DescuentoCuota d) {
        if (selecccionado != null) {
            int indice = 0;
            for (DescuentoCuota d1 : selecccionado.getDescuentoCuotas()) {
                if (d1.getDetPrestamo().equals(d.getDetPrestamo()) && d1.getTipo() == d.getTipo() && d1.getMonto().compareTo(d.getMonto()) == 0) {
                    break;
                }
                indice++;
            }

            selecccionado.getDescuentoCuotas().remove(indice);
        }
    }
}

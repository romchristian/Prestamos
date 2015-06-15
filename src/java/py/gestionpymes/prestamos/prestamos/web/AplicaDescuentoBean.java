/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.modelo.DescuentoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;

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
    private List<DescuentoCuota> descuentos;
    private DescuentoCuota nuevo = new DescuentoCuota();
    private DescuentoTemp descuentoTemp;

    public DescuentoTemp getDescuentoTemp() {
        return descuentoTemp;
    }

    public void setDescuentoTemp(DescuentoTemp descuentoTemp) {
        this.descuentoTemp = descuentoTemp;
    }
    
    

    public List<DescuentoCuota> getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(List<DescuentoCuota> descuentos) {
        this.descuentos = descuentos;
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
    
    public void cargaDatos(){
        if(id > 0){
            actual = ejb.find(id);
            cuotasPedientes = ejb.findCuotasPendientes(actual);
        }
    }
    
    public String guardar(){
        
        return "List.xhtml";
    }

    
    public void agregaDescuento(){
        if(descuentos == null)
            descuentos = new ArrayList<>();
        
        descuentos.add(nuevo);
    }
    
    
    public void onRowSelect(SelectEvent event) {
        selecccionado = (DetPrestamo) event.getObject();
        descuentoTemp = new DescuentoTemp();
        descuentoTemp.setNroCuota(selecccionado.getNroCuota());
        descuentoTemp.setDescuentoMora(selecccionado.calculaSaldoMoratorio().add(selecccionado.calculaSaldoPunitorio()));
        descuentoTemp.setDescuentoCargo(BigDecimal.ZERO);
        descuentoTemp.setDescuentoInteres(selecccionado.getCuotaInteres());
    }
}

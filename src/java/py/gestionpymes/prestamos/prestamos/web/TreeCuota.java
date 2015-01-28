/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.util.Date;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;


/**
 *
 * @author cromero
 */
public class TreeCuota {
    private Prestamo prestamo;
    
    private DetPrestamo detPrestamo;
    private Moneda moneda;
    
    
    private String descPrestamo;
    private String descDetPrestamo;
    private Date fechaVencimiento;
    private int nroCuota;
    private Double montoCuota;
    private Integer diasMora;
    private Double montoMora;
    private Double montoPago;
    private Double saldoCuota;
    private boolean esPrestamo;
    private boolean cancelado;
    private boolean modoEdicion;
    private boolean seleccionado;

    public TreeCuota() {
    }

    
    
    public TreeCuota(Prestamo prestamo) {
        this.prestamo = prestamo;
        this.descPrestamo = "Prestamo # " + prestamo.getId();
        this.esPrestamo = true;
        this.moneda = prestamo.getMoneda();
    }

    public TreeCuota(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
        this.descDetPrestamo = "Cuota # " + detPrestamo.getNroCuota();
        this.fechaVencimiento = detPrestamo.getFechaVencimiento();
        this.nroCuota = detPrestamo.getNroCuota();
        this.montoCuota = detPrestamo.getMontoCuota();
        this.diasMora = detPrestamo.getDiasMora();
        this.montoMora = detPrestamo.getMontoMora();
        this.montoPago = detPrestamo.getMontoPago();
        this.saldoCuota = detPrestamo.getSaldoCuota();
        this.prestamo = detPrestamo.getPrestamo();
        this.moneda = detPrestamo.getPrestamo().getMoneda();
        this.esPrestamo = false;
        this.cancelado = true;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
 
    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    

    
    public String getDescDetPrestamo() {
        return descDetPrestamo;
    }

    public void setDescDetPrestamo(String descDetPrestamo) {
        this.descDetPrestamo = descDetPrestamo;
    }

    public String getDescPrestamo() {
        return descPrestamo;
    }

    public void setDescPrestamo(String descPrestamo) {
        this.descPrestamo = descPrestamo;
    }

    public DetPrestamo getDetPrestamo() {
        return detPrestamo;
    }

    public void setDetPrestamo(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
    }

    public Integer getDiasMora() {
        return diasMora;
    }

    public void setDiasMora(Integer diasMora) {
        this.diasMora = diasMora;
    }

    public boolean isEsPrestamo() {
        return esPrestamo;
    }

    public void setEsPrestamo(boolean esPrestamo) {
        this.esPrestamo = esPrestamo;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(Double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public Double getMontoMora() {
        return montoMora;
    }

    public void setMontoMora(Double montoMora) {
        this.montoMora = montoMora;
    }

    public Double getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(Double montoPago) {
        this.montoPago = montoPago;
    }

    

    public int getNroCuota() {
        return nroCuota;
    }

    public void setNroCuota(int nroCuota) {
        this.nroCuota = nroCuota;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public Double getSaldoCuota() {
        return saldoCuota;
    }

    public void setSaldoCuota(Double saldoCuota) {
        this.saldoCuota = saldoCuota;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    
    
    
}

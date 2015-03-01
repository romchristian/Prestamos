/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoDetPrestamo;


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
    private BigDecimal montoCuota;
    private Integer diasMora;
    private BigDecimal montoMora;
    private BigDecimal montoPago;
    private BigDecimal montoAPagar;
    private BigDecimal saldoCuota;
    private boolean esPrestamo;
    private boolean cancelado;
    private boolean modoEdicion;
    private boolean seleccionado;
    private Empresa empresa;
    private Sucursal sucursal;
    private BigDecimal cuotaCapital;
    private BigDecimal cuotaInteres;
    private BigDecimal montoMoratorio;
    private BigDecimal montoPunitorio;
    private BigDecimal descuento;
    private boolean tieneDescuento;
    private boolean disabledPagar;
    private TreeNode padre;
    

    public TreeCuota() {
    }

    
    
    public TreeCuota(Prestamo prestamo) {
        this.prestamo = prestamo;
        this.empresa = prestamo.getEmpresa();
        this.sucursal = prestamo.getSucursal();
        NumberFormat nf = NumberFormat.getInstance(new Locale("es","py"));
        this.descPrestamo = "Prestamo # " + prestamo.getId() +" - "+ nf.format(prestamo.getMontoPrestamo());
        this.esPrestamo = true;
        this.moneda = prestamo.getMoneda();
    }

    public TreeCuota(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
        this.descDetPrestamo = "Cuota # " + detPrestamo.getNroCuota();
        this.fechaVencimiento = detPrestamo.getFechaVencimiento();
        this.nroCuota = detPrestamo.getNroCuota();
        this.montoCuota = detPrestamo.getMontoCuota().setScale(0, RoundingMode.HALF_EVEN);
        this.diasMora = detPrestamo.getDiasMora();
        
        this.descuento = detPrestamo.getDescuento();
        this.montoMoratorio = detPrestamo.calculaSaldoMoratorio();
        this.montoPunitorio = detPrestamo.calculaSaldoPunitorio();
        this.cuotaCapital = detPrestamo.getCuotaCapital();
        this.cuotaInteres = this.montoCuota.subtract(this.cuotaCapital);
        
        //this.montoMora = detPrestamo.devuelveMontoMora().setScale(0, RoundingMode.HALF_EVEN);
        if(detPrestamo.getEstado() == EstadoDetPrestamo.CANCELADO){
            this.montoMora = detPrestamo.getMoraMoratorio().add(detPrestamo.getMoraPunitorio());
        }else{
            this.montoMora = detPrestamo.devuelveMontoMora().setScale(0, RoundingMode.HALF_EVEN);
        }
        
        this.montoPago = detPrestamo.getMontoPago().setScale(0, RoundingMode.HALF_EVEN);
        this.saldoCuota = detPrestamo.getSaldoCuota().setScale(0, RoundingMode.HALF_EVEN);
        this.prestamo = detPrestamo.getPrestamo();
        this.empresa = detPrestamo.getPrestamo().getEmpresa();
        this.sucursal = detPrestamo.getPrestamo().getSucursal();
        this.moneda = detPrestamo.getPrestamo().getMoneda();
        this.esPrestamo = false;
        this.cancelado = true;
    }

    public boolean isTieneDescuento() {
        if(tieneDescuento && descuento != null && descuento.compareTo(new BigDecimal(BigInteger.ZERO))> 0){
            tieneDescuento = true;
        }else {
            tieneDescuento = false;
        }
        return tieneDescuento;
    }

    public void setTieneDescuento(boolean tieneDescuento) {
        this.tieneDescuento = tieneDescuento;
    }

    
    
    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    
    

    public TreeNode getPadre() {
        return padre;
    }

    public void setPadre(TreeNode padre) {
        this.padre = padre;
    }

    
    public boolean isDisabledPagar() {
        return disabledPagar;
    }

    public void setDisabledPagar(boolean sePuedePagar) {
        this.disabledPagar = sePuedePagar;
    }
    
    
    public BigDecimal getMontoAPagar() {
        return montoAPagar;
    }

    public void setMontoAPagar(BigDecimal montoAPagar) {
        this.montoAPagar = montoAPagar;
    }

    public BigDecimal getCuotaCapital() {
        return cuotaCapital;
    }

    public void setCuotaCapital(BigDecimal cuotaCapital) {
        this.cuotaCapital = cuotaCapital;
    }

    public BigDecimal getCuotaInteres() {
        return cuotaInteres;
    }

    public void setCuotaInteres(BigDecimal cuotaInteres) {
        this.cuotaInteres = cuotaInteres;
    }
    
    

    public BigDecimal getMontoMoratorio() {
        return montoMoratorio;
    }

    public void setMontoMoratorio(BigDecimal montoMoratorio) {
        this.montoMoratorio = montoMoratorio;
    }

    public BigDecimal getMontoPunitorio() {
        return montoPunitorio;
    }

    public void setMontoPunitorio(BigDecimal montoPunitorio) {
        this.montoPunitorio = montoPunitorio;
    }

    
    
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
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

    public BigDecimal getMontoCuota() {
        
        return montoCuota;
    }

    public void setMontoCuota(BigDecimal montoCuota) {
        this.montoCuota = montoCuota;
    }

    public BigDecimal getMontoMora() {
        return montoMora;
    }

    public void setMontoMora(BigDecimal montoMora) {
        this.montoMora = montoMora;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
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

    public BigDecimal getSaldoCuota() {
        return saldoCuota;
    }

    public void setSaldoCuota(BigDecimal saldoCuota) {
        this.saldoCuota = saldoCuota;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    
    
    
}

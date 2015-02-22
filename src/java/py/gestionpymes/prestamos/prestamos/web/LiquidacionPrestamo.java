    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import static java.rmi.Naming.list;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author Acer
 */
public class LiquidacionPrestamo {

    private Long id;
    //encabezado
    private String empresa;
    private String sucursal;
    private String vendedor;
    private String cliente;
    private String codeudor;
    private BigDecimal montoPrestamo = new BigDecimal(BigInteger.ZERO);
    private BigDecimal capital = new BigDecimal(BigInteger.ZERO);
    private int plazo;
    private int tasa;
    private String periodoPago;
    private BigDecimal gastos = new BigDecimal(BigInteger.ZERO);
    private BigDecimal comisiones = new BigDecimal(BigInteger.ZERO);
    private BigDecimal impuestoIVA = new BigDecimal(BigInteger.ZERO);
    private Date fechaInicioOperacion;
    private BigDecimal montoDeCuota = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalIntereses = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalOperacion = new BigDecimal(BigInteger.ZERO);
    private String sistemaAmortizacion;
    private String moneda;
    //detalle
    private int nroCuota;
    private Date fechaVencimiento;
    private BigDecimal saldoCapital = new BigDecimal(BigInteger.ZERO);
     private BigDecimal montoCuota = new BigDecimal(BigInteger.ZERO);
    private BigDecimal cuotaCapital = new BigDecimal(BigInteger.ZERO);// monto del prestamo / plazo
    private BigDecimal cuotaIntereses = new BigDecimal(BigInteger.ZERO);// total interes / plazo
    private BigDecimal impuestoIvaCuota = new BigDecimal(BigInteger.ZERO);
    private DetPrestamo detPrestamo;
    private boolean firmaConyugeTitular;
    private boolean firmaConyugeCodeudor;

    public LiquidacionPrestamo(DetPrestamo dp) {

        this.detPrestamo = dp;

        this.id = dp.getId();
        setEmpresa(this.detPrestamo.getPrestamo().getEmpresa()== null ? null : this.detPrestamo.getPrestamo().getEmpresa().getRazonSocial());
        setSucursal(this.detPrestamo.getPrestamo().getSucursal()== null ? null : this.detPrestamo.getPrestamo().getSucursal().getNombre());
        setVendedor(this.detPrestamo.getPrestamo().getVendedor()== null ? null : this.detPrestamo.getPrestamo().getVendedor().getNombres()+" "+this.detPrestamo.getPrestamo().getVendedor().getApellidos());
        setCliente(this.detPrestamo.getPrestamo().getCliente() == null ? null : this.detPrestamo.getPrestamo().getCliente().devuelveNombreCompleto());
        setCodeudor(this.detPrestamo.getPrestamo().getCodeudor() == null ? null : this.detPrestamo.getPrestamo().getCodeudor().devuelveNombreCompleto());
        this.montoPrestamo = dp.getPrestamo().getMontoPrestamo().setScale(0, RoundingMode.HALF_EVEN);
        this.capital = dp.getPrestamo().getCapital().setScale(0, RoundingMode.HALF_EVEN);
        this.plazo = dp.getPrestamo().getPlazo();
        //cambiar tipo en Reporte BigDecimal
        this.tasa = dp.getPrestamo().getTasa().intValue();
        this.periodoPago = dp.getPrestamo().getPeriodoPago().name();
        this.gastos = dp.getPrestamo().getGastos().setScale(0, RoundingMode.HALF_EVEN);
        this.comisiones = dp.getPrestamo().getGastos().setScale(0, RoundingMode.HALF_EVEN);
        this.impuestoIVA = dp.getPrestamo().getImpuestoIVA().setScale(0, RoundingMode.HALF_EVEN);
        this.fechaInicioOperacion = dp.getPrestamo().getFechaInicioOperacion();
        this.montoDeCuota = dp.getPrestamo().getMontoCuota().setScale(0, RoundingMode.HALF_EVEN);
        this.totalIntereses = dp.getPrestamo().getTotalIntereses().setScale(0, RoundingMode.HALF_EVEN);
        this.totalOperacion = dp.getPrestamo().getTotalOperacion().setScale(0, RoundingMode.HALF_EVEN);
        this.sistemaAmortizacion = dp.getPrestamo().getSistemaAmortizacion().name();
        setMoneda(this.detPrestamo.getPrestamo().getMoneda() == null ? null : this.detPrestamo.getPrestamo().getMoneda().getNombre());
        this.firmaConyugeCodeudor = dp.getPrestamo().isFirmaConyugeCodeudor();
        this.firmaConyugeTitular = dp.getPrestamo().isFirmaConyugeTitular();
        //detalles
        this.nroCuota = dp.getNroCuota();
        this.fechaVencimiento = dp.getFechaVencimiento();
        this.montoCuota = dp.getMontoCuota().setScale(0, RoundingMode.HALF_EVEN);
        this.saldoCapital = dp.getSaldoCapital().setScale(0, RoundingMode.HALF_EVEN);
        this.cuotaCapital = dp.getCuotaCapital().setScale(0, RoundingMode.HALF_EVEN);// monto del prestamo / plazo
        this.cuotaIntereses = dp.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN);// total interes / plazo
        this.impuestoIvaCuota = dp.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN);

    }
    
   
    public boolean isFirmaConyugeTitular() {
        return firmaConyugeTitular;
    }

    public void setFirmaConyugeTitular(boolean firmaConyugeTitular) {
        this.firmaConyugeTitular = firmaConyugeTitular;
    }

    public boolean isFirmaConyugeCodeudor() {
        return firmaConyugeCodeudor;
    }

    public void setFirmaConyugeCodeudor(boolean firmaConyugeCodeudor) {
        this.firmaConyugeCodeudor = firmaConyugeCodeudor;
    }

    public BigDecimal getMontoDeCuota() {
        return montoDeCuota;
    }

    public void setMontoDeCuota(BigDecimal montoDeCuota) {
        this.montoDeCuota = montoDeCuota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCodeudor() {
        return codeudor;
    }

    public void setCodeudor(String codeudor) {
        this.codeudor = codeudor;
    }

    public BigDecimal getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigDecimal montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public int getTasa() {
        return tasa;
    }

    public void setTasa(int tasa) {
        this.tasa = tasa;
    }

    public String getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(String periodoPago) {
        this.periodoPago = periodoPago;
    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos;
    }

    public BigDecimal getComisiones() {
        return comisiones;
    }

    public void setComisiones(BigDecimal comisiones) {
        this.comisiones = comisiones;
    }

    public BigDecimal getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(BigDecimal impuestoIVA) {
        this.impuestoIVA = impuestoIVA;
    }

    public Date getFechaInicioOperacion() {
        return fechaInicioOperacion;
    }

    public void setFechaInicioOperacion(Date fechaInicioOperacion) {
        this.fechaInicioOperacion = fechaInicioOperacion;
    }

    public BigDecimal getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(BigDecimal montoCuota) {
        this.montoCuota = montoCuota;
    }

    public BigDecimal getTotalIntereses() {
        return totalIntereses;
    }

    public void setTotalIntereses(BigDecimal totalIntereses) {
        this.totalIntereses = totalIntereses;
    }

    public BigDecimal getTotalOperacion() {
        return totalOperacion;
    }

    public void setTotalOperacion(BigDecimal totalOperacion) {
        this.totalOperacion = totalOperacion;
    }

    public String getSistemaAmortizacion() {
        return sistemaAmortizacion;
    }

    public void setSistemaAmortizacion(String sistemaAmortizacion) {
        this.sistemaAmortizacion = sistemaAmortizacion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getNroCuota() {
        return nroCuota;
    }

    public void setNroCuota(int nroCuota) {
        this.nroCuota = nroCuota;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(BigDecimal saldoCapital) {
        this.saldoCapital = saldoCapital;
    }

    public BigDecimal getCuotaCapital() {
        return cuotaCapital;
    }

    public void setCuotaCapital(BigDecimal cuotaCapital) {
        this.cuotaCapital = cuotaCapital;
    }

    public BigDecimal getCuotaIntereses() {
        return cuotaIntereses;
    }

    public void setCuotaIntereses(BigDecimal cuotaIntereses) {
        this.cuotaIntereses = cuotaIntereses;
    }

    public BigDecimal getImpuestoIvaCuota() {
        return impuestoIvaCuota;
    }

    public void setImpuestoIvaCuota(BigDecimal impuestoIvaCuota) {
        this.impuestoIvaCuota = impuestoIvaCuota;
    }

    public DetPrestamo getDetPrestamo() {
        return detPrestamo;
    }

    public void setDetPrestamo(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
    }
    
    
}

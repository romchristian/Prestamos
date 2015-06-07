/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import py.gestionpymes.prestamos.prestamos.modelo.enums.PeriodoPago;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.modelo.Cotizacion;
import py.gestionpymes.prestamos.adm.modelo.Empresa;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.adm.modelo.Vendedor;

/**
 *
 * @author christian
 */
@Entity

public class PrestamoHistorico implements Serializable {

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetPrestamoHistorico> detalles;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Sucursal sucursal;
    @ManyToOne
    private Vendedor vendedor;
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Cliente codeudor;
    private BigDecimal montoPrestamo = new BigDecimal(BigInteger.ZERO);
    private BigDecimal capital = new BigDecimal(BigInteger.ZERO);
    private int plazo = 12;
    @Column(precision = 38, scale = 8)
    private BigDecimal tasa;
    @Enumerated(EnumType.STRING)
    private PeriodoPago periodoPago;
    private BigDecimal gastos = new BigDecimal(BigInteger.ZERO);//Gastos de cobranza domiciliaria
    private BigDecimal comisiones = new BigDecimal(BigInteger.ZERO);//Comisiones por Asesoramiento Financiero
    private BigDecimal impuestoIVA = new BigDecimal(BigInteger.ZERO);
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicioOperacion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha = new Date();
    private BigDecimal montoCuota = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalIntereses = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalOperacion = new BigDecimal(BigInteger.ZERO);
    @Enumerated(EnumType.STRING)
    private SistemaAmortizacion sistemaAmortizacion;
    @Transient
    private Sistema sistema;
    private EstadoPrestamo estado;
    private boolean firmaPagare;
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Cotizacion cotizacion;
    private boolean firmaConyugeTitular = false;
    private boolean firmaConyugeCodeudor = false;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimoPago;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPrimerVencimiento;
    @ManyToOne
    private PlanGastos planGastos;

    public List<DetPrestamoHistorico> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetPrestamoHistorico> detalles) {
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCodeudor() {
        return codeudor;
    }

    public void setCodeudor(Cliente codeudor) {
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

    public BigDecimal getTasa() {
        return tasa;
    }

    public void setTasa(BigDecimal tasa) {
        this.tasa = tasa;
    }

    public PeriodoPago getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(PeriodoPago periodoPago) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public SistemaAmortizacion getSistemaAmortizacion() {
        return sistemaAmortizacion;
    }

    public void setSistemaAmortizacion(SistemaAmortizacion sistemaAmortizacion) {
        this.sistemaAmortizacion = sistemaAmortizacion;
    }

    public Sistema getSistema() {
        return sistema;
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public boolean isFirmaPagare() {
        return firmaPagare;
    }

    public void setFirmaPagare(boolean firmaPagare) {
        this.firmaPagare = firmaPagare;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
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

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public Date getFechaPrimerVencimiento() {
        return fechaPrimerVencimiento;
    }

    public void setFechaPrimerVencimiento(Date fechaPrimerVencimiento) {
        this.fechaPrimerVencimiento = fechaPrimerVencimiento;
    }

    public PlanGastos getPlanGastos() {
        return planGastos;
    }

    public void setPlanGastos(PlanGastos planGastos) {
        this.planGastos = planGastos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrestamoHistorico other = (PrestamoHistorico) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

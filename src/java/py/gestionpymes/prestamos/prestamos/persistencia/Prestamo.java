/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.PeriodoPago;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import py.gestionpymes.prestamos.adm.persistencia.Canal;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.adm.persistencia.Vendedor;

/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Prestamo.TODOS, query = "select p from Prestamo p"),
    @NamedQuery(name = Prestamo.POR_CLIENTE, query = "select p from Prestamo p where p.cliente = :cliente")})
public class Prestamo implements Serializable {

    public static final String TODOS = "py.gestionpymes.jpa.prestamos.Prestamo.TODOS";
    public static final String POR_CLIENTE = "py.gestionpymes.jpa.prestamos.Prestamo.POR_CLIENTE";
    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetPrestamo> detalles;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Sucursal sucursal;
    @ManyToOne
    private Canal canal;
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
    private BigDecimal impuestoIVAcomisiones = new BigDecimal(BigInteger.ZERO);//Comisiones por Asesoramiento Financiero
    private BigDecimal impuestoIVAgastos = new BigDecimal(BigInteger.ZERO);//Comisiones por Asesoramiento Financiero
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

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public PlanGastos getPlanGastos() {
        return planGastos;
    }

    public void setPlanGastos(PlanGastos planGastos) {
        this.planGastos = planGastos;
    }

    public Prestamo() {
        this.estado = EstadoPrestamo.PENDIENTE_DESEMBOLSO;
        this.sistemaAmortizacion = SistemaAmortizacion.FRANCES;
    }

    public BigDecimal devuelveSaldoPrestamo() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);
        for (DetPrestamo d : getDetalles()) {
            R = R.add(d.getSaldoCuota());
        }

        return R;
    }

    public Date getFechaPrimerVencimiento() {
        return fechaPrimerVencimiento;
    }

    public void setFechaPrimerVencimiento(Date fechaPrimerVencimiento) {
        this.fechaPrimerVencimiento = fechaPrimerVencimiento;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
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

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
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

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public Sistema getSistema() {
        if (sistema == null) {
            switch (sistemaAmortizacion) {
                case INTERES_SIMPLE:
                    sistema = new InteresSimple(this);
                    break;
                case FRANCES:
                    sistema = new InteresFrances(this);
                    break;
            }
        }
        return sistema;
    }

    public boolean isFirmaPagare() {
        return firmaPagare;
    }

    public void setFirmaPagare(boolean firmaPagare) {
        this.firmaPagare = firmaPagare;
    }

    public BigDecimal getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigDecimal montoPrestamo) {
        this.montoPrestamo = montoPrestamo.setScale(0, RoundingMode.HALF_EVEN);
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public BigDecimal getCapital() {
        capital = montoPrestamo.add(gastos).add(comisiones).setScale(0, RoundingMode.HALF_EVEN);
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(BigDecimal impuestoIVA) {
        this.impuestoIVA = impuestoIVA.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getImpuestoIVAcomisiones() {
        return impuestoIVAcomisiones;
    }

    public void setImpuestoIVAcomisiones(BigDecimal impuestoIVAcomisiones) {
        this.impuestoIVAcomisiones = impuestoIVAcomisiones.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getImpuestoIVAgastos() {
        return impuestoIVAgastos;
    }

    public void setImpuestoIVAgastos(BigDecimal impuestoIVAgastos) {
        this.impuestoIVAgastos = impuestoIVAgastos.setScale(0, RoundingMode.HALF_EVEN);
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

    public List<DetPrestamo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetPrestamo> detalles) {
        this.detalles = detalles;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaInicioOperacion() {
        return fechaInicioOperacion;
    }

    public void setFechaInicioOperacion(Date fechaInicioOperacion) {
        this.fechaInicioOperacion = fechaInicioOperacion;
        if (fechaInicioOperacion != null) {
            DateTime d = new DateTime(fechaInicioOperacion);
            
            this.fechaPrimerVencimiento = d.plusMonths(1).toDate();
        }

    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getComisiones() {
        return comisiones;
    }

    public void setComisiones(BigDecimal comisiones) {
        this.comisiones = comisiones.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getMontoCuota() {
        return montoCuota;//.add(impuestoIVA).divide(new BigDecimal(plazo))
    }

    public void setMontoCuota(BigDecimal montoCuota) {
        this.montoCuota = montoCuota.setScale(0, RoundingMode.HALF_EVEN);
    }

    public PeriodoPago getPeriodoPago() {
        return periodoPago;
    }

    public void setPeriodoPago(PeriodoPago periodoPago) {
        this.periodoPago = periodoPago;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public SistemaAmortizacion getSistemaAmortizacion() {
        return sistemaAmortizacion;
    }

    public void setSistemaAmortizacion(SistemaAmortizacion sistemaAmortizacion) {
        this.sistemaAmortizacion = sistemaAmortizacion;
    }

    public BigDecimal getTasa() {
        return tasa;
    }

    public void setTasa(BigDecimal tasa) {
        this.tasa = tasa;
    }

    public BigDecimal getTotalIntereses() {

        return totalIntereses;
    }

    public void setTotalIntereses(BigDecimal totalIntereses) {
        this.totalIntereses = totalIntereses.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getTotalOperacion() {
        return totalOperacion.setScale(0, RoundingMode.HALF_EVEN);
    }

    public void setTotalOperacion(BigDecimal totalOperacion) {
        this.totalOperacion = totalOperacion.setScale(0, RoundingMode.HALF_EVEN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void calcula() {
        detalles = getSistema().calculaCuotas();

        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;

        BigDecimal interesPorDia = new BigDecimal(getTasa().doubleValue() / 100d / 365d, MathContext.DECIMAL128);
        int difPrimerVencimiento = Days.daysBetween(new DateTime(fechaInicioOperacion).plusMonths(1), new DateTime(fechaPrimerVencimiento)).getDays();

        BigDecimal montoDif = interesPorDia.multiply(new BigDecimal(difPrimerVencimiento)).multiply(getCapital(), MathContext.DECIMAL128);

        for (DetPrestamo d : getDetalles()) {

            totalIntereses = totalIntereses.add(d.getCuotaInteres());
            impuestoIVA = impuestoIVA.add(d.getCuotaInteres().multiply(new BigDecimal(0.1), MathContext.DECIMAL128));
        }

        totalOperacion = getCapital().add(totalIntereses).add(impuestoIVA);

        System.out.println("TOTAL IVA: " + impuestoIVA.doubleValue());
        System.out.println("PLAZO: " + plazo);
        BigDecimal ivaMesFijo = impuestoIVA.divide(new BigDecimal(plazo), MathContext.DECIMAL128);

        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;

        for (DetPrestamo d : getDetalles()) {
            BigDecimal interesIvaIncluido = d.getCuotaInteres().add(ivaMesFijo);

            BigDecimal ivaInteres = interesIvaIncluido.divide(new BigDecimal(11), MathContext.DECIMAL128);
            d.setCuotaInteres(interesIvaIncluido.subtract(ivaInteres));
            d.setImpuestoIvaCuota(ivaInteres);

            BigDecimal nuevomontoCuota = d.getCuotaInteres().add(d.getCuotaCapital()).add(d.getImpuestoIvaCuota());

            d.setMontoCuota(nuevomontoCuota);
            d.setSaldoCuota(nuevomontoCuota);
            totalIntereses = totalIntereses.add(d.getCuotaInteres());

            impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota());
        }

        if (difPrimerVencimiento > 0) {
            totalIntereses = BigDecimal.ZERO;
            impuestoIVA = BigDecimal.ZERO;
            totalOperacion = BigDecimal.ZERO;

            for (DetPrestamo d : getDetalles()) {
                if (d.getNroCuota() == 1 && difPrimerVencimiento > 0) {
                    d.setCuotaInteres(d.getCuotaInteres().add(montoDif));
                    d.setImpuestoIvaCuota(d.getImpuestoIvaCuota().add(montoDif.multiply(new BigDecimal(0.1), MathContext.DECIMAL128)));

                    totalIntereses = totalIntereses.add(d.getCuotaInteres());

                    impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota());

                    BigDecimal nuevomontoCuota = d.getCuotaInteres().add(d.getCuotaCapital()).add(d.getImpuestoIvaCuota());
                    d.setMontoCuota(nuevomontoCuota);
                    d.setSaldoCuota(nuevomontoCuota);

                } else {
                    totalIntereses = totalIntereses.add(d.getCuotaInteres());
                    impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota());
                }

            }
        }

        totalOperacion = getCapital().add(totalIntereses).add(impuestoIVA);

        if (sistemaAmortizacion == SistemaAmortizacion.FRANCES) {
            montoCuota = getSistema().getCuota().add(ivaMesFijo);
        } else if (sistemaAmortizacion == SistemaAmortizacion.INTERES_SIMPLE) {
            montoCuota = getSistema().getCuota();
        }

        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;

        for (DetPrestamo d : getDetalles()) {
            totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN));
            impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN));
        }

        totalOperacion = getCapital().add(totalIntereses).add(impuestoIVA);

        montoCuota.setScale(0, RoundingMode.HALF_EVEN);
        totalIntereses.setScale(0, RoundingMode.HALF_EVEN);
        totalOperacion.setScale(0, RoundingMode.HALF_EVEN);
        impuestoIVA.setScale(0, RoundingMode.HALF_EVEN);//Total de impuestos de los itereses generados a devengar
        impuestoIVAcomisiones = getComisiones().divide(new BigDecimal(11), 0, RoundingMode.HALF_EVEN);
        impuestoIVAgastos = getGastos().divide(new BigDecimal(11), 0, RoundingMode.HALF_EVEN);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestamo)) {
            return false;
        }
        Prestamo other = (Prestamo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.prestamos.Prestamo[ id=" + id + " ]";
    }
}

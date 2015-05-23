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
import java.util.Collections;
import java.util.Comparator;
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
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Prestamo.TODOS, query = "select p from Prestamo p"),
    @NamedQuery(name = Prestamo.POR_CLIENTE, query = "select p from Prestamo p where p.cliente = :cliente")})
public class Prestamo implements Serializable , Auditable{

    public static final String TODOS = "py.gestionpymes.jpa.prestamos.Prestamo.TODOS";
    public static final String POR_CLIENTE = "py.gestionpymes.jpa.prestamos.Prestamo.POR_CLIENTE";
    
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
    private BigDecimal tasa = new BigDecimal(BigInteger.ZERO);
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

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<DetPrestamo> detalles;
    
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
        this.tasa = new BigDecimal(BigInteger.ZERO);
    }

    public BigDecimal devuelveSaldoPrestamo() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);
        for (DetPrestamo d : getDetalles()) {
            R = R.add(d.getSaldoCuota().setScale(0, RoundingMode.HALF_EVEN));
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
                case INTERES_AMERICANO:
                    sistema = new InteresAmericano(this);
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
        if (detalles != null) {
            Comparator<DetPrestamo> com = new Comparator<DetPrestamo>() {

                @Override
                public int compare(DetPrestamo o1, DetPrestamo o2) {
                    return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
                }
            };
            Collections.sort(detalles, com);
        }
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
        return montoCuota.setScale(0, RoundingMode.HALF_EVEN);//.add(impuestoIVA).divide(new BigDecimal(plazo))
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

        BigDecimal interesPorDia = new BigDecimal(getTasa().doubleValue() / 100d / 365d, MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);
        int difPrimerVencimiento = Days.daysBetween(new DateTime(fechaInicioOperacion).plusMonths(1), new DateTime(fechaPrimerVencimiento)).getDays();

        BigDecimal montoDif = interesPorDia.multiply(new BigDecimal(difPrimerVencimiento)).multiply(getCapital(), MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);

        for (DetPrestamo d : getDetalles()) {

            totalIntereses = totalIntereses.add(d.getCuotaInteres());
            impuestoIVA = impuestoIVA.add(d.getCuotaInteres().multiply(new BigDecimal(0.1), MathContext.DECIMAL128)).setScale(8, RoundingMode.HALF_EVEN);
        }

        totalOperacion = getCapital().add(totalIntereses).add(impuestoIVA).setScale(8, RoundingMode.HALF_EVEN);

        System.out.println("TOTAL IVA: " + impuestoIVA.doubleValue());
        System.out.println("PLAZO: " + plazo);
        BigDecimal ivaMesFijo = impuestoIVA.divide(new BigDecimal(plazo), MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);

        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;

        for (DetPrestamo d : getDetalles()) {
            BigDecimal interesIvaIncluido = d.getCuotaInteres().add(ivaMesFijo).setScale(8, RoundingMode.HALF_EVEN);

            BigDecimal ivaInteres = interesIvaIncluido.divide(new BigDecimal(11), MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);
            d.setCuotaInteres(interesIvaIncluido.subtract(ivaInteres).setScale(8, RoundingMode.HALF_EVEN));
            d.setImpuestoIvaCuota(ivaInteres.setScale(8, RoundingMode.HALF_EVEN));

            BigDecimal nuevomontoCuota = d.getCuotaInteres().add(d.getCuotaCapital()).add(d.getImpuestoIvaCuota()).setScale(8, RoundingMode.HALF_EVEN);

            d.setMontoCuota(nuevomontoCuota.setScale(8, RoundingMode.HALF_EVEN));
            d.setSaldoCuota(nuevomontoCuota.setScale(8, RoundingMode.HALF_EVEN));
            totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(8, RoundingMode.HALF_EVEN));

            impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(8, RoundingMode.HALF_EVEN));
        }

        if (difPrimerVencimiento > 0) {
            totalIntereses = BigDecimal.ZERO;
            impuestoIVA = BigDecimal.ZERO;
            totalOperacion = BigDecimal.ZERO;

            for (DetPrestamo d : getDetalles()) {
                if (d.getNroCuota() == 1 && difPrimerVencimiento > 0) {
                    d.setCuotaInteres(d.getCuotaInteres().add(montoDif));
                    d.setImpuestoIvaCuota(d.getImpuestoIvaCuota().add(montoDif.multiply(new BigDecimal(0.1), MathContext.DECIMAL128)).setScale(8, RoundingMode.HALF_EVEN));

                    totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(8, RoundingMode.HALF_EVEN));

                    impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(8, RoundingMode.HALF_EVEN));

                    BigDecimal nuevomontoCuota = d.getCuotaInteres().add(d.getCuotaCapital()).add(d.getImpuestoIvaCuota()).setScale(8, RoundingMode.HALF_EVEN);
                    d.setMontoCuota(nuevomontoCuota.setScale(8, RoundingMode.HALF_EVEN));
                    d.setSaldoCuota(nuevomontoCuota.setScale(8, RoundingMode.HALF_EVEN));

                } else {
                    totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(8, RoundingMode.HALF_EVEN));
                    impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(8, RoundingMode.HALF_EVEN));
                }

            }
        }

        totalOperacion = getCapital().add(totalIntereses).add(impuestoIVA).setScale(8, RoundingMode.HALF_EVEN);

        if (sistemaAmortizacion == SistemaAmortizacion.FRANCES) {
            montoCuota = getSistema().getCuota().add(ivaMesFijo).setScale(8, RoundingMode.HALF_EVEN);
        } else if (sistemaAmortizacion == SistemaAmortizacion.INTERES_SIMPLE) {
            montoCuota = getSistema().getCuota();
        }else if (sistemaAmortizacion == SistemaAmortizacion.INTERES_AMERICANO) {
            montoCuota = getSistema().getCuota().add(ivaMesFijo);
        }

        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;
        

        BigDecimal totalCuotas = new BigDecimal(BigInteger.ZERO);
        BigDecimal sumaCapital = new BigDecimal(BigInteger.ZERO);
        DetPrestamo ultimoDetalle = null;
        for (DetPrestamo d : getDetalles()) {
            totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN));
            impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN));
            
            
            //Ajuste cuotas en detalle
            BigDecimal sumaCuota = d.getCuotaCapital().setScale(0, RoundingMode.HALF_EVEN)
                    .add(d.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN))
                    .add(d.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN));
            
            if(sumaCuota.compareTo(d.getMontoCuota()) != 0){
                BigDecimal b2 = d.getMontoCuota().subtract(sumaCuota);
                BigDecimal temp = d.getCuotaCapital().add(b2);
                d.setCuotaCapital(temp); //Correccion
            }
            
            sumaCapital = sumaCapital.add(d.getCuotaCapital()).setScale(0, RoundingMode.HALF_EVEN);
            
            totalCuotas = totalCuotas.add(d.getMontoCuota().setScale(0, RoundingMode.HALF_EVEN));
            
            ultimoDetalle = d;
        }
        
        
        if(getCapital().compareTo(sumaCapital)!= 0){
            BigDecimal diffCapital = getCapital().subtract(sumaCapital).setScale(0, RoundingMode.HALF_EVEN);
            if(ultimoDetalle != null){
                ultimoDetalle.setCuotaCapital(ultimoDetalle.getCuotaCapital().add(diffCapital).setScale(0, RoundingMode.HALF_EVEN));
                ultimoDetalle.setCuotaInteres(ultimoDetalle.getCuotaInteres().subtract(diffCapital).setScale(0, RoundingMode.HALF_EVEN));
                
                BigDecimal sumaDetalle = ultimoDetalle.getCuotaCapital().setScale(0, RoundingMode.HALF_EVEN)
                        .add(ultimoDetalle.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN))
                        .add(ultimoDetalle.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN));
                
                if(ultimoDetalle.getMontoCuota().compareTo(sumaDetalle)!=0){
                    BigDecimal diffIva = ultimoDetalle.getMontoCuota().subtract(sumaDetalle.setScale(0, RoundingMode.HALF_EVEN)).setScale(0, RoundingMode.HALF_EVEN);
                    ultimoDetalle.setImpuestoIvaCuota(ultimoDetalle.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN).add(diffIva));
                }
            }
            
        }
        
        
        totalIntereses = BigDecimal.ZERO;
        impuestoIVA = BigDecimal.ZERO;
        totalOperacion = BigDecimal.ZERO;
        BigDecimal amortizacionAcumulada = new BigDecimal(BigInteger.ZERO);
        for (DetPrestamo d : getDetalles()) {
            d.setSaldoCapital(getCapital().subtract(amortizacionAcumulada));
            
            amortizacionAcumulada = amortizacionAcumulada.add(d.getCuotaCapital().setScale(0, RoundingMode.HALF_EVEN));
            
            totalIntereses = totalIntereses.add(d.getCuotaInteres().setScale(0, RoundingMode.HALF_EVEN));
            impuestoIVA = impuestoIVA.add(d.getImpuestoIvaCuota().setScale(0, RoundingMode.HALF_EVEN));
            montoCuota = d.getMontoCuota();
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

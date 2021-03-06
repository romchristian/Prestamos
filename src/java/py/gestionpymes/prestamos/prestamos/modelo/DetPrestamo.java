/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoDetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author christian
 */
@Entity
public class DetPrestamo implements Serializable, Auditable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long version;
    @ManyToOne
    private Prestamo prestamo;
    private int nroCuota;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    private BigDecimal saldoCapital = new BigDecimal(BigInteger.ZERO);
    private BigDecimal cuotaCapital = new BigDecimal(BigInteger.ZERO);// monto del prestamo / plazo
    private BigDecimal cuotaInteres = new BigDecimal(BigInteger.ZERO);// total interes / plazo
    private BigDecimal montoCuota = new BigDecimal(BigInteger.ZERO);

    private int diasMora;
    private BigDecimal montoMora = new BigDecimal(BigInteger.ZERO);
    private BigDecimal moraPunitorio = new BigDecimal(BigInteger.ZERO);
    private BigDecimal moraMoratorio = new BigDecimal(BigInteger.ZERO);
    private BigDecimal ivaMoraPunitorio = new BigDecimal(BigInteger.ZERO);
    private BigDecimal ivaMoraMoratorio = new BigDecimal(BigInteger.ZERO);

    private BigDecimal montoPago = new BigDecimal(BigInteger.ZERO);
    private boolean tieneDescuento;
    private BigDecimal descuento = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoCuota = new BigDecimal(BigInteger.ZERO);
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimoPago;
    @ManyToOne
    private Moneda moneda;
    //@Enumerated(EnumType.STRING)
    private EstadoDetPrestamo estado;
    private float interesMoratorio;//Es la misma tasa del prestamo
    private float interesPunitorio;//Interes punitorio es el 20% del interes moratorio
    private BigDecimal impuestoIvaCuota = new BigDecimal(BigInteger.ZERO);
    @OneToMany(mappedBy = "detPrestamo")
    private List<DescuentoCuota> descuentoCuotas;
    @OneToMany(mappedBy = "detPrestamo")
    private List<AplicacionPagoCuota> aplicacionesPago;
    @Transient
    private BigDecimal pendienteCargo;

    public DetPrestamo() {
        this.estado = EstadoDetPrestamo.PENDIENTE;
    }

    public List<AplicacionPagoCuota> getAplicacionesPago() {
        return aplicacionesPago;
    }

    public void setAplicacionesPago(List<AplicacionPagoCuota> aplicacionesPago) {
        this.aplicacionesPago = aplicacionesPago;
    }

    public List<DescuentoCuota> getDescuentoCuotas() {
        return descuentoCuotas;
    }

    public BigDecimal getPendienteCargo() {
        return pendienteCargo;
    }

    public void setPendienteCargo(BigDecimal pendienteCargo) {
        this.pendienteCargo = pendienteCargo;
    }

    public BigDecimal obtDescuentoAcumulado(TipoDescuento tipo) {
        BigDecimal R = BigDecimal.ZERO;
        if (getDescuentoCuotas() != null) {
            for (DescuentoCuota d : getDescuentoCuotas()) {
                if (d.getTipo() == tipo) {
                    R = R.add(d.getMonto());
                }
            }
        }

        return R;
    }

    public BigDecimal obtDescuentoAcumulado(String tipo) {
        TipoDescuento t;
        switch (tipo) {
            case "MORA":
                t = TipoDescuento.MORA;
                break;
            case "INTERES":
                t = TipoDescuento.INTERES;
                break;
            case "CARGOS":
                t = TipoDescuento.CARGOS;
                break;
            default:
                t = TipoDescuento.MORA;
        }
        return obtDescuentoAcumulado(t);
    }

    public BigDecimal getDescuentoAcumuladoTotal() {
        BigDecimal R = BigDecimal.ZERO;
        if (getDescuentoCuotas() != null) {
            for (DescuentoCuota d : getDescuentoCuotas()) {
                R = R.add(d.getMonto());
            }
        }

        return R;
    }

    public BigDecimal getDescuentoAcumuladoTotalNoAplicado() {
        BigDecimal R = BigDecimal.ZERO;
        if (getDescuentoCuotas() != null) {
            for (DescuentoCuota d : getDescuentoCuotas()) {
                if (!d.isAplicado()) {
                    R = R.add(d.getMonto());
                }
            }
        }

        return R;
    }

    public void setDescuentoCuotas(List<DescuentoCuota> descuentoCuotas) {
        this.descuentoCuotas = descuentoCuotas;
    }

    public DetPrestamo(Prestamo prestamo, int nroCuota, BigDecimal cuotaCapital, BigDecimal cuotaInteres, BigDecimal saldoCapital) {
        this();
        this.prestamo = prestamo;
        this.moneda = prestamo.getMoneda();
        this.nroCuota = nroCuota;
        this.cuotaCapital = cuotaCapital;
        this.cuotaInteres = cuotaInteres;
        this.saldoCapital = saldoCapital;
        this.impuestoIvaCuota.add(cuotaInteres).multiply(new BigDecimal(0.1), MathContext.DECIMAL128);
        this.montoCuota = cuotaCapital.add(cuotaInteres);

        GregorianCalendar gc = new GregorianCalendar(new Locale("es", "py"));
        gc.setTime(prestamo.getFechaPrimerVencimiento());

        System.out.println("PRIMER VENCIMIENTO: " + prestamo.getFechaPrimerVencimiento());
        int dias = 0;

        switch (prestamo.getPeriodoPago()) {
            case MENSUAL:
                gc.add(Calendar.MONTH, nroCuota - 1);
                this.fechaVencimiento = gc.getTime();
                break;
            case QUINCENAL:
                dias = 15;
                dias *= (nroCuota - 1);
                gc.add(Calendar.DAY_OF_MONTH, dias);
                this.fechaVencimiento = gc.getTime();
                break;
            case SEMANAL:
                dias = 7;
                dias *= (nroCuota - 1);
                gc.add(Calendar.DAY_OF_MONTH, dias);
                this.fechaVencimiento = gc.getTime();
                break;
            case DIARIO:

                System.out.println("GET TIME: " + gc.getTime());
                if (nroCuota == 1) {
                    dias = 0;
                } else {
                    dias = 1;
                }

                //dias *= (nroCuota -1);
                gc.add(Calendar.DAY_OF_MONTH, dias);

                LocalDate fecha = new LocalDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, gc.get(Calendar.DAY_OF_MONTH));

                if (fecha.getDayOfWeek() == DateTimeConstants.SATURDAY) {
                    fecha = fecha.plusDays(2);
                    this.prestamo.setFechaPrimerVencimiento(fecha.toDate());
                    this.fechaVencimiento = fecha.toDate();
                } else {
                    this.fechaVencimiento = gc.getTime();
                    this.prestamo.setFechaPrimerVencimiento(gc.getTime());
                }

                break;
        }

        interesMoratorio = prestamo.getTasa().floatValue();
        interesPunitorio = prestamo.getTasa().floatValue() * 0.2f;
    }

    public boolean isTieneDescuento() {
        return tieneDescuento;
    }

    public void setTieneDescuento(boolean tieneDescuento) {
        this.tieneDescuento = tieneDescuento;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuesto) {
        this.descuento = descuesto;
    }

    public BigDecimal getIvaMoraPunitorio() {
        return ivaMoraPunitorio;
    }

    public void setIvaMoraPunitorio(BigDecimal ivaMoraPunitorio) {
        this.ivaMoraPunitorio = ivaMoraPunitorio;
    }

    public BigDecimal getIvaMoraMoratorio() {
        return ivaMoraMoratorio;
    }

    public void setIvaMoraMoratorio(BigDecimal ivaMoraMoratorio) {
        this.ivaMoraMoratorio = ivaMoraMoratorio;
    }

    public BigDecimal getMoraPunitorio() {
        return moraPunitorio;
    }

    public void setMoraPunitorio(BigDecimal moraPunitorio) {
        this.moraPunitorio = moraPunitorio;
    }

    public BigDecimal getMoraMoratorio() {
        return moraMoratorio;
    }

    public void setMoraMoratorio(BigDecimal moraMoratorio) {
        this.moraMoratorio = moraMoratorio;
    }

    public BigDecimal getImpuestoIvaCuota() {
        return impuestoIvaCuota;
    }

    public void setImpuestoIvaCuota(BigDecimal impuestoIvaCuota) {
        this.impuestoIvaCuota = impuestoIvaCuota;
    }

    public float getInteresMoratorio() {
        return interesMoratorio;
    }

    public void setInteresMoratorio(float interesMoratorio) {
        this.interesMoratorio = interesMoratorio;
    }

    public float getInteresPunitorio() {
        return interesPunitorio;
    }

    public void setInteresPunitorio(float interesPunitorio) {
        this.interesPunitorio = interesPunitorio;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public EstadoDetPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoDetPrestamo estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiasMora() {

        if (estado == EstadoDetPrestamo.PENDIENTE) {
            diasMora = Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(new Date())).getDays();
        }
        return diasMora;
    }

    public void setDiasMora(int diasMora) {
        this.diasMora = diasMora;
    }

    public Date getFechaVencimiento() {
        //TODO Calcular fecha vencimiento

        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    public BigDecimal obtCuotaInteresConIva() {
        BigDecimal capital = cuotaCapital != null ? cuotaCapital : BigDecimal.ZERO;
        return montoCuota != null ? montoCuota.subtract(capital) : BigDecimal.ZERO;
    }

    public void setCuotaInteres(BigDecimal cuotaInteres) {
        this.cuotaInteres = cuotaInteres;
    }

    public BigDecimal getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(BigDecimal montoCuota) {
        this.montoCuota = montoCuota;
    }

    public BigDecimal getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(BigDecimal saldoCapital) {
        this.saldoCapital = saldoCapital;
    }

    public BigDecimal getMontoMora() {
        return montoMora;
    }

    public void setMontoMora(BigDecimal montoMora) {

        this.montoMora.add(montoMora);
    }

    public void calculaCuotaIvaIncluido() {
        BigDecimal ivaMesFijo = prestamo.getImpuestoIVA().divide(new BigDecimal(prestamo.getPlazo())).setScale(8, RoundingMode.HALF_DOWN);
        montoCuota = montoCuota.add(ivaMesFijo).setScale(0, RoundingMode.HALF_DOWN);
        saldoCuota = montoCuota;
    }

    public BigDecimal devuelveMontoMora() {
        if (saldoCuota.compareTo(new BigDecimal(0)) == 0) {
            return montoMora.setScale(0, RoundingMode.HALF_DOWN);
        } else {
            BigDecimal moratorio = calculaSaldoMoratorio();

            BigDecimal punitorio = calculaSaldoPunitorio();

            BigDecimal R = moratorio.add(punitorio);

            return R;
        }

    }

    public BigDecimal calculaSaldoMoratorio() {
        return calculaMontoPorDiasMoratorio().subtract(moraMoratorio);
    }

    public BigDecimal calculaSaldoPunitorio() {
        return calculaMontoPorDiasPunitorio().subtract(moraPunitorio);
    }

    public BigDecimal calculaMontoPorDiasMoratorio() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);

        if (getDiasMora() > 0) {

            double interesDiario = getInteresMoratorio() / 100d / 365d;
            double interesMora = interesDiario * getDiasMora();

            R = new BigDecimal(interesMora).multiply(cuotaCapital).setScale(8, RoundingMode.HALF_DOWN);
            setIvaMoraMoratorio(R.multiply(new BigDecimal(0.1)).setScale(8, RoundingMode.HALF_EVEN));
            R = R.add(getIvaMoraMoratorio());
        }
        return R.setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculaMontoPorDiasPunitorio() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);
        if (getDiasMora() > 0) {
            double interesDiario = getInteresPunitorio() / 100d / 365d;
            double interesMora = interesDiario * getDiasMora();
            R = new BigDecimal(interesMora).multiply(cuotaCapital).setScale(8, RoundingMode.HALF_DOWN);
            setIvaMoraPunitorio(R.multiply(new BigDecimal(0.1)).setScale(8, RoundingMode.HALF_EVEN));
            R = R.add(getIvaMoraPunitorio());
        }
        return R.setScale(0, RoundingMode.HALF_EVEN);
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
        if (estado == EstadoDetPrestamo.PENDIENTE) {
//            if (tieneDescuento) {
            BigDecimal pendienteMora = calculaMontoPorDiasMoratorio();
            pendienteMora = pendienteMora.add(calculaMontoPorDiasPunitorio());
            pendienteMora = pendienteMora.subtract(obtDescuentoAcumulado(TipoDescuento.MORA));

            saldoCuota = getMontoCuota();

            saldoCuota = saldoCuota.add(pendienteMora);
            if (pendienteCargo != null) {
                saldoCuota = saldoCuota.add(pendienteCargo);
            }
            saldoCuota = saldoCuota.subtract(obtDescuentoAcumulado(TipoDescuento.INTERES));
            saldoCuota = saldoCuota.subtract(obtDescuentoAcumulado(TipoDescuento.CARGOS));

            saldoCuota = saldoCuota.subtract(getMontoPago());
            //calculaMontoPorDiasMoratorio().add(calculaMontoPorDiasPunitorio()).add();
            //saldoCuota = saldoCuota.subtract(descuento);
//            } else {
//                saldoCuota = calculaMontoPorDiasMoratorio().add(calculaMontoPorDiasPunitorio()).add(getMontoCuota()).subtract(getMontoPago()).setScale(8, RoundingMode.HALF_EVEN);
//            }

        }
        return saldoCuota;
    }

    public void setSaldoCuota(BigDecimal saldoCuota) {
        this.saldoCuota = saldoCuota;
    }

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public boolean afectaSaldoCuota(BigDecimal monto) {
        boolean R = false;
        saldoCuota = getSaldoCuota().setScale(0, RoundingMode.HALF_EVEN);
        monto = monto.setScale(0, RoundingMode.HALF_EVEN);

        System.out.println("MONTO DETALLE FACTURA: " + monto);
        System.out.println("SALDO CUOTA: " + saldoCuota);
        System.out.println("COMPARACION (MONTO EXCEDE AL SALDO): " + (saldoCuota.compareTo(monto) >= 0));

        if (saldoCuota.compareTo(monto) >= 0) {

            montoPago = montoPago.add(monto);
            saldoCuota = saldoCuota.subtract(monto);

            ultimoPago = new Date();

            R = true;

            if (saldoCuota.compareTo(new BigDecimal(0)) == 0) {

                estado = EstadoDetPrestamo.CANCELADO;
                setDiasMora(Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(new Date())).getDays());

                if (prestamo.devuelveSaldoPrestamo().compareTo(new BigDecimal(BigInteger.ZERO)) == 0) {
                    // CANCELO EL PRESTAMO

                    prestamo.setEstado(EstadoPrestamo.CANCELADO);
                }
            }

        }
        return R;
    }

    public boolean afectaMora(BigDecimal monto, String refMonto) {
        boolean R = false;

        if (refMonto.compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0) {
            moraMoratorio = moraMoratorio.add(monto);
            R = true;
        } else if (refMonto.compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0) {
            moraPunitorio = moraPunitorio.add(monto);
            R = true;
        }

        ultimoPago = new Date();

        return R;
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
        if (!(object instanceof DetPrestamo)) {
            return false;
        }
        DetPrestamo other = (DetPrestamo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.prestamos.DetPrestamo[ id=" + id + " ]";
    }
}

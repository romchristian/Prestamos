/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoDetPrestamo;

/**
 *
 * @author christian
 */
@Entity
public class DetPrestamo implements Serializable {

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
    private BigDecimal montoPago = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoCuota = new BigDecimal(BigInteger.ZERO);
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimoPago;
    @ManyToOne
    private Moneda moneda;
    private EstadoDetPrestamo estado;

    public DetPrestamo() {
        this.estado = EstadoDetPrestamo.PENDIENTE;
    }

    public DetPrestamo(Prestamo prestamo, int nroCuota, BigDecimal cuotaCapital, BigDecimal cuotaInteres, BigDecimal saldoCapital) {
        this();
        this.prestamo = prestamo;
        this.moneda = prestamo.getMoneda();
        this.nroCuota = nroCuota;
        this.cuotaCapital = cuotaCapital;
        this.cuotaInteres = cuotaInteres;
        this.montoCuota = cuotaCapital.add(cuotaInteres);
        this.saldoCapital = saldoCapital;
        this.setSaldoCuota(montoCuota);

        GregorianCalendar gc = new GregorianCalendar(new Locale("es", "py"));
        gc.setTime(prestamo.getFechaInicioOperacion());
        int dias = 0;

        switch (prestamo.getPeriodoPago()) {
            case MENSUAL:
                dias = 30;
                break;
            case QUINCENAL:
                dias = 15;
                break;
            case SEMANAL:
                dias = 7;
                break;
        }

        dias *= nroCuota;
        gc.add(Calendar.DAY_OF_YEAR, dias);
        this.fechaVencimiento = gc.getTime();

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

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public boolean afectaSaldoCuota(BigDecimal monto) {
        boolean R = false;
        if ((saldoCuota.add(montoMora).compareTo(monto)) >= 0) {

            montoPago = montoPago.add(monto);
            saldoCuota = saldoCuota.subtract(monto);
            R = true;
            ultimoPago = new Date();

            if (saldoCuota.compareTo(new BigDecimal(0)) == 0) {
                estado = EstadoDetPrestamo.CANCELADO;
                setDiasMora(Days.daysBetween(new DateTime(fechaVencimiento), new DateTime(new Date())).getDays());
            }

        }
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

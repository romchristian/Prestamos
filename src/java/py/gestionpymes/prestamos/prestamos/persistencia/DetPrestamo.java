/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;

/**
 *
 * @author christian
 */
@Entity
public class DetPrestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long version;
    @ManyToOne
    private Prestamo prestamo;
    private int nroCuota;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    private double saldoCapital;
    private double cuotaCapital;// monto del prestamo / plazo
    private double cuotaInteres;// total interes / plazo
    private double montoCuota;
    private int diasMora;
    private double montoMora;
    private double montoPago;
    private double saldoCuota;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimoPago;
    @ManyToOne
    private Moneda moneda;

    public DetPrestamo() {
    }

    public DetPrestamo(Prestamo prestamo, int nroCuota, double cuotaCapital, double cuotaInteres, double saldoCapital) {
        this.prestamo = prestamo;
        this.moneda = prestamo.getMoneda();
        this.nroCuota = nroCuota;
        this.cuotaCapital = cuotaCapital;
        this.cuotaInteres = cuotaInteres;
        this.montoCuota = cuotaCapital + cuotaInteres;
        this.saldoCapital = saldoCapital;
        this.setSaldoCuota(montoCuota);

        GregorianCalendar gc = new GregorianCalendar(new Locale("es", "py"));
        gc.setTime(prestamo.getFechaInicioOperacion());
        int dias = 0;

        switch (prestamo.getPeriodoPago()) {
            case MENSUAL:
                dias = 30;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiasMora() {
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

    public double getCuotaCapital() {
        return cuotaCapital;
    }

    public void setCuotaCapital(double cuotaCapital) {
        this.cuotaCapital = cuotaCapital;
    }

    public double getCuotaInteres() {
        return cuotaInteres;
    }

    public void setCuotaInteres(double cuotaInteres) {
        this.cuotaInteres = cuotaInteres;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public double getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(double saldoCapital) {
        this.saldoCapital = saldoCapital;
    }

    public double getMontoMora() {
        return montoMora;
    }

    public void setMontoMora(double montoMora) {
        this.montoMora = montoMora;
    }

    public double getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(double montoPago) {
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

    public double getSaldoCuota() {
        return saldoCuota;
    }

    public void setSaldoCuota(double saldoCuota) {
        this.saldoCuota = saldoCuota;
    }

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public boolean afectaSaldoCuota(double monto) {
        boolean R = false;
        if ((monto + montoMora) <= saldoCuota) {
            saldoCuota -= monto;
            R = true;
            ultimoPago = new Date();
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

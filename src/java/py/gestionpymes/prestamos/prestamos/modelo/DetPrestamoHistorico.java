/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoDetPrestamo;

/**
 *
 * @author christian
 */
@Entity
public class DetPrestamoHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long version;
    @ManyToOne
    private PrestamoHistorico prestamo;
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
    private BigDecimal saldoCuota = new BigDecimal(BigInteger.ZERO);
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultimoPago;
    @ManyToOne
    private Moneda moneda;
    private EstadoDetPrestamo estado;
    private float interesMoratorio;//Es la misma tasa del prestamo
    private float interesPunitorio;//Interes punitorio es el 20% del interes moratorio
    private BigDecimal impuestoIvaCuota = new BigDecimal(BigInteger.ZERO);
    private boolean pagado;

    public DetPrestamoHistorico() {
        this.estado = EstadoDetPrestamo.PENDIENTE;
    }

    public DetPrestamoHistorico(PrestamoHistorico prestamo, int nroCuota, BigDecimal montoCuota) {
        this();
        this.prestamo = prestamo;
        this.moneda = prestamo.getMoneda();
        this.nroCuota = nroCuota;
        this.montoCuota = montoCuota;

        GregorianCalendar gc = new GregorianCalendar(new Locale("es", "py"));
        gc.setTime(prestamo.getFechaPrimerVencimiento());
        int dias = 0;

        switch (prestamo.getPeriodoPago()) {
            case MENSUAL:
                gc.add(Calendar.MONTH, nroCuota - 1);
                break;
            case QUINCENAL:
                dias = 15;
                dias *= (nroCuota - 1);
                gc.add(Calendar.DAY_OF_MONTH, dias);
                break;
            case SEMANAL:
                dias = 7;
                dias *= (nroCuota - 1);
                gc.add(Calendar.DAY_OF_MONTH, dias);
                break;
            case DIARIO:
                dias = 1;
                dias *= (nroCuota - 1);
                gc.add(Calendar.DAY_OF_MONTH, dias);
                break;
        }

        this.fechaVencimiento = gc.getTime();
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public PrestamoHistorico getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(PrestamoHistorico prestamo) {
        this.prestamo = prestamo;
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

    public int getDiasMora() {
        return diasMora;
    }

    public void setDiasMora(int diasMora) {
        this.diasMora = diasMora;
    }

    public BigDecimal getMontoMora() {
        return montoMora;
    }

    public void setMontoMora(BigDecimal montoMora) {
        this.montoMora = montoMora;
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

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
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

    public BigDecimal getImpuestoIvaCuota() {
        return impuestoIvaCuota;
    }

    public void setImpuestoIvaCuota(BigDecimal impuestoIvaCuota) {
        this.impuestoIvaCuota = impuestoIvaCuota;
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
        if (!(object instanceof DetPrestamoHistorico)) {
            return false;
        }
        DetPrestamoHistorico other = (DetPrestamoHistorico) object;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle;
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
    private int interesMoratorio;//Es la misma tasa del prestamo
    private float interesPunitorio;//Interes punitorio es el 20% del interes moratorio
    private BigDecimal impuestoIvaCuota = new BigDecimal(BigInteger.ZERO);

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
        this.saldoCapital = saldoCapital;
        this.impuestoIvaCuota.add(cuotaInteres).multiply(new BigDecimal(0.1));
        this.montoCuota = cuotaCapital.add(cuotaInteres);

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
        interesMoratorio = prestamo.getTasa();
        interesPunitorio = prestamo.getTasa() * 0.2f;
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
        impuestoIvaCuota = cuotaInteres.multiply(new BigDecimal(0.1));
        return impuestoIvaCuota;
    }

    public void setImpuestoIvaCuota(BigDecimal impuestoIvaCuota) {
        this.impuestoIvaCuota = impuestoIvaCuota;
    }

    public int getInteresMoratorio() {
        return interesMoratorio;
    }

    public void setInteresMoratorio(int interesMoratorio) {
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
        BigDecimal ivaMesFijo = prestamo.getImpuestoIVA().divide(new BigDecimal(prestamo.getPlazo()));
        montoCuota = montoCuota.add(ivaMesFijo).setScale(0, RoundingMode.HALF_EVEN);
        saldoCuota = montoCuota;
    }

    public BigDecimal devuelveMontoMora() {
        if (saldoCuota.compareTo(new BigDecimal(0)) == 0) {
            return montoMora.setScale(0, RoundingMode.HALF_EVEN);
        } else {
            BigDecimal moratorio = calculaSaldoMoratorio();
            
            BigDecimal punitorio = calculaSaldoPunitorio();
            
            BigDecimal R = moratorio.add(punitorio);
            
            return R;
        }

    }

    public BigDecimal calculaSaldoMoratorio(){
        return calculaMontoPorDiasMoratorio().subtract(moraMoratorio);
    }
    
    public BigDecimal calculaSaldoPunitorio(){
        return calculaMontoPorDiasPunitorio().subtract(moraPunitorio);
    }
    
    public BigDecimal calculaMontoPorDiasMoratorio() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);
        
        if (getDiasMora() > 0) {
        
            double interesDiario = getInteresMoratorio() / 100d / 12d / 30d;
            double interesMora = interesDiario * getDiasMora();

            R = new BigDecimal(interesMora).multiply(saldoCapital).setScale(0, RoundingMode.HALF_EVEN);
            setIvaMoraMoratorio(R.multiply(new BigDecimal(0.1)).setScale(0, RoundingMode.HALF_EVEN));
            R = R.add(getIvaMoraMoratorio());
        }
        return R;
    }

    public BigDecimal calculaMontoPorDiasPunitorio() {
        BigDecimal R = new BigDecimal(BigInteger.ZERO);
        if (getDiasMora() > 0) {
            double interesDiario = getInteresPunitorio() / 100d / 12d / 30d;
            double interesMora = interesDiario * getDiasMora();
            R = new BigDecimal(interesMora).multiply(saldoCapital).setScale(0, RoundingMode.HALF_EVEN);
            setIvaMoraPunitorio(R.multiply(new BigDecimal(0.1)).setScale(0, RoundingMode.HALF_EVEN));
            R = R.add(getIvaMoraPunitorio());
        }
        return R;
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
            saldoCuota = calculaMontoPorDiasMoratorio().add(calculaMontoPorDiasPunitorio()).add(getMontoCuota()).subtract(getMontoPago()).setScale(0, RoundingMode.HALF_EVEN);
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

    public boolean afectaSaldoCuota(BigDecimal monto, String refMonto) {
        boolean R = false;
        saldoCuota.setScale(0, RoundingMode.HALF_EVEN);
        monto.setScale(0, RoundingMode.HALF_EVEN);
        BigDecimal mora = devuelveMontoMora().setScale(0, RoundingMode.HALF_EVEN);
   
        if ((saldoCuota.add(mora).compareTo(monto)) >= 0) {

            montoPago = montoPago.add(monto);
            if(refMonto.compareToIgnoreCase(FacturaVentaDetalle.MONTO_CUOTA) == 0){
                saldoCuota = saldoCuota.subtract(monto);
            }else if(refMonto.compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0){
                moraMoratorio = moraMoratorio.add(monto);
                saldoCuota = saldoCuota.subtract(monto);
            }else if(refMonto.compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0){
                moraPunitorio = moraPunitorio.add(monto);
                saldoCuota = saldoCuota.subtract(monto);
            }
            
            ultimoPago = new Date();
            
            R = true;
            
            System.out.println("SALDO CUOTAAAAAA: "  + saldoCuota);
            
            if (saldoCuota.compareTo(new BigDecimal(0)) == 0) {
                System.out.println("CANCELOOOOOOOOOOOOOO");
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

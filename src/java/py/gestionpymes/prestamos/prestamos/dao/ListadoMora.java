/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.math.BigDecimal;
import org.joda.time.DateTime;


/**
 *
 * @author Acer
 */
class ListadoMora {
    
    private String nrodocumento;
    private String primernombre;
    private String segundonombre;
    private String primerapellido;
    private String segundoapellido;    
    private Long operacionnro;
    private Integer nrocuota;
    private DateTime fechainiciooperacion;
    private DateTime fechavencimiento;
    private Integer diasmora;
    private BigDecimal montomora;
    private BigDecimal montocuota;
    private BigDecimal moracuota;
    private BigDecimal cuotaconmora;

    public ListadoMora(String nrodocumento, String primernombre, String segundonombre, String primerapellido, String segundoapellido, Long operacionnro, Integer nrocuota, DateTime fechainiciooperacion, DateTime fechavencimiento, Integer diasmora, BigDecimal montomora, BigDecimal montocuota, BigDecimal moracuota, BigDecimal cuotaconmora) {
        this.nrodocumento = nrodocumento;
        this.primernombre = primernombre;
        this.segundonombre = segundonombre;
        this.primerapellido = primerapellido;
        this.segundoapellido = segundoapellido;
        this.operacionnro = operacionnro;
        this.nrocuota = nrocuota;
        this.fechainiciooperacion = fechainiciooperacion;
        this.fechavencimiento = fechavencimiento;
        this.diasmora = diasmora;
        this.montomora = montomora;
        this.montocuota = montocuota;
        this.moracuota = moracuota;
        this.cuotaconmora = cuotaconmora;
    }
    
    public ListadoMora(Object[] o) {
        this((String)o[0],(String)o[1],(String)o[2],(String)o[3],(String)o[4],(Long)o[5],(Integer)o[6],(DateTime)o[7],(DateTime)o[8],(Integer)o[9],(BigDecimal)o[10],(BigDecimal)o[11],(BigDecimal)o[12],(BigDecimal)o[13]);
    }

    public String getNrodocumento() {
        return nrodocumento;
    }

    public void setNrodocumento(String nrodocumento) {
        this.nrodocumento = nrodocumento;
    }

    public String getPrimernombre() {
        return primernombre;
    }

    public void setPrimernombre(String primernombre) {
        this.primernombre = primernombre;
    }

    public String getSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(String segundonombre) {
        this.segundonombre = segundonombre;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public Long getOperacionnro() {
        return operacionnro;
    }

    public void setOperacionnro(Long operacionnro) {
        this.operacionnro = operacionnro;
    }

    public Integer getNrocuota() {
        return nrocuota;
    }

    public void setNrocuota(Integer nrocuota) {
        this.nrocuota = nrocuota;
    }

    public DateTime getFechainiciooperacion() {
        return fechainiciooperacion;
    }

    public void setFechainiciooperacion(DateTime fechainiciooperacion) {
        this.fechainiciooperacion = fechainiciooperacion;
    }

    public DateTime getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(DateTime fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public Integer getDiasmora() {
        return diasmora;
    }

    public void setDiasmora(Integer diasmora) {
        this.diasmora = diasmora;
    }

    public BigDecimal getMontomora() {
        return montomora;
    }

    public void setMontomora(BigDecimal montomora) {
        this.montomora = montomora;
    }

    public BigDecimal getMontocuota() {
        return montocuota;
    }

    public void setMontocuota(BigDecimal montocuota) {
        this.montocuota = montocuota;
    }

    public BigDecimal getMoracuota() {
        return moracuota;
    }

    public void setMoracuota(BigDecimal moracuota) {
        this.moracuota = moracuota;
    }

    public BigDecimal getCuotaconmora() {
        return cuotaconmora;
    }

    public void setCuotaconmora(BigDecimal cuotaconmora) {
        this.cuotaconmora = cuotaconmora;
    }
    
    
    
}

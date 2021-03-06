/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.modelo.Estado;
import py.gestionpymes.prestamos.finanza.modelo.CuentaBancaria;

/**
 *
 * @author Acer
 */
@Entity
public class Secuencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String timbrado;
    private String establecimiento;
    private String puntoExpedicion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimiento;
    private Long valorInicial;
    private Long valorFinal;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Enumerated(EnumType.STRING)
    private TipoSecuencia tipoSecuencia;
    private Long ultimoNumero;
    @ManyToOne
    private CuentaBancaria cuentaBancaria;
    private String serie;

    public Secuencia() {
        this.estado = Estado.ACTIVO;
        this.valorFinal = valorFinal == null ? 0L : valorFinal;
        ultimoNumero = 0L;
    }
    

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    
    public String getNumeroFormateado(Long num) {
      
        String valor = num+"";
        int tamaño = valor.length();
        if (tamaño == 1) {
            valor = "000000" + valor;
        } else if (tamaño == 2) {
            valor = "00000" + valor;
        } else if (tamaño == 3) {
            valor = "0000" + valor;
        } else if (tamaño == 4) {
            valor = "000" + valor;
        } else if (tamaño == 5) {
            valor = "00" + valor;
        } else if (tamaño == 6) {
            valor = "0" + valor;
        }
        return valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(Long valorInicial) {
        this.valorInicial = valorInicial;
    }

    public Long getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Long valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(String puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Long obtSiguienteNumero() {
        if (valorFinal > 0 && valorFinal < (ultimoNumero + 1)) {
            return null;
        }
        if(ultimoNumero == 0){
            return valorInicial;
            
        } else {
            return  ultimoNumero +1;
        }
    }

    public Estado getEstado() {
        if (vencimiento != null && (vencimiento.before(new Date()))) {
            estado = Estado.INACTIVO;
        } else if (valorFinal > 0 && obtSiguienteNumero() == null) {
            estado = Estado.INACTIVO;
        }
        return estado;
    }

    public void setEstado(Estado estado) {
        if (vencimiento != null && (vencimiento.before(new Date()))) {
            this.estado = Estado.INACTIVO;
        } else if (valorFinal > 0 && obtSiguienteNumero() == null) {
            this.estado = Estado.INACTIVO;
        } else {
            this.estado = estado;
        }
    }

    public TipoSecuencia getTipoSecuencia() {
        return tipoSecuencia;
    }

    public void setTipoSecuencia(TipoSecuencia tipoSecuencia) {
        this.tipoSecuencia = tipoSecuencia;
    }

    public Long getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Long ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
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
        if (!(object instanceof Secuencia)) {
            return false;
        }
        Secuencia other = (Secuencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

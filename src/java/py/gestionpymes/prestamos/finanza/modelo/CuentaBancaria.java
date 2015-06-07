/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;
import py.gestionpymes.prestamos.adm.modelo.Banco;

/**
 *
 * @author Acer
 */
@Entity

public class CuentaBancaria implements Serializable , Auditable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creacion;
    
    @ManyToOne
    private Banco banco;
    private String numero;
    private String denominacion;
    
    @ManyToOne
    private Moneda moneda;
    private BigDecimal saldoTeorico;
    private BigDecimal saldoReal;
    
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private TipoCuentaBancaria tipo;
    private boolean borrado;

    public TipoCuentaBancaria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuentaBancaria tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldoTeorico() {
        return saldoTeorico;
    }

    public void setSaldoTeorico(BigDecimal saldoTeorico) {
        this.saldoTeorico = saldoTeorico;
    }

    public BigDecimal getSaldoReal() {
        return saldoReal;
    }

    public void setSaldoReal(BigDecimal saldoReal) {
        this.saldoReal = saldoReal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
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
        if (!(object instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return denominacion+": #"+numero;
    }

    
}

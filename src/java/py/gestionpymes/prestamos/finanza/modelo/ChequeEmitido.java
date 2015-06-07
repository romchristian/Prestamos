/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;

/**
 *
 * @author Acer
 */
@Entity
public class ChequeEmitido implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CuentaBancaria cuentaBancaria;
    @ManyToOne
    private Secuencia secuencia;
    private String numero;
    private String beneficiario;
    @Enumerated(EnumType.STRING)
    private TipoCheque tipoCheque;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date emision;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimiento;
    @ManyToOne
    private Moneda moneda;
    private BigDecimal monto;
    @ManyToOne
    private EstadoCheque estado;//BORRADOR,AUTORIZADO,ENTREGADO,COBRADO,ANULADO

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Secuencia getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Secuencia secuencia) {
        this.secuencia = secuencia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }
    
    
    
    public ChequeEmitido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoCheque getTipoCheque() {
        return tipoCheque;
    }

    public void setTipoCheque(TipoCheque tipoCheque) {
        this.tipoCheque = tipoCheque;
    }

    public Date getEmision() {
        return emision;
    }

    public void setEmision(Date emision) {
        this.emision = emision;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public EstadoCheque getEstado() {
        return estado;
    }

    public void setEstado(EstadoCheque estado) {
        this.estado = estado;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChequeEmitido other = (ChequeEmitido) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;

/**
 *
 * @author Acer
 */
@Entity
public class TransaccionBancaria implements Serializable, Auditable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codRef;
    private String descripcion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @ManyToOne
    private CuentaBancaria cuentaBancaria;
    private TipoTransaccion tipo;
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Cotizacion cotizacion;
    private boolean borrado;
    private String usuario;
    private BigDecimal monto;

    public TransaccionBancaria() {
    }

    public TransaccionBancaria(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public TransaccionBancaria(String descripcion, TipoTransaccion tipo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public TransaccionBancaria(String codRef, String descripcion, Date fecha, CuentaBancaria cuentaBancaria, TipoTransaccion tipo, Moneda moneda, Cotizacion cotizacion, String usuario, BigDecimal monto) {
        this.codRef = codRef;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.cuentaBancaria = cuentaBancaria;
        this.tipo = tipo;
        this.moneda = moneda;
        this.cotizacion = cotizacion;
        this.usuario = usuario;
        this.monto = monto;
    }

    
    
    
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodRef() {
        return codRef;
    }

    public void setCodRef(String codRef) {
        this.codRef = codRef;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
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

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
        if (!(object instanceof TransaccionBancaria)) {
            return false;
        }
        TransaccionBancaria other = (TransaccionBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.prestamos.finanza.persistencia.TransaccionBancaria[ id=" + id + " ]";
    }
    
}

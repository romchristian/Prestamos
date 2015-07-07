/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

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
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoAplicacionPagoCuota;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author Acer
 */
@Entity
public class AplicacionPagoCuota implements Serializable,Auditable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Enumerated(EnumType.STRING)
    private TipoAplicacionPagoCuota tipo;
    @ManyToOne
    private DetPrestamo detPrestamo;
    @ManyToOne
    private FacturaVentaDetalle facturaVentaDetalle;
    private BigDecimal monto;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoAplicacionPagoCuota getTipo() {
        return tipo;
    }

    public void setTipo(TipoAplicacionPagoCuota tipo) {
        this.tipo = tipo;
    }

    public DetPrestamo getDetPrestamo() {
        return detPrestamo;
    }

    public void setDetPrestamo(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
    }

    public FacturaVentaDetalle getFacturaVentaDetalle() {
        return facturaVentaDetalle;
    }

    public void setFacturaVentaDetalle(FacturaVentaDetalle facturaVentaDetalle) {
        this.facturaVentaDetalle = facturaVentaDetalle;
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
        if (!(object instanceof AplicacionPagoCuota)) {
            return false;
        }
        AplicacionPagoCuota other = (AplicacionPagoCuota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.prestamos.prestamos.modelo.AplicacionPagoCuota[ id=" + id + " ]";
    }
    
}

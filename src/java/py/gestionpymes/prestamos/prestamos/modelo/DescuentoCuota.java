/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

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
import py.gestionpymes.prestamos.adm.modelo.Usuario;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author Acer
 */
@Entity
public class DescuentoCuota implements Serializable, Auditable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private DetPrestamo detPrestamo;
    @Enumerated(EnumType.STRING)
    private TipoDescuento tipo;
    private BigDecimal monto;
    private boolean aplicado;

    public TipoDescuento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDescuento tipo) {
        this.tipo = tipo;
    }

    public DescuentoCuota() {
        this.fecha = new Date();
    }

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

    public DetPrestamo getDetPrestamo() {
        return detPrestamo;
    }

    public void setDetPrestamo(DetPrestamo detPrestamo) {
        this.detPrestamo = detPrestamo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public boolean isAplicado() {
        return aplicado;
    }

    public void setAplicado(boolean aplicado) {
        this.aplicado = aplicado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final DescuentoCuota other = (DescuentoCuota) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

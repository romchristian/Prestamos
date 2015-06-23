/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author Acer
 */
@Entity
public class CargoPorMora implements Serializable, Auditable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private int inicioTramo;
    private int finTramo;
    private BigDecimal monto;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Enumerated(EnumType.STRING)
    private TipoCargo tipo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaBaja;

    public CargoPorMora() {
        this.estado = Estado.ACTIVO;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCargo getTipo() {
        return tipo;
    }

    public void setTipo(TipoCargo tipo) {
        this.tipo = tipo;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getInicioTramo() {
        return inicioTramo;
    }

    public void setInicioTramo(int inicioTramo) {
        this.inicioTramo = inicioTramo;
    }

    public int getFinTramo() {
        return finTramo;
    }

    public void setFinTramo(int finTramo) {
        this.finTramo = finTramo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaAnulacion() {
        return fechaBaja;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaBaja = fechaAnulacion;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargoPorMora)) {
            return false;
        }
        CargoPorMora other = (CargoPorMora) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return descripcion;
    }

}

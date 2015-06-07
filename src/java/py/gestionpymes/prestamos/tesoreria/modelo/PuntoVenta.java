/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.adm.modelo.Estado;
import py.gestionpymes.prestamos.adm.modelo.Usuario;
import py.gestionpymes.prestamos.contabilidad.modelo.Diario;
import py.gestionpymes.prestamos.contabilidad.modelo.MetodoPago;

/**
 *
 * @author Acer
 */
@Entity
public class PuntoVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @ManyToOne
    private Diario diario;
    @ManyToOne
    private Secuencia secuencia;
    @ManyToMany
    private List<MetodoPago> metodoPagos;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private BigDecimal saldo;
    @ManyToOne
    private Usuario usuario;

    public PuntoVenta() {
        this.estado = Estado.ACTIVO;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    
    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public Secuencia getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Secuencia secuencia) {
        this.secuencia = secuencia;
    }

    public List<MetodoPago> getMetodoPagos() {
        return metodoPagos;
    }

    public void setMetodoPagos(List<MetodoPago> metodoPagos) {
        this.metodoPagos = metodoPagos;
    }

    public void addMetodoPago(MetodoPago m) {
        if (metodoPagos == null) {
            metodoPagos = new ArrayList<>();
        }

        metodoPagos.add(m);
    }

    public void removeMetodoPago(MetodoPago m) {
        metodoPagos.remove(m);
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
        if (!(object instanceof PuntoVenta)) {
            return false;
        }
        PuntoVenta other = (PuntoVenta) object;
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

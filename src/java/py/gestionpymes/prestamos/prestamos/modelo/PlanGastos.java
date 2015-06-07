/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.modelo.Empresa;
import py.gestionpymes.prestamos.adm.modelo.Estado;

/**
 *
 * @author ACER
 */
@Entity

public class PlanGastos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @ManyToOne
    private Empresa empresa;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date  creacion;
    private Estado estado;
    @OneToMany(mappedBy = "planGastos",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<DetPlanGastos> detalles;

    public PlanGastos() {
        this.estado = Estado.ACTIVO;
        this.creacion = new Date();
                
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
        this.nombre = nombre.toUpperCase();
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<DetPlanGastos> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetPlanGastos> detalles) {
        this.detalles = detalles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final PlanGastos other = (PlanGastos) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return nombre;
    }
}

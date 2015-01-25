/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import py.gestionpymes.prestamos.adm.persistencia.Estado;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.CicloIngreso;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoEmpresa;

/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = ActividadLaboral.TODOS, query = "select a from ActividadLaboral a")})
public class ActividadLaboral implements Serializable {
 public static final String TODOS = "py.gestionpymes.clientes.persistencia.ActividadLaboral.TODOS";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String nombreEmpresa;
    private String cargo;
    @NotNull
    private double ingreso;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngreso;
    @Enumerated(EnumType.STRING)
    private CicloIngreso cicloIngreso;
    @Enumerated(EnumType.STRING)
    private TipoEmpresa tipoEmpresa;
    private boolean aportaIPS;
    @ManyToOne
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public ActividadLaboral() {
        
        this.estado=Estado.ACTIVO;
    }
    
    

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isAportaIPS() {
        return aportaIPS;
    }

    public void setAportaIPS(boolean aportaIPS) {
        this.aportaIPS = aportaIPS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public CicloIngreso getCicloIngreso() {
        return cicloIngreso;
    }

    public void setCicloIngreso(CicloIngreso cicloIngreso) {
        this.cicloIngreso = cicloIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public TipoEmpresa getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
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
        if (!(object instanceof ActividadLaboral)) {
            return false;
        }
        ActividadLaboral other = (ActividadLaboral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.clientes.ActividadLaboral[ id=" + id + " ]";
    }
}

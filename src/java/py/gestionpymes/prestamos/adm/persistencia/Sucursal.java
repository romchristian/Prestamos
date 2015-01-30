/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Sucursal.TODOS, query = "select s from Sucursal s"),
    @NamedQuery(name = Sucursal.POR_EMPRESA, query = "select s from Sucursal s where s.empresa = :empresa")})
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TODOS = "package py.gestionpymes.jpa.adm.Sucursal.TODOS";
    public static final String POR_EMPRESA = "package py.gestionpymes.jpa.adm.Sucursal.POR_EMPRESA";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @ManyToOne
    private Empresa empresa;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Sucursal() {
        this.estado = Estado.ACTIVO;
    }

    public Sucursal(Empresa empresa, String nombre) {
        this();
        this.empresa = empresa;
        this.nombre = nombre;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final Sucursal other = (Sucursal) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

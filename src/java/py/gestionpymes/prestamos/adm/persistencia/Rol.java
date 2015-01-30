/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia;


import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


/**
 *
 * @author cromero
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Rol.TODOS, query = "select r from Rol r"),
    @NamedQuery(name = Rol.POR_NOMBRE, query = "select r from Rol r where r.nombre = :nombre")})
public class Rol implements Serializable{
    public static final String TODOS = "py.gestionpymes.seguridad.Rol.TODOS";
    public static final String POR_NOMBRE = "py.gestionpymes.seguridad.Rol.POR_NOMBRE";
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
    

    public Rol() {
        this.estado = Estado.ACTIVO;
    }

    public Rol (String nombre) {
        this();
        this.nombre = nombre;
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

    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rol other = (Rol) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    


    
    
}

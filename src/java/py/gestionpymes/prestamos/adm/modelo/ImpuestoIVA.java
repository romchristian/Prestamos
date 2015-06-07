/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author cromero
 */
@Entity
@NamedQueries({
    @NamedQuery(name = ImpuestoIVA.TODOS, query = "select i from ImpuestoIVA i")})
public class ImpuestoIVA implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String TODOS = "package py.gestionpymes.jpa.adm.ImpuetoIVA.TODOS";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private String nombre;
    private Double valor;

    public ImpuestoIVA() {
    }

    public ImpuestoIVA(String nombre, Double valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
        if (!(object instanceof ImpuestoIVA)) {
            return false;
        }
        ImpuestoIVA other = (ImpuestoIVA) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.adm.ImpuestoIVA[ id=" + id + " ]";
    }
    
}

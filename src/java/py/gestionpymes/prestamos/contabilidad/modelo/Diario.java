/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Acer
 */
@Entity
public class Diario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @ManyToOne
    private Diario padre;
    @Enumerated(EnumType.STRING)
    private TipoDiario tipo;

    public Diario() {
    }
    
    

    public Diario(String descripcion, TipoDiario tipo) {
        this.descripcion = descripcion.toUpperCase();
        this.tipo = tipo;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }

    public Diario getPadre() {
        return padre;
    }

    public void setPadre(Diario padre) {
        this.padre = padre;
    }

    public TipoDiario getTipo() {
        return tipo;
    }

    public void setTipo(TipoDiario tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof Diario)) {
            return false;
        }
        Diario other = (Diario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

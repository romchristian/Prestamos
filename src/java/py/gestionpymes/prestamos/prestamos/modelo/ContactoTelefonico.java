/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoContactoTelefonico;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoContactoTelefonico;

/**
 *
 * @author ACER
 */
@Entity
public class ContactoTelefonico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    private String numero;
    private TipoContactoTelefonico tipo;
    private boolean principal;
    @Enumerated(EnumType.STRING)
    private EstadoContactoTelefonico estado;
    @ManyToOne
    private Cliente cliente;
    @Transient
    private String estiloPrincipal;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoContactoTelefonico getTipo() {
        return tipo;
    }

    public void setTipo(TipoContactoTelefonico tipo) {
        this.tipo = tipo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public EstadoContactoTelefonico getEstado() {
        return estado;
    }

    public void setEstado(EstadoContactoTelefonico estado) {
        this.estado = estado;
    }

    public String getEstiloPrincipal() {
        if(principal){
            estiloPrincipal = "background-color:#22bbcc";
        } 
        return estiloPrincipal;
    }

    public void setEstiloPrincipal(String estiloPrincipal) {
        this.estiloPrincipal = estiloPrincipal;
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
        if (!(object instanceof ContactoTelefonico)) {
            return false;
        }
        ContactoTelefonico other = (ContactoTelefonico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public boolean igual(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContactoTelefonico other = (ContactoTelefonico) obj;
        if ((this.numero == null) ? (other.numero != null) : !this.numero.equals(other.numero)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.principal != other.principal) {
            return false;
        }
        if (this.estado != other.estado) {
            return false;
        }
        if (this.cliente != other.cliente && (this.cliente == null || !this.cliente.equals(other.cliente))) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.clientes.ContactoTelefonico[ id=" + id + " ]";
    }
}

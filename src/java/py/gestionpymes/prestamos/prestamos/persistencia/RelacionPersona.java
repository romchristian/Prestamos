/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoRelacionPersona;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.persistencia.Estado;
import py.gestionpymes.prestamos.adm.persistencia.Persona;

/**
 *
 * @author ACER
 */
@Entity
public class RelacionPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Persona personaA;
    @ManyToOne
    private Persona personaB;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @ManyToOne
    private TipoRelacionPersona tipoRelacionPersona;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Persona getPersonaA() {
        return personaA;
    }

    public void setPersonaA(Persona personaA) {
        this.personaA = personaA;
    }

    public Persona getPersonaB() {
        return personaB;
    }

    public void setPersonaB(Persona personaB) {
        this.personaB = personaB;
    }

    public TipoRelacionPersona getTipoRelacionPersona() {
        return tipoRelacionPersona;
    }

    public void setTipoRelacionPersona(TipoRelacionPersona tipoRelacionPersona) {
        this.tipoRelacionPersona = tipoRelacionPersona;
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
        if (!(object instanceof RelacionPersona)) {
            return false;
        }
        RelacionPersona other = (RelacionPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.clientes.persistencia.RelacionPersona[ id=" + id + " ]";
    }
}

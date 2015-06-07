/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.modelo;

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
public class ValorEfectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private SesionTPV sesionTPV;
    private Integer denominacionMoneda;
    private Integer cantidad;
    @Enumerated(EnumType.STRING)
    private TipoValorEfectivo tipo;

    public TipoValorEfectivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoValorEfectivo tipo) {
        this.tipo = tipo;
    }

    public SesionTPV getSesionTPV() {
        return sesionTPV;
    }

    public void setSesionTPV(SesionTPV sesionTPV) {
        this.sesionTPV = sesionTPV;
    }

    public Integer getDenominacionMoneda() {
        return denominacionMoneda;
    }

    public void setDenominacionMoneda(Integer denominacionMoneda) {
        this.denominacionMoneda = denominacionMoneda;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ValorEfectivo)) {
            return false;
        }
        ValorEfectivo other = (ValorEfectivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.syscvsa.puntoventa.persisitencia.ValorEfectivoInicial[ id=" + id + " ]";
    }
}

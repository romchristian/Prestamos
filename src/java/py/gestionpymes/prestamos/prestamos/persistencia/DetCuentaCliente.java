/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author christian
 */
@Entity
public abstract class DetCuentaCliente<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private CuentaCliente cuentaCliente;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha = new Date();
    private TipoOperacion tipoOperacion;
    private String descripcion;
    
    private double montoCredito;
    private double montoDebito;
  
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    
    public CuentaCliente getCuentaCliente() {
        return cuentaCliente;
    }

    public void setCuentaCliente(CuentaCliente cuentaCliente) {
        this.cuentaCliente = cuentaCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(double montoCredito) {
        this.montoCredito = montoCredito;
    }

    public double getMontoDebito() {
        return montoDebito;
    }

    public void setMontoDebito(double montoDebito) {
        this.montoDebito = montoDebito;
    }

    public abstract T getReferencia();
  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetCuentaCliente)) {
            return false;
        }
        DetCuentaCliente other = (DetCuentaCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.clientes.DetCuentaCliente[ id=" + id + " ]";
    }
    
}

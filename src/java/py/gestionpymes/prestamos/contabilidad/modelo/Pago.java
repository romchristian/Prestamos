/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import py.gestionpymes.prestamos.adm.modelo.Cotizacion;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;



@Entity
public abstract class Pago implements Serializable,Auditable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descripcion;
    
    private BigDecimal monto = new BigDecimal(BigInteger.ZERO);
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Cotizacion cotizacion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @ManyToOne
    private FacturaVenta facturaVenta;
    
    @ManyToOne
    private Transaccion transaccion;
    
    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

  
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    public FacturaVenta getFacturaVenta() {
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }
    
    

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.cobranza.FormaPago[ id=" + id + " ]";
    }
    
}

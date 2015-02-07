/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Acer
 */
@Entity
public class FacturaVentaDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private FacturaVenta facturaVenta;
    private int nrolinea;
    private BigDecimal cantidad;
    private String descripcion;
    private BigDecimal precioUnitario;
    private BigDecimal impuesto = new BigDecimal(10);
    private BigDecimal exenta;
    private BigDecimal gravada05;
    private BigDecimal gravada10;
    

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
        if (!(object instanceof FacturaVentaDetalle)) {
            return false;
        }
        FacturaVentaDetalle other = (FacturaVentaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle[ id=" + id + " ]";
    }
    
}

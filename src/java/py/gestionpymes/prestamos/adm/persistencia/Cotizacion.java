/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;


/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Cotizacion.ULTIMO, query = "select c from Cotizacion c where c.moneda= :moneda order by c.vigencia desc"),
    @NamedQuery(name = Cotizacion.TODOS, query = "select c from Cotizacion c")})
public class Cotizacion implements Serializable {

    public static final String ULTIMO = "py.gestionpymes.prestamos.adm.persistencia.cotizacion.ULTIMO";
    public static final String TODOS = "py.gestionpymes.prestamos.adm.persistencia.cotizacion.TODOS";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Moneda moneda;
    private double compra;
    private double venta;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vigencia;

    public Cotizacion() {
    }



    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCompra() {
        return compra;
    }

    public void setCompra(double compra) {
        this.compra = compra;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public double getVenta() {
        return venta;
    }

    public void setVenta(double venta) {
        this.venta = venta;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
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
        if (!(object instanceof Cotizacion)) {
            return false;
        }
        Cotizacion other = (Cotizacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es","py"));
        return String.format("C: %s / V: %s ",nf.format(compra), nf.format(venta));
    }
    
    
}

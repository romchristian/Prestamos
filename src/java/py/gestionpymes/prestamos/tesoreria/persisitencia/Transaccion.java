/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.persisitencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;



/**
 *
 * @author emelgarejo
 */
@Entity
public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private SesionTPV sesionTPV;
    @ManyToOne
    private PuntoVenta puntoVenta;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;
    private String descripcion;
    private BigDecimal monto;
    @ManyToOne
    private Moneda moneda;    
    @ManyToOne
    private TipoTransaccionCaja tipoTransaccionCaja;
    private String usuarioLogeado;
    @OneToMany(mappedBy = "transaccion")
    private List<Pago> pagos; 

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
    

    
    public String getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(String usuarioLogeado) {
        this.usuarioLogeado = usuarioLogeado;
    }

    
    public Transaccion() {
        fecha = new Date();
    }

    public Transaccion(SesionTPV sesionTPV, TipoTransaccion tipoTransaccion, String descripcion, BigDecimal monto, Moneda moneda) {
        this();
        this.sesionTPV = sesionTPV;
        this.tipoTransaccion = tipoTransaccion;
        this.descripcion = descripcion;
        this.monto = monto;
        this.moneda = moneda;
    }
    
     public Transaccion(SesionTPV sesionTPV, TipoTransaccion tipoTransaccion, String descripcion, BigDecimal monto, Moneda moneda, PuntoVenta puntoVenta) {
        this();
        this.sesionTPV = sesionTPV;
        this.tipoTransaccion = tipoTransaccion;
        this.descripcion = descripcion;
        this.monto = monto;
        this.moneda = moneda;
        this.puntoVenta = puntoVenta;
    }

    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public TipoTransaccionCaja getTipoTransaccionCaja() {
        return tipoTransaccionCaja;
    }

    public void setTipoTransaccionCaja(TipoTransaccionCaja tipoTransaccionCaja) {
        this.tipoTransaccionCaja = tipoTransaccionCaja;
        if( this.tipoTransaccionCaja != null){
            this.tipoTransaccion = tipoTransaccionCaja.getTipoTransaccion();
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SesionTPV getSesionTPV() {
        return sesionTPV;
    }

    public void setSesionTPV(SesionTPV sesionTPV) {
        this.sesionTPV = sesionTPV;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
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
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.syscvsa.puntoventa.persisitencia.Transaccion[ id=" + id + " ]";
    }
    
}

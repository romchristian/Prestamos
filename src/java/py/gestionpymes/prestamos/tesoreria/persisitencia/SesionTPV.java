/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.persisitencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import py.gestionpymes.prestamos.adm.persistencia.Usuario;



/**
 *
 * @author Acer
 */
@Entity
public class SesionTPV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private PuntoVenta puntoVenta;
    private String estado;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaApertura;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCierre;
    private BigDecimal saldoInicial;
    private BigDecimal saldoCierre;
    private BigDecimal diferencia;
    private BigDecimal totalTransacciones;
    @OneToMany(mappedBy = "sesionTPV", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValorEfectivo> valorEfectivos;
    @Transient
    private List<ValorEfectivo> valorEfectivosInicial;
    @Transient
    private List<ValorEfectivo> valorEfectivosFinal;
  

    public SesionTPV() {
        fechaApertura = new Date();
        estado = "Creado";
    }
    
    

    public List<ValorEfectivo> getValorEfectivosInicial() {
        valorEfectivosInicial = new ArrayList<ValorEfectivo>();
        if (valorEfectivos == null) {
            valorEfectivos = new ArrayList<ValorEfectivo>();
        }
        for (ValorEfectivo v : valorEfectivos) {
            if (v.getTipo() == TipoValorEfectivo.INICIAL) {
                valorEfectivosInicial.add(v);
            }
        }
        return valorEfectivosInicial;
    }

    public List<ValorEfectivo> getValorEfectivosFinal() {
        valorEfectivosFinal = new ArrayList<ValorEfectivo>();
        if (valorEfectivos == null) {
            valorEfectivos = new ArrayList<ValorEfectivo>();
        }
        for (ValorEfectivo v : valorEfectivos) {
            if (v.getTipo() == TipoValorEfectivo.FINAL) {
                valorEfectivosFinal.add(v);
            }
        }
        return valorEfectivosFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    
    
    public List<ValorEfectivo> getValorEfectivos() {
        return valorEfectivos;
    }

    public void setValorEfectivos(List<ValorEfectivo> valorEfectivos) {
        this.valorEfectivos = valorEfectivos;
    }

    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getSaldoInicial() {
//        BigDecimal total = new BigDecimal(BigInteger.ZERO);
//        for (ValorEfectivo ve : getValorEfectivosInicial()) {
//            BigDecimal moneda = new BigDecimal(ve.getDenominacionMoneda());
//            BigDecimal cantidad = new BigDecimal(ve.getCantidad());
//
//            total = total.add(moneda.multiply(cantidad));
//        }
//        saldoInicial = total;
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoCierre() {
        return saldoCierre;
    }

    public void setSaldoCierre(BigDecimal saldoCierre) {
        this.saldoCierre = saldoCierre;
    }

    public BigDecimal getTotalTransacciones() {
        return totalTransacciones;
    }

    public void setTotalTransacciones(BigDecimal totalTransacciones) {
        this.totalTransacciones = totalTransacciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof SesionTPV)) {
            return false;
        }
        SesionTPV other = (SesionTPV) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.syscvsa.puntoventa.persisitencia.SesionTPV[ id=" + id + " ]";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoCuenta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author christian
 */
@Entity
@NamedQueries({
    @NamedQuery(name = CuentaCliente.TODOS, query = "select c from CuentaCliente c"),
    @NamedQuery(name = CuentaCliente.POR_CLIENTE, query = "select c from CuentaCliente c where c.cliente = :cliente")
})
public class CuentaCliente implements Serializable {

    public static Long contador = 1000l;
    public static final String TODOS = "package py.gestionpymes.jpa.clientes.CuentaCliente.TODOS";
    public static final String POR_CLIENTE = "package py.gestionpymes.jpa.clientes.CuentaCliente.POR_CLIENTE";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @ManyToOne
    private Cliente cliente;
    private String denominacion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaBaja;
    private String direccion;
    private String telefono;
    private String nroCuenta;
    private EstadoCuenta estado;
    @Transient
    private BigDecimal saldo;
    @Transient
    private BigDecimal totalDebito;
    @Transient
    private BigDecimal totalCredito;

    @OneToMany(mappedBy = "cuentaCliente", fetch = FetchType.EAGER)
    private List<DetCuentaCliente> detalles;

    public BigDecimal getSaldo() {
        saldo = getTotalCredito().subtract(getTotalDebito()).setScale(0, RoundingMode.HALF_EVEN);
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getTotalDebito() {
        if (totalDebito == null) {
            devuelveTotalDebito();
        }
        return totalDebito;
    }

    public void setTotalDebito(BigDecimal totalDebito) {
        this.totalDebito = totalDebito;
    }

    public BigDecimal getTotalCredito() {
        if (totalCredito == null) {
            devuelveTotalCredito();
        }
        return totalCredito;
    }

    public void setTotalCredito(BigDecimal totalCredito) {
        this.totalCredito = totalCredito;
    }

    public void devuelveTotalCredito() {
        totalCredito = new BigDecimal(BigInteger.ZERO);
        for (DetCuentaCliente d : detalles) {
            totalCredito = totalCredito.add(d.getMontoCredito().setScale(0, RoundingMode.HALF_EVEN));
        }

    }

    public void devuelveTotalDebito() {
        totalDebito = new BigDecimal(BigInteger.ZERO);
        for (DetCuentaCliente d : detalles) {
            totalDebito = totalDebito.add(d.getMontoDebito().setScale(0, RoundingMode.HALF_EVEN));
        }

    }

    public List<DetCuentaCliente> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetCuentaCliente> detalles) {
        this.detalles = detalles;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        if (!(object instanceof CuentaCliente)) {
            return false;
        }
        CuentaCliente other = (CuentaCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.jpa.clientes.CuentaCliente[ id=" + id + " ]";
    }

}

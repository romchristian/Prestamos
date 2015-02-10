/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad;

import py.gestionpymes.prestamos.adm.persistencia.Cobrador;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.Pago;

/**
 *
 * @author Acer
 */
@Entity
public class FacturaVenta implements Serializable {
    
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timbrado;
    private String codEstablecimiento;
    private String puntoExpedicion;
    private String numero;
    @Enumerated(EnumType.STRING)
    private CondicionVenta condicionVenta;
    private int cantidadCuota;
    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Sucursal sucursal;
    @ManyToOne
    private Cliente cliente;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEmision;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCreacion = new Date();
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCancelacion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAnulacion;
    
    @ManyToOne
    private Cobrador cobrador;
    
    private String razonSocial;
    private String ruc;
    private String direccion;
    private BigDecimal totalExento;
    private BigDecimal totalGravada05;
    private BigDecimal totalGravada10;
    private BigDecimal total;
    private BigDecimal iva05;
    private BigDecimal iva10;
    private BigDecimal totalIva;
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Cotizacion cotizacion;
    
    @OneToMany(mappedBy = "facturaVenta",cascade = CascadeType.ALL)
    private List<FacturaVentaDetalle> detalles;
    @OneToMany(mappedBy = "facturaVenta",cascade = CascadeType.ALL)
    private List<Pago> pagos;

    public List<FacturaVentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FacturaVentaDetalle> detalles) {
        this.detalles = detalles;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getCodEstablecimiento() {
        return codEstablecimiento;
    }

    public void setCodEstablecimiento(String codEstablecimiento) {
        this.codEstablecimiento = codEstablecimiento;
    }

    public String getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(String puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public CondicionVenta getCondicionVenta() {
        return condicionVenta;
    }

    public void setCondicionVenta(CondicionVenta condicionVenta) {
        this.condicionVenta = condicionVenta;
    }

    public int getCantidadCuota() {
        return cantidadCuota;
    }

    public void setCantidadCuota(int cantidadCuota) {
        this.cantidadCuota = cantidadCuota;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public Cobrador getCobrador() {
        return cobrador;
    }

    public void setCobrador(Cobrador cobrador) {
        this.cobrador = cobrador;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getTotalExento() {
        return totalExento;
    }

    public void setTotalExento(BigDecimal totalExento) {
        this.totalExento = totalExento;
    }

    public BigDecimal getTotalGravada05() {
        return totalGravada05;
    }

    public void setTotalGravada05(BigDecimal totalGravada05) {
        this.totalGravada05 = totalGravada05;
    }

    public BigDecimal getTotalGravada10() {
        return totalGravada10;
    }

    public void setTotalGravada10(BigDecimal totalGravada10) {
        this.totalGravada10 = totalGravada10;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIva05() {
        return iva05;
    }

    public void setIva05(BigDecimal iva05) {
        this.iva05 = iva05;
    }

    public BigDecimal getIva10() {
        return iva10;
    }

    public void setIva10(BigDecimal iva10) {
        this.iva10 = iva10;
    }

    public BigDecimal getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
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
        if (!(object instanceof FacturaVenta)) {
            return false;
        }
        FacturaVenta other = (FacturaVenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gestionpymes.prestamos.contabilidad.FacturaVenta[ id=" + id + " ]";
    }

}

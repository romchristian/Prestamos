/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.adm.modelo.Cotizacion;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.contabilidad.modelo.ChequeRecibido;
import py.gestionpymes.prestamos.finanza.modelo.enums.EstadoBoletaDeposito;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author Acer
 */

@Entity
public class BoletaDeposito implements Serializable, Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    private String nroComprobante;
    private BigDecimal totalEfectivo;
    private BigDecimal totalCheque;
    @ManyToOne
    private CuentaBancaria cuentaBancaria;
    @OneToMany(mappedBy = "boletaDeposito")
    private List<ChequeRecibido> cheques;
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Cotizacion cotizacion;
    private BigDecimal total;
    @Enumerated(EnumType.ORDINAL)
    private EstadoBoletaDeposito estado;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoBoletaDeposito getEstado() {
        return estado;
    }

    public void setEstado(EstadoBoletaDeposito estado) {
        this.estado = estado;
    }

    
  
    public BoletaDeposito() {
        this.estado = EstadoBoletaDeposito.PENDIENTE;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public BigDecimal getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(BigDecimal totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public BigDecimal getTotalCheque() {
        return totalCheque;
    }

    public void setTotalCheque(BigDecimal totalCheque) {
        this.totalCheque = totalCheque;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public List<ChequeRecibido> getCheques() {
        return cheques;
    }

    public void setCheques(List<ChequeRecibido> cheques) {
        this.cheques = cheques;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoletaDeposito other = (BoletaDeposito) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
  
    
    
}

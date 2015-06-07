/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.modelo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.tesoreria.modelo.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoMetodoPago;
import py.gestionpymes.prestamos.tesoreria.modelo.ValorMoneda;

/**
 *
 * @author Acer
 */
@Entity
public class MetodoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private boolean controlEfectivo;
    @ManyToMany(mappedBy = "metodoPagos")
    private List<PuntoVenta> puntoVentas;
    @ManyToOne
    private Moneda moneda;
    @ManyToOne
    private Diario diario;
    @Enumerated(EnumType.STRING)
    private TipoMetodoPago tipoMetodoPago;
    @OneToMany(mappedBy = "metodoPago", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ValorMoneda> valoresMonedas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PuntoVenta> getPuntoVentas() {
        return puntoVentas;
    }

    public void setPuntoVentas(List<PuntoVenta> puntoVentas) {
        this.puntoVentas = puntoVentas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public boolean isControlEfectivo() {
        return controlEfectivo;
    }

    public void setControlEfectivo(boolean controlEfectivo) {
        this.controlEfectivo = controlEfectivo;
    }

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public TipoMetodoPago getTipoMetodoPago() {
        return tipoMetodoPago;
    }

    public void setTipoMetodoPago(TipoMetodoPago tipoMetodoPago) {
        this.tipoMetodoPago = tipoMetodoPago;
    }
    

    public List<ValorMoneda> getValoresMonedas() {
         
        return valoresMonedas;
    }

    public void setValoresMonedas(List<ValorMoneda> valoresMonedas) {
        this.valoresMonedas = valoresMonedas;
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
        if (!(object instanceof MetodoPago)) {
            return false;
        }
        MetodoPago other = (MetodoPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

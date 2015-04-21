/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.contabilidad.persistencia.Recibo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author christian
 */
@Entity
public class CobroCuota extends Recibo {

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Prestamo prestamo;
    @ManyToOne
    private PrestamoHistorico prestamoHistorico;

    @OneToMany(mappedBy = "cobroCuota", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetCobroCuota> detalles;

    public CobroCuota() {
    }

    public CobroCuota(Prestamo prestamo) {
        this.prestamo = prestamo;
        this.cliente = prestamo.getCliente();

    }

    public CobroCuota(PrestamoHistorico prestamo) {
        this.prestamoHistorico = prestamo;
        this.cliente = prestamo.getCliente();

    }

    public CobroCuota(String concepto) {
        super("Cobro cuota/s");
    }

    public PrestamoHistorico getPrestamoHistorico() {
        return prestamoHistorico;
    }

    public void setPrestamoHistorico(PrestamoHistorico prestamoHistorico) {
        this.prestamoHistorico = prestamoHistorico;
    }

    
    public List<DetCobroCuota> getDetalles() {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        return detalles;
    }

    public void setDetalles(List<DetCobroCuota> detalles) {
        this.detalles = detalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author christian
 */
@Entity
public class CobroCuota extends Recibo{
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Prestamo prestamo;
    @OneToMany(mappedBy = "cobroCuota",cascade= CascadeType.ALL)
    private List<DetCobroCuota> detalles;
    

    public CobroCuota() {
    }

    public CobroCuota(Prestamo prestamo) {
        this.prestamo = prestamo;
        this.cliente = prestamo.getCliente();
    }
    
    

    
    public CobroCuota(String concepto) {
        super("Cobro cuota/s");
    }

    public List<DetCobroCuota> getDetalles() {
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

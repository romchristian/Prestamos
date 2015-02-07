/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ACER
 */
public abstract  class Sistema {

    private Prestamo prestamo;

    protected  Sistema(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    protected  Prestamo getPrestamo() {
        return prestamo;
    }
    
    
    protected abstract List<DetPrestamo> calculaCuotas();
    
    protected abstract BigDecimal getCuota();
    
}

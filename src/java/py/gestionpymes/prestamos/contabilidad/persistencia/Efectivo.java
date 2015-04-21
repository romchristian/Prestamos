/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.persistencia;

import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
import javax.persistence.Entity;

/**
 *
 * @author christian
 */
@Entity
public class Efectivo extends Pago{

    public Efectivo() {
        super.setDescripcion("EFECTIVO");
    }
    
    
    
        
}

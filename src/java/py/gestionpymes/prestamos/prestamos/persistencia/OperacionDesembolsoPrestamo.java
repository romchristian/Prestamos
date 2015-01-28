/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author christian
 */
@Entity
public class OperacionDesembolsoPrestamo extends DetCuentaCliente<Prestamo> {

    
    
    public OperacionDesembolsoPrestamo() {
        setTipoOperacion(TipoOperacion.PRESTAMO);
    }

    public OperacionDesembolsoPrestamo(Prestamo prestamo) {
        this();
        setPrestamo(prestamo);
        setMoneda(prestamo.getMoneda());
        setMontoCredito(prestamo.getTotalOperacion());
        setMontoDebito(0d);
        setDescripcion("Desembolso prestamo nro " + prestamo.getId());
        
    }

    

    @Override
    public Prestamo getReferencia() {
        return getPrestamo();
    }

    

    
}

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

    @ManyToOne
    private Prestamo prestamo;
    
    public OperacionDesembolsoPrestamo() {
        setTipoOperacion(TipoOperacion.PRESTAMO);
    }

    public OperacionDesembolsoPrestamo(Prestamo prestamo) {
        this();
        this.prestamo = prestamo;
        setMontoCredito(prestamo.getTotalOperacion());
        setMontoDebito(0d);
        setDescripcion("Desembolso prestamo nro " + prestamo.getId());
        
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    @Override
    public Prestamo getReferencia() {
        return prestamo;
    }

    

    
}

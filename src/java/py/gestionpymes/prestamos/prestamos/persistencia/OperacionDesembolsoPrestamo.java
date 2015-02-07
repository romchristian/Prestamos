/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import javax.persistence.Entity;

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
        setEmpresa(prestamo.getEmpresa());
        setSucursal(prestamo.getSucursal());
        setMoneda(prestamo.getMoneda());
        setMontoCredito(prestamo.getTotalOperacion());
        setMontoDebito(new BigDecimal(BigInteger.ZERO));
        setDescripcion("Desembolso prestamo nro " + prestamo.getId());
        
    }

    

    @Override
    public Prestamo getReferencia() {
        return getPrestamo();
    }

    

    
}

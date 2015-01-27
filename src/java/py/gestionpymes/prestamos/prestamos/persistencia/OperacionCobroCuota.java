/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author christian
 */
@Entity
public class OperacionCobroCuota extends DetCuentaCliente<DetCobroCuota> {

    
    @ManyToOne
    private DetCobroCuota detCobroCuota;
    
    public OperacionCobroCuota() {
        setTipoOperacion(TipoOperacion.PAGO_CUOTA);
    }

    public OperacionCobroCuota(DetCobroCuota detCobroCuota) {
        this();
        this.detCobroCuota = detCobroCuota;
        setMontoCredito(0d);
        setMontoDebito(detCobroCuota.getMonto());
        setDescripcion("Pago cuota "+ detCobroCuota.getDetPrestamo().getNroCuota()+" Prestamo nro "+ detCobroCuota.getCobroCuota().getPrestamo().getId());
        
    }

    

    @Override
    public DetCobroCuota getReferencia() {
        return detCobroCuota;
    }

   
    
}
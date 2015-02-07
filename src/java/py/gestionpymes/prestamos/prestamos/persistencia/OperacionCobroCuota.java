/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
        Prestamo prestamo = detCobroCuota.getDetPrestamo().getPrestamo();
        setPrestamo(prestamo);
        setEmpresa(prestamo.getEmpresa());
        setSucursal(prestamo.getSucursal());
        this.detCobroCuota = detCobroCuota;
        setMontoCredito(new BigDecimal(BigInteger.ZERO));
        setMoneda(detCobroCuota.getMoneda());
        setMontoDebito(detCobroCuota.getMonto());
        setDescripcion("Pago cuota "+ detCobroCuota.getDetPrestamo().getNroCuota()+" Prestamo nro "+ detCobroCuota.getCobroCuota().getPrestamo().getId());
        
    }

    

    @Override
    public DetCobroCuota getReferencia() {
        return detCobroCuota;
    }

   
    
}

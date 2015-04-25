/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.persisitencia;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVenta;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author Acer
 */
@Entity
public class TransaccionDesembolso extends Transaccion implements Serializable{

    @ManyToOne
    private FacturaVenta facturaVenta;
    @ManyToOne
    private Prestamo prestamo;

    public TransaccionDesembolso() {
        
    }

    public TransaccionDesembolso(FacturaVenta facturaVenta, Prestamo prestamo, SesionTPV sesionTPV, String descripcion, BigDecimal monto, Moneda moneda) {
        super(sesionTPV, TipoTransaccion.SALIDA, descripcion, monto, moneda,sesionTPV.getPuntoVenta());
        this.facturaVenta = facturaVenta;
        this.prestamo = prestamo;
    }

    public FacturaVenta getFacturaVenta() {
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

}

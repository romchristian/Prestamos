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
import py.gestionpymes.prestamos.contabilidad.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle;

/**
 *
 * @author christian
 */
@Entity
public class OperacionCobroCuotaFactura extends DetCuentaCliente<FacturaVentaDetalle> {

    @ManyToOne
    private FacturaVentaDetalle facturaVentaDetalle;
    @ManyToOne
    private FacturaVenta facturaVenta;

    public OperacionCobroCuotaFactura() {
        setTipoOperacion(TipoOperacion.PAGO_CUOTA);
    }

    public OperacionCobroCuotaFactura(FacturaVentaDetalle d) {
        this();
        this.facturaVentaDetalle = d;
        this.facturaVenta = d.getFacturaVenta();
        Prestamo prestamo = d.getDetPrestamo().getPrestamo();
        setPrestamo(prestamo);
        setEmpresa(prestamo.getEmpresa());
        setSucursal(prestamo.getSucursal());
        setMontoCredito(new BigDecimal(BigInteger.ZERO));
        setMoneda(d.getFacturaVenta().getMoneda());
        setMontoDebito(d.getGravada10());
        String desc = "";
        if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_CUOTA) == 0) {
            desc = "Pago de cuota #" + d.getDetPrestamo().getNroCuota()+" del Prestamo # " + prestamo.getId();
        } else if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0) {
            desc = "Pago moratorio por la cuota #" + d.getDetPrestamo().getNroCuota()+" del Prestamo # " + prestamo.getId();
        } else if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_PUNITORIO) == 0) {
            desc = "Pago punitorio por la cuota #" + d.getDetPrestamo().getNroCuota()+" del Prestamo # " + prestamo.getId();
        }

        setDescripcion(desc);

    }

    public FacturaVentaDetalle getFacturaVentaDetalle() {
        return facturaVentaDetalle;
    }

    public void setFacturaVentaDetalle(FacturaVentaDetalle facturaVentaDetalle) {
        this.facturaVentaDetalle = facturaVentaDetalle;
    }

    public FacturaVenta getFacturaVenta() {
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    @Override
    public FacturaVentaDetalle getReferencia() {
        return facturaVentaDetalle;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoOperacion;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle;

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
        setDescripcion("Pago cuota " + detCobroCuota.getDetPrestamo().getNroCuota() + " Prestamo nro " + detCobroCuota.getCobroCuota().getPrestamo().getId());

    }

    public OperacionCobroCuota(DetCobroCuota d, boolean esCredito) {

        PrestamoHistorico prestamo = d.getCobroCuota().getPrestamoHistorico();
        setPrestamoHistorico(prestamo);
        setEmpresa(prestamo.getEmpresa());
        setSucursal(prestamo.getSucursal());
        setMontoCredito(new BigDecimal(BigInteger.ZERO));
        setMoneda(d.getFacturaVenta().getMoneda());

        String desc = "";
        if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0) {
            if (esCredito) {
                setMontoDebito(new BigDecimal(BigInteger.ZERO));
                setMontoCredito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
                desc = "Recargo moratorio por la cuota #" + d.getDetPrestamo().getNroCuota() + " del Prestamo # " + prestamo.getId();
            } else {
                setMontoDebito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
                setMontoCredito(new BigDecimal(BigInteger.ZERO));
                desc = "Pago moratorio por la cuota #" + d.getDetPrestamo().getNroCuota() + " del Prestamo # " + prestamo.getId();
            }

        } else if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_CUOTA) == 0) {
            setMontoDebito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
            setMontoCredito(new BigDecimal(BigInteger.ZERO));

            desc = "Pago por la cuota #" + d.getDetPrestamo().getNroCuota() + " del Prestamo # " + prestamo.getId();
        }

        setDescripcion(desc);

    }

    @Override
    public DetCobroCuota getReferencia() {
        return detCobroCuota;
    }

}

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
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVentaDetalle;

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
        this();

        PrestamoHistorico prestamo = d.getCobroCuota().getPrestamoHistorico();
        setPrestamoHistorico(prestamo);
        setEmpresa(prestamo.getEmpresa());
        setSucursal(prestamo.getSucursal());
        setMontoCredito(new BigDecimal(BigInteger.ZERO));
        setMoneda(d.getMoneda());

        String desc = "";
        if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_MORATORIO) == 0) {
            System.out.println("Entre en Moratorio 2");
            if (esCredito) {
                System.out.println("Entre en Moratorio 3");
                setMontoDebito(new BigDecimal(BigInteger.ZERO));
                setMontoCredito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
                System.out.println("Monto Credito : " + getMontoCredito());
                desc = "Recargo moratorio por la cuota #" + d.getDetPrestamoHistorico().getNroCuota() + " del Prestamo # " + prestamo.getId();
            } else {
                setMontoDebito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
                setMontoCredito(new BigDecimal(BigInteger.ZERO));
                desc = "Pago moratorio por la cuota #" + d.getDetPrestamoHistorico().getNroCuota() + " del Prestamo # " + prestamo.getId();
            }

        } else if (d.getRefMonto().compareToIgnoreCase(FacturaVentaDetalle.MONTO_CUOTA) == 0) {
            setMontoDebito(d.getMonto().setScale(0, RoundingMode.HALF_EVEN));
            setMontoCredito(new BigDecimal(BigInteger.ZERO));

            desc = "Pago por la cuota #" + d.getDetPrestamoHistorico().getNroCuota() + " del Prestamo # " + prestamo.getId();
        }

        setDescripcion(desc);

    }

    @Override
    public DetCobroCuota getReferencia() {
        return detCobroCuota;
    }

}

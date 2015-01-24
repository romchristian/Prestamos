/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
public class InteresSimple extends Sistema {

    public InteresSimple(Prestamo prestamo) {
        super(prestamo);
    }

    @Override
    protected List<DetPrestamo> calculaCuotas() {
        List<DetPrestamo> detalles = new ArrayList<DetPrestamo>();
        Prestamo p = getPrestamo();


        for (int i = 0; i < p.getPlazo(); i++) {
            int nroCuota = i + 1;
            double cuotaCapital = (p.getCapital() ) / p.getPlazo();
            double cuotaInteres = calculaInteres(nroCuota, p);
            double saldoCapital = (p.getCapital()) - cuotaCapital * (nroCuota-1);

            DetPrestamo d = new DetPrestamo(p, nroCuota, cuotaCapital, cuotaInteres,saldoCapital);
            detalles.add(d);
        }

        return detalles;
    }

    protected double getCuota() {
        double R = getPrestamo().getTotalOperacion()/getPrestamo().getPlazo();
        return R;
    }

    private double calculaInteres(int nroCuota, Prestamo p) {
        double R = 0;

        R = ((p.getCapital()  * (p.getTasa() / 100d) * (p.getPlazo() / 10d))) / p.getPlazo();

        return R;

    }
}

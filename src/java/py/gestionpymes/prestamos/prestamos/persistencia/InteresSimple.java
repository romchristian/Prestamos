/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        List<DetPrestamo> detalles = new ArrayList<>();
        Prestamo p = getPrestamo();

        for (int i = 0; i < p.getPlazo(); i++) {
            int nroCuota = i + 1;
            BigDecimal cuotaCapital = p.getCapital().divide(new BigDecimal(p.getPlazo()), 6, RoundingMode.HALF_EVEN); //(p.getCapital() ) / p.getPlazo();
            BigDecimal cuotaInteres = calculaInteres(nroCuota, p);
            BigDecimal saldoCapital = p.getCapital().subtract(cuotaCapital.multiply(new BigDecimal(nroCuota - 1)));

            DetPrestamo d = new DetPrestamo(p, nroCuota, cuotaCapital, cuotaInteres, saldoCapital);
            detalles.add(d);
        }

        return detalles;
    }

    @Override
    protected BigDecimal getCuota() {
        BigDecimal R = getPrestamo().getTotalOperacion().divide(new BigDecimal(getPrestamo().getPlazo()), 6, RoundingMode.HALF_EVEN);
        return R;
    }

    private BigDecimal calculaInteres(int nroCuota, Prestamo p) {
        BigDecimal R;
        BigDecimal div1 = p.getTasa().divide(new BigDecimal(100), 6, RoundingMode.HALF_EVEN);//tasa interes anual

        int periocidad = 1;
        switch (p.getPeriodoPago()) {
            case QUINCENAL:
                periocidad = 2;
                break;
            case SEMANAL:
                periocidad = 4;
                break;
            case DIARIO:
                periocidad = 30;
                break;
        }
        
        
        BigDecimal div2 = div1.divide(new BigDecimal(12*periocidad), 6, RoundingMode.HALF_EVEN);//interes mensual
                
        R = p.getCapital().multiply(div2); //interes del mes

        //R = ((p.getCapital()  * ((p.getTasa() / 100d) / 12d))) / p.getPlazo();
        //R = ((p.getCapital()  * (p.getTasa() / 100d) * (p.getPlazo() / 10d))) / p.getPlazo();error
        return R;

    }
}

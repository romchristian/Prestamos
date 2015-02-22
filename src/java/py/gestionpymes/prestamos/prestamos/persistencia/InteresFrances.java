/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.persistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
public class InteresFrances extends Sistema {

    public InteresFrances(Prestamo prestamo) {
        super(prestamo);
    }

    @Override
    protected List<DetPrestamo> calculaCuotas() {
        List<DetPrestamo> detalles = new ArrayList<>();
        Prestamo p = getPrestamo();

        for (int i = 0; i < p.getPlazo(); i++) {
            int nroCuota = i;
            BigDecimal saldoCapital = getSaldoCapital(nroCuota).setScale(8, RoundingMode.HALF_EVEN);
            BigDecimal cuotaInteres = getCuotaInteres(saldoCapital).setScale(8, RoundingMode.HALF_DOWN);
            BigDecimal cuotaCapital = getCuotaCapital(cuotaInteres).setScale(8, RoundingMode.HALF_EVEN);

            DetPrestamo d = new DetPrestamo(p, nroCuota + 1, cuotaCapital, cuotaInteres, saldoCapital);
            detalles.add(d);
        }

        return detalles;
    }

    /**
     *
     * @return (C) cuota total : cuota final fija = cuota de capital + cuota de
     * interes
     */
    @Override
    protected BigDecimal getCuota() {
        BigDecimal V = getPrestamo().getCapital();
        float i = getInteresPeriodico();
        int n = getPlazo();

        BigDecimal pow = new BigDecimal((Math.pow((1 + i), n)), MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);
        BigDecimal op1 = pow.multiply(new BigDecimal(i, MathContext.DECIMAL128)).setScale(8, RoundingMode.HALF_EVEN);
        BigDecimal op2 = pow.subtract(new BigDecimal(BigInteger.ONE)).setScale(8, RoundingMode.HALF_EVEN);

        BigDecimal C = V.multiply(op1).divide(op2, MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);
//V * ((Math.pow((1+i), n)*i)/(Math.pow((1+i), n)-1)); 
        return C;
    }

    /**
     *
     * @return Cuota de Amortización
     */
    private BigDecimal getCuotaAmortizacion(int nroCuota) {
        BigDecimal V = getPrestamo().getCapital();// Capital sumar comision
        float i = getInteresPeriodico();
        BigDecimal pow = new BigDecimal(Math.pow(1 + i, (nroCuota - 1)), MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);

        BigDecimal tp = getT1().multiply(pow, MathContext.DECIMAL128).setScale(8, RoundingMode.HALF_EVEN);

        return tp;
    }

    /**
     *
     * @return valor total del prestamo a las n cuotas actualizadas a la tasa de
     * referencia VP = C * (1-(1+i)^-n+p)/i)
     */
    public BigDecimal getSaldoCapital(int nroCuota) {
//        float i = getInteresPeriodico();
//                         1+ 0.0375
//        136516 * (-11        ) / 0.0375
//                         
//        BigDecimal pow = new BigDecimal(Math.pow((1+i),-getPlazo()+nroCuota),MathContext.DECIMAL64);
//        BigDecimal op1 = new BigDecimal(BigInteger.ONE).subtract(pow);
//        BigDecimal op2 = op1.divide(new BigDecimal(i,MathContext.DECIMAL128),0,RoundingMode.HALF_EVEN);
//        BigDecimal VP = getCuota().multiply(op2);//getCuota() * ((1-Math.pow((1+i),-getPlazo()+nroCuota))/i);

        BigDecimal amortizacionAcumulada = new BigDecimal(BigInteger.ZERO).setScale(8, RoundingMode.HALF_EVEN);
        for (int i = 1; i <= nroCuota; i++) {
            amortizacionAcumulada = amortizacionAcumulada.add(getCuotaAmortizacion(i)).setScale(8, RoundingMode.HALF_EVEN);
        }

        return getPrestamo().getCapital().subtract(amortizacionAcumulada);
    }

    public BigDecimal getCuotaInteres(BigDecimal vp) {
        return vp.multiply(new BigDecimal(getInteresPeriodico()).setScale(8, RoundingMode.HALF_EVEN));
    }

    public BigDecimal getCuotaCapital(BigDecimal cuotaInteres) {
        return getCuota().subtract(cuotaInteres).setScale(8, RoundingMode.HALF_EVEN);
    }

    /**
     *
     * @return cantidad de cuotas en las que se divide el prestamo (n)
     */
    public int getPlazo() {
        return getPrestamo().getPlazo();
    }

    public float getInteresPeriodico() {
        int meses = 12;

        switch (getPrestamo().getPeriodoPago()) {
            case MENSUAL:
                meses = 12;
                break;
            case QUINCENAL:
                meses = meses * 2;
                break;
            case SEMANAL:
                meses = meses * 4;
                break;
            case DIARIO:
                meses = meses * 30;
                break;
        }

        return (getPrestamo().getTasa().floatValue() / 100f) / meses;
    }

    public BigDecimal getT1() {
        BigDecimal V = getPrestamo().getCapital();
        float i = getInteresPeriodico();
        BigDecimal op1 = V.multiply(new BigDecimal(i, MathContext.DECIMAL128)).setScale(8, RoundingMode.HALF_EVEN);
        return getCuota().subtract(op1);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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

        Date auxFecha = p.getFechaPrimerVencimiento();
        for (int i = 0; i < p.getPlazo(); i++) {
            int nroCuota = i;
            
            BigDecimal saldoCapital = getSaldoCapital(nroCuota);
            BigDecimal cuotaInteres = getCuotaInteres(saldoCapital);
            BigDecimal cuotaCapital = getCuotaCapital(cuotaInteres);

            DetPrestamo d = new DetPrestamo(p, nroCuota + 1, cuotaCapital, cuotaInteres, saldoCapital);
            detalles.add(d);
        }

        p.setFechaPrimerVencimiento(auxFecha);
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
        float i = getInteresPeriodico().floatValue();
        int n = getPlazo();

        BigDecimal pow = new BigDecimal((Math.pow((1 + getInteresPeriodico().floatValue()), n))).setScale(14, RoundingMode.HALF_EVEN);
        BigDecimal op1 = pow.multiply(new BigDecimal(getInteresPeriodico()).setScale(14, RoundingMode.HALF_EVEN));
        BigDecimal op2 = pow.subtract(new BigDecimal(BigInteger.ONE));

        BigDecimal C = V.multiply(op1).divide(op2, 8, RoundingMode.HALF_EVEN);
//V * ((Math.pow((1+i), n)*i)/(Math.pow((1+i), n)-1)); 
        return C;
    }

    /**
     *
     * @return
     */
    private BigDecimal getCuotaAmortizacion(int nroCuota) {
        BigDecimal V = getPrestamo().getCapital();// Capital sumar comision
        float i = getInteresPeriodico().floatValue();
        BigDecimal pow = new BigDecimal(Math.pow(1 + i, (nroCuota - 1))).setScale(14, RoundingMode.HALF_EVEN);

        BigDecimal tp = getT1().multiply(pow);

        return tp;
    }

    /**
     *
     * @return valor total del prestamo a las n cuotas actualizadas a la tasa de
     * referencia VP = C * (1-(1+i)^-n+p)/i)
     */
    public BigDecimal getSaldoCapital(int nroCuota) {
        BigDecimal amortizacionAcumulada = new BigDecimal(BigInteger.ZERO);
        for (int i = 1; i <= nroCuota; i++) {
            amortizacionAcumulada = amortizacionAcumulada.add(getCuotaAmortizacion(i).setScale(0, RoundingMode.HALF_EVEN));
        }

        return getPrestamo().getCapital().subtract(amortizacionAcumulada.setScale(0, RoundingMode.HALF_EVEN));
    }

    public BigDecimal getCuotaInteres(BigDecimal vp) {
        return vp.multiply(new BigDecimal(getInteresPeriodico()).setScale(14, RoundingMode.HALF_EVEN)).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getCuotaCapital(BigDecimal cuotaInteres) {
        return getCuota().subtract(cuotaInteres).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getCuotaCapital(int nroCuota) {
        return getCuotaAmortizacion(nroCuota);
    }

    /**
     *
     * @return cantidad de cuotas en las que se divide el prestamo (n)
     */
    public int getPlazo() {
        return getPrestamo().getPlazo();
    }

    public Double getInteresPeriodico() {
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

        Double R = (getPrestamo().getTasa().floatValue() / 100d) / meses;
        return R;
    }

    public BigDecimal getT1() {
        BigDecimal V = getPrestamo().getCapital();
        float i = getInteresPeriodico().floatValue();
        BigDecimal op1 = V.multiply(new BigDecimal(i)).setScale(0, RoundingMode.HALF_EVEN);
        return getCuota().subtract(op1).setScale(0, RoundingMode.HALF_EVEN);
    }

}

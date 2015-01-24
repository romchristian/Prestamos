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
public class InteresFrances extends Sistema{

    public InteresFrances(Prestamo prestamo) {
        super(prestamo);
    }

    
    @Override
    protected List<DetPrestamo> calculaCuotas() {
        List<DetPrestamo> detalles = new ArrayList<DetPrestamo>();
        Prestamo p = getPrestamo();


        for (int i = 0; i < p.getPlazo(); i++) {
            int nroCuota = i;
            double saldoCapital = getSaldoCapital(nroCuota);
            double cuotaInteres = getCuotaInteres(saldoCapital);
            double cuotaCapital = getCuotaCapital(cuotaInteres);
           

            DetPrestamo d = new DetPrestamo(p, nroCuota+1, cuotaCapital, cuotaInteres,saldoCapital);
            detalles.add(d);
        }

        return detalles;
    }

    
    /**
     * 
     * @return (C) cuota total : cuota final fija = cuota de capital + cuota de interes
     */
    @Override
    protected double getCuota() {
        double V = getPrestamo().getCapital();
        float i = getInteresPeriodico();
        int n = getPlazo();
        
        double C = V * ((Math.pow((1+i), n)*i)/(Math.pow((1+i), n)-1)); 
        return  C;
    }
    
    /**
     * 
     * @return 
     */
    private double getCuotaAmortizacion(int nroCuota){
        double V = getPrestamo().getCapital() + getPrestamo().getGastos();
        float i = getInteresPeriodico();
        
        
        double tp = getT1()*Math.pow(1+i, (nroCuota -1));
        
       return tp;
    }
    
    /**
     * 
     * @return valor total del prestamo a las n cuotas actualizadas 
     *  a la tasa de referencia 
     * VP = C * (1-(1+i)^-n+p)/i)
     */
    
    public double getSaldoCapital(int nroCuota){
        float i = getInteresPeriodico();
        double VP = getCuota() * ((1-Math.pow((1+i),-getPlazo()+nroCuota))/i);
        return VP;
    }
    
    public double getCuotaInteres(double vp){
        return vp * getInteresPeriodico();
    }
    
    public double getCuotaCapital(double cuotaInteres){
        return getCuota() - cuotaInteres;
    }
    
    
    /**
     * 
     * @return cantidad de cuotas en las que se divide el prestamo (n)
     */
    public int getPlazo(){
        return getPrestamo().getPlazo();
    }
    
    public float getInteresPeriodico(){
        return (getPrestamo().getTasa()/100f) / getPlazo();
    }
    
    public double getT1(){
        double V = getPrestamo().getCapital();
        float i = getInteresPeriodico();
        return getCuota() - V*i;
    }
    
    
}

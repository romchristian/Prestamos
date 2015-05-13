/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 * @author Acer
 */
public class VistaGrafico {

    private BigDecimal saldo;
    private BigDecimal entradas;
    private BigDecimal salidas;
    private BigDecimal disponible;

    public VistaGrafico() {
        this.saldo = new BigDecimal(BigInteger.ZERO);
        this.entradas = new BigDecimal(BigInteger.ZERO);
        this.salidas = new BigDecimal(BigInteger.ZERO);
        this.disponible = new BigDecimal(BigInteger.ZERO);
    }
    public VistaGrafico(Object[] obj) {
        this.saldo = (BigDecimal) obj[0];
        this.entradas = (BigDecimal) obj[1];
        this.salidas = (BigDecimal) obj[2];
        this.disponible = saldo.add(entradas).subtract(salidas).setScale(0, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getEntradas() {
        return entradas;
    }

    public void setEntradas(BigDecimal entradas) {
        this.entradas = entradas;
    }

    public BigDecimal getSalidas() {
        return salidas;
    }

    public void setSalidas(BigDecimal salidas) {
        this.salidas = salidas;
    }

    public BigDecimal getDisponible() {

        return disponible;
    }

    public void setDisponible(BigDecimal disponible) {
        this.disponible = disponible;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Acer
 */
public class DescuentoTemp {

    private Integer nroCuota;
    private boolean aplicaDescuentoMora;
    private BigDecimal descuentoMora;
    private boolean aplicaDescuentoCargo;
    private BigDecimal descuentoCargo;
    private boolean aplicaDescuentoInteres;
    private BigDecimal descuentoInteres;
    private BigDecimal total;

    public DescuentoTemp() {
        descuentoMora = new BigDecimal(BigInteger.ZERO);
        descuentoCargo = new BigDecimal(BigInteger.ZERO);
        descuentoInteres = new BigDecimal(BigInteger.ZERO);
    }

    public Integer getNroCuota() {
        return nroCuota;
    }

    public void setNroCuota(Integer nroCuota) {
        this.nroCuota = nroCuota;
    }

    public boolean isAplicaDescuentoMora() {
        return aplicaDescuentoMora;
    }

    public void setAplicaDescuentoMora(boolean aplicaDescuentoMora) {
        this.aplicaDescuentoMora = aplicaDescuentoMora;
    }

    public BigDecimal getDescuentoMora() {
        return descuentoMora;
    }

    public void setDescuentoMora(BigDecimal descuentoMora) {
        this.descuentoMora = descuentoMora;
    }

    public boolean isAplicaDescuentoCargo() {
        return aplicaDescuentoCargo;
    }

    public void setAplicaDescuentoCargo(boolean aplicaDescuentoCargo) {
        this.aplicaDescuentoCargo = aplicaDescuentoCargo;
    }

    public BigDecimal getDescuentoCargo() {
        return descuentoCargo;
    }

    public void setDescuentoCargo(BigDecimal descuentoCargo) {
        this.descuentoCargo = descuentoCargo;
    }

    public boolean isAplicaDescuentoInteres() {
        return aplicaDescuentoInteres;
    }

    public void setAplicaDescuentoInteres(boolean aplicaDescuentoInteres) {
        this.aplicaDescuentoInteres = aplicaDescuentoInteres;
    }

    public BigDecimal getDescuentoInteres() {
        return descuentoInteres;
    }

    public void setDescuentoInteres(BigDecimal descuentoInteres) {
        this.descuentoInteres = descuentoInteres;
    }

    public BigDecimal getTotal() {
        total = new BigDecimal(BigInteger.ZERO);
        if (aplicaDescuentoMora) {
            total = total.add(descuentoMora);
        }

        if (aplicaDescuentoCargo) {
            total = total.add(descuentoCargo);
        }

        if (aplicaDescuentoInteres) {
            total = total.add(descuentoInteres);
        }
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}

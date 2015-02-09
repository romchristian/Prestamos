/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Temporal;

/**
 *
 * @author Acer
 */
public class LiquidacionPrestamo {

    private Long id;
    private String empresa;
    private String sucursal;
    private String vendedor;
    private String cliente;
    private String codeudor;
    private BigDecimal montoPrestamo = new BigDecimal(BigInteger.ZERO);
    private BigDecimal capital = new BigDecimal(BigInteger.ZERO);
    private int plazo;
    private int tasa;
    private String periodoPago;
    private BigDecimal gastos = new BigDecimal(BigInteger.ZERO);
    private BigDecimal comisiones = new BigDecimal(BigInteger.ZERO);
    private BigDecimal impuestoIVA = new BigDecimal(BigInteger.ZERO);
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicioOperacion;
    private BigDecimal montoCuota = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalIntereses = new BigDecimal(BigInteger.ZERO);
    private BigDecimal totalOperacion = new BigDecimal(BigInteger.ZERO);
    private String sistemaAmortizacion;
    private String moneda;

}

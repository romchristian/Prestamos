/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.persistencia;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import py.gestionpymes.prestamos.adm.persistencia.Cotizacion;
import py.gestionpymes.prestamos.adm.persistencia.Moneda;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;

/**
 *
 * @author Acer
 */
@Entity
public class TransaccionDepositoEfectivo extends TransaccionBancaria{
    private String comprobante;
    
    public TransaccionDepositoEfectivo() {
    }

    public TransaccionDepositoEfectivo(String codRef,String comprobante,Date fecha, CuentaBancaria cuentaBancaria, Moneda moneda, Cotizacion cotizacion, String usuario, BigDecimal monto) {
        super(codRef, "Deposito Efectivo comprobate: " +comprobante, fecha, cuentaBancaria, TipoTransaccion.ENTRADA, moneda, cotizacion, usuario, monto);
        this.comprobante = comprobante;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }    
    
}

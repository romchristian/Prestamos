/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import py.gestionpymes.prestamos.adm.modelo.Cotizacion;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccion;

/**
 *
 * @author Acer
 */
@Entity
public class TransaccionReversionDepositoEfe extends TransaccionBancaria{
    private String comprobante;
    
    public TransaccionReversionDepositoEfe() {
    }

    public TransaccionReversionDepositoEfe(String codRef,String comprobante, CuentaBancaria cuentaBancaria, Moneda moneda, Cotizacion cotizacion, String usuario, BigDecimal monto) {
        super(codRef, "Reversión Deposito Efectivo comprobate: " +comprobante, new Date(), cuentaBancaria, TipoTransaccion.SALIDA, moneda, cotizacion, usuario, monto);
        
        this.comprobante = comprobante;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }
}

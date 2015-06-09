/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.adm.modelo.Cotizacion;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.tesoreria.modelo.TipoTransaccion;

/**
 *
 * @author Acer
 */
@Entity
public class TransaccionReversaCobroCheque extends TransaccionBancaria{
    private String comprobante;
    @ManyToOne
    private ChequeEmitido chequeEmitido;
    
    public TransaccionReversaCobroCheque() {
    }

    public TransaccionReversaCobroCheque(String codRef,ChequeEmitido ch, CuentaBancaria cuentaBancaria, Moneda moneda, Cotizacion cotizacion, String usuario, BigDecimal monto) {
        super(codRef, "Reversión de cobro de cheque #: " + ch.getNumero(), new Date(), cuentaBancaria, TipoTransaccion.ENTRADA, moneda, cotizacion, usuario, monto);
        this.chequeEmitido = ch;
        this.comprobante = ch.getNumero();
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }    

    public ChequeEmitido getChequeEmitido() {
        return chequeEmitido;
    }

    public void setChequeEmitido(ChequeEmitido chequeEmitido) {
        this.chequeEmitido = chequeEmitido;
    }
    
    
}

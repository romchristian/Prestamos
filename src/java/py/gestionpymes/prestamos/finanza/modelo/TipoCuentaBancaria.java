/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

/**
 *
 * @author Acer
 */
public enum TipoCuentaBancaria {
    CUENTA_CORRIENTE("Cuenta Corriente"),
    CAJA_AHORRO("Caja Ahorro");
    
    private String name;

    private TipoCuentaBancaria(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    
}

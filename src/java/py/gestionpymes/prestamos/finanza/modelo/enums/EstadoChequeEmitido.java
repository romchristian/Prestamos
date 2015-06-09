/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo.enums;

/**
 *
 * @author Acer
 */
public enum EstadoChequeEmitido {
    BORRADOR("CREACIÓN CHEQUE"),
    AUTORIZADO("AUTORIZACIÓN CHEQUE"),
    COMPROMETIDO("ENTREGA CHEQUE"),
    COBRADO("CONFIRMACIÓN COBRO CHEQUE"),
    ANULADO("ANULACIÓN CHEQUE");
    
    private String operacion;

    private EstadoChequeEmitido(String operacion) {
        this.operacion = operacion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.dao;

import java.math.BigDecimal;

/**
 *
 * @author Acer
 */
public class ResumenTransaccion {
    
   
    private String operacion;
    private String tipo;
    private BigDecimal monto;
    private Integer cantidadTransaccion;
    

    public ResumenTransaccion(String operacion, String tipo, BigDecimal monto, Integer cantidadTransaccion) {
        this.operacion = operacion;
        this.tipo = tipo;
        this.monto = monto;
        this.cantidadTransaccion = cantidadTransaccion;
    }

    public ResumenTransaccion(Object[] o) {
        
        this((String)o[0],(String) o[1],(BigDecimal) o[2], ((Long)o[3]).intValue());
        
    }

    public Integer getCantidadTransaccion() {
        return cantidadTransaccion;
    }

    public void setCantidadTransaccion(Integer cantidadTransaccion) {
        this.cantidadTransaccion = cantidadTransaccion;
    }
    
    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
    
    
}

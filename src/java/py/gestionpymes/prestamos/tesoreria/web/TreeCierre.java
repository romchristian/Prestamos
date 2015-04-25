/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.math.BigDecimal;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;

/**
 *
 * @author Acer
 */
public class TreeCierre {
    
    private String descripcion;
    private TipoTransaccion tipoTransaccion;
    private BigDecimal monto;
    private int cantidad;
    private String styleClass;
    
    private TreeNode padre;

    public TreeCierre(String descripcion, TipoTransaccion tipoTransaccion, BigDecimal monto, int cantidad, TreeNode padre,String styleClass) {
        this.descripcion = descripcion;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.cantidad = cantidad;
        this.padre = padre;
        this.styleClass = styleClass;
    }

    public TreeCierre() {
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public TreeNode getPadre() {
        return padre;
    }

    public void setPadre(TreeNode padre) {
        this.padre = padre;
    }
    
    
    
}

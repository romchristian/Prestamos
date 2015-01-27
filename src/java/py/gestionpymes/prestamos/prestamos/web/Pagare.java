/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.util.Date;
import py.gestionpymes.prestamos.adm.web.util.NumeroALetras;

/**
 *
 * @author Acer
 */
public class Pagare {
    private int nro;
    private int cantidadPagares = 1;
    private Double monto;
    private Date vencimiento;
    private String empresaNombre;
    private String empresaRuc;
    private Date fechaEmision;
    private String deudor;
    private String deudorDoc;
    private String deudorDireccion;
    private String deudorConyuge;
    private String deudorConyugeDoc;
    
    private String coDeudor;
    private String coDeudorDoc;
    private String coDeudorDireccion;
    private String coDeudorConyuge;
    private String coDeudorConyugeDoc;
    
    //Calulados
    private String montoLetras;
    private String dia;
    private int mesNumero;
    private String mesLetra;
    private int anio;
    

    public Pagare(Double monto) {
        this.monto = monto;
    }
    
    

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public int getCantidadPagares() {
        return cantidadPagares;
    }

    public void setCantidadPagares(int cantidadPagares) {
        this.cantidadPagares = cantidadPagares;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }

    public String getEmpresaRuc() {
        return empresaRuc;
    }

    public void setEmpresaRuc(String empresaRuc) {
        this.empresaRuc = empresaRuc;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDeudor() {
        return deudor;
    }

    public void setDeudor(String deudor) {
        this.deudor = deudor;
    }

    public String getDeudorDoc() {
        return deudorDoc;
    }

    public void setDeudorDoc(String deudorDoc) {
        this.deudorDoc = deudorDoc;
    }

    public String getDeudorDireccion() {
        return deudorDireccion;
    }

    public void setDeudorDireccion(String deudorDireccion) {
        this.deudorDireccion = deudorDireccion;
    }

    public String getDeudorConyuge() {
        return deudorConyuge;
    }

    public void setDeudorConyuge(String deudorConyuge) {
        this.deudorConyuge = deudorConyuge;
    }

    public String getDeudorConyugeDoc() {
        return deudorConyugeDoc;
    }

    public void setDeudorConyugeDoc(String deudorConyugeDoc) {
        this.deudorConyugeDoc = deudorConyugeDoc;
    }

    public String getCoDeudor() {
        return coDeudor;
    }

    public void setCoDeudor(String coDeudor) {
        this.coDeudor = coDeudor;
    }

    public String getCoDeudorDoc() {
        return coDeudorDoc;
    }

    public void setCoDeudorDoc(String coDeudorDoc) {
        this.coDeudorDoc = coDeudorDoc;
    }

    public String getCoDeudorDireccion() {
        return coDeudorDireccion;
    }

    public void setCoDeudorDireccion(String coDeudorDireccion) {
        this.coDeudorDireccion = coDeudorDireccion;
    }

    public String getCoDeudorConyuge() {
        return coDeudorConyuge;
    }

    public void setCoDeudorConyuge(String coDeudorConyuge) {
        this.coDeudorConyuge = coDeudorConyuge;
    }

    public String getCoDeudorConyugeDoc() {
        return coDeudorConyugeDoc;
    }

    public void setCoDeudorConyugeDoc(String coDeudorConyugeDoc) {
        this.coDeudorConyugeDoc = coDeudorConyugeDoc;
    }

    public String getMontoLetras() {
        montoLetras = numeroALetras(monto);
        return montoLetras;
    }

    public void setMontoLetras(String montoLetras) {
        this.montoLetras = montoLetras;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getMesNumero() {
        return mesNumero;
    }

    public void setMesNumero(int mesNumero) {
        this.mesNumero = mesNumero;
    }

    public String getMesLetra() {
        return mesLetra;
    }

    public void setMesLetra(String mesLetra) {
        this.mesLetra = mesLetra;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    private String numeroALetras(Double num){
        
        System.out.println("");
        return new NumeroALetras().Convertir(num.intValue()+"", false);
    }
    
      
}

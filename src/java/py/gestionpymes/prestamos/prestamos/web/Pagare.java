/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;
import py.gestionpymes.prestamos.adm.web.util.NumeroALetras;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author Acer
 */
public class Pagare {

    private Prestamo prestamo;
    private int nro=1;
    private int cantidadPagares = 1;
    private BigDecimal monto;
    private Date vencimiento;
    private String empresaNombre;
    private String empresaRuc;
    private Date fechaEmision;
    private String fechaEmisionTexto;
    private String deudor;
    private String deudorDoc;
    private String deudorDireccion;
    private String deudorCiudad;
    private String deudorConyuge;
    private String deudorConyugeDoc;

    private String coDeudor;
    private String coDeudorDoc;
    private String coDeudorDireccion;
    private String coDeudorCiudad;
    private String coDeudorConyuge;
    private String coDeudorConyugeDoc;

    //Calulados
    private String montoLetras;
    private int dia;
    private int mesNumero;
    private String mesLetra;
    private int anio;

    public Pagare() {

    }

    public Pagare(Prestamo prestamo) {
        this.prestamo = prestamo;
        
        

        monto = this.prestamo.getTotalOperacion().setScale(0, RoundingMode.HALF_EVEN);
        setMontoLetras(this.prestamo.getTotalOperacion().setScale(0, RoundingMode.HALF_EVEN));
        fechaEmision = this.prestamo.getFechaInicioOperacion();
        setFechaEmisionTexto(this.prestamo.getFechaInicioOperacion());
        DateTime d = new DateTime(this.prestamo.getFechaInicioOperacion());
        setAnio(d.getYear());
        setMesNumero(d.getMonthOfYear());
        setMesLetra(d.toString("MMMM", new Locale("es", "py")));
        setDia(d.getDayOfMonth());

        setEmpresaNombre(this.prestamo.getEmpresa() == null ? null : this.prestamo.getEmpresa().getRazonSocial());
        setEmpresaRuc(this.prestamo.getEmpresa() == null ? null : this.prestamo.getEmpresa().getRuc());

        setDeudor(this.prestamo.getCliente() == null ? null : this.prestamo.getCliente().devuelveNombreCompleto());
        setDeudorDoc(this.prestamo.getCliente() == null ? null : this.prestamo.getCliente().getNroDocumento());
        setDeudorDireccion(this.prestamo.getCliente() == null ? null : this.prestamo.getCliente().devuelveDireccionParticular());
        setDeudorCiudad(this.prestamo.getCliente() == null ? null : this.prestamo.getCliente().devuelveCiudadParticular());
        if (prestamo.isFirmaConyugeTitular()) {
            setDeudorConyuge(this.prestamo.getCliente().getConyuge() == null ? null : this.prestamo.getCliente().getConyuge().devuelveNombreCompleto());
            setDeudorConyugeDoc(this.prestamo.getCliente().getConyuge() == null ? null : this.prestamo.getCliente().getConyuge().getNroDocumento());
        }

        setCoDeudor(this.prestamo.getCodeudor() == null ? null : this.prestamo.getCodeudor().devuelveNombreCompleto());
        setCoDeudorDoc(this.prestamo.getCodeudor() == null ? null : this.prestamo.getCodeudor().getNroDocumento());
        setCoDeudorDireccion(this.prestamo.getCodeudor() == null ? null : this.prestamo.getCodeudor().devuelveDireccionParticular());
        setCoDeudorCiudad(this.prestamo.getCodeudor() == null ? null : this.prestamo.getCodeudor().devuelveCiudadParticular());
        if (prestamo.isFirmaConyugeCodeudor()) {
            setCoDeudorConyuge(this.prestamo.getCodeudor().getConyuge() == null ? null : this.prestamo.getCodeudor().getConyuge().devuelveNombreCompleto());
            setCoDeudorConyugeDoc(this.prestamo.getCodeudor().getConyuge() == null ? null : this.prestamo.getCodeudor().getConyuge().getNroDocumento());
        }
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
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

    public String getFechaEmisionTexto() {
        return fechaEmisionTexto;
    }

    public void setFechaEmisionTexto(Date d) {

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        this.fechaEmisionTexto = dateFormat.format(d);
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

    public String getDeudorCiudad() {
        return deudorCiudad;
    }

    public void setDeudorCiudad(String deudorCiudad) {
        this.deudorCiudad = deudorCiudad;
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

    public String getCoDeudorCiudad() {
        return coDeudorCiudad;
    }

    public void setCoDeudorCiudad(String coDeudorCiudad) {
        this.coDeudorCiudad = coDeudorCiudad;
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
        return montoLetras;
    }

    public void setMontoLetras(BigDecimal monto) {
        this.montoLetras = numeroALetras(monto);
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
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

    private String numeroALetras(BigDecimal num) {

        System.out.println("");
        return new NumeroALetras().Convertir(num.intValue() + "", true);
    }

}

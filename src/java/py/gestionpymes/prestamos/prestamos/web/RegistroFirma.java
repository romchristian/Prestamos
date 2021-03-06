/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import py.gestionpymes.prestamos.prestamos.modelo.Cliente;

/**
 *
 * @author Acer
 */
public class RegistroFirma {
    
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nroDocumento;
    private String direccion;
    private String barrio;
    private String ciudad;
    
    
    private Cliente cliente;

    public RegistroFirma(Cliente cliente) {
        this.cliente = cliente;
        
        primerNombre = this.cliente.getPrimerNombre();
        segundoNombre = this.cliente.getSegundoNombre(); 
        primerApellido = this.cliente.getPrimerApellido();
        segundoApellido = this.cliente.getSegundoApellido();
        nroDocumento = this.cliente.getNroDocumento();
        direccion = this.cliente.devuelveDireccionParticular();
        barrio = this.cliente.devuelveBarrioParticular();
        ciudad = this.cliente.devuelveCiudadParticular();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String sgundoNombre) {
        this.segundoNombre = sgundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
    
}

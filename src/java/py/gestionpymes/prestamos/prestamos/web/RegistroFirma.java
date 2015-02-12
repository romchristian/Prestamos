/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;

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
    
    private Cliente cliente;

    public RegistroFirma(Cliente cliente) {
        this.cliente = cliente;
        
        primerNombre = this.cliente.getPrimerNombre();
        segundoNombre = this.cliente.getSegundoNombre(); 
        primerApellido = this.cliente.getPrimerApellido();
        segundoApellido = this.cliente.getSegundoApellido();
        nroDocumento = this.cliente.getNroDocumento();
        direccion = this.cliente.devuelveDireccionParticular();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

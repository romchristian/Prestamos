/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia;

import java.io.Serializable;
import javax.persistence.Entity;
import py.gestionpymes.prestamos.adm.persistencia.Persona;

/**
 *
 * @author Acer
 */
@Entity
public class Vendedor extends Persona implements Serializable{
    private String nombres;
    private String apellidos;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String devuelveNombreCompleto(){
        
        return getNombres()+" "+getApellidos();
    }

    @Override
    public String toString() {
        return devuelveNombreCompleto();
    }
    
    
}

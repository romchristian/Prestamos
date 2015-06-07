/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.tesoreria.modelo.SesionTPV;

/**
 *
 * @author Acer
 */
@Named
@SessionScoped
public class SesionTPVActual implements Serializable{
    private SesionTPV sesion;

    public SesionTPV getSesion() {
        return sesion;
    }

    public void setSesion(SesionTPV sesion) {
        this.sesion = sesion;
    }

    
    
}

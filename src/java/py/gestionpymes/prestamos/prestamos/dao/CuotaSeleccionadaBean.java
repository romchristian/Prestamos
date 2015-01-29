/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;

/**
 *
 * @author Acer
 */
@Named
@SessionScoped
public class CuotaSeleccionadaBean implements Serializable{
    
   private TreeCuota cuotaSeleccionada;

    public TreeCuota getCuotaSeleccionada() {
        return cuotaSeleccionada;
    }

    public void setCuotaSeleccionada(TreeCuota cuotaSeleccionada) {
        this.cuotaSeleccionada = cuotaSeleccionada;
    }
   
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo;

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;



/**
 *
 * @author acer
 */
@Named
@SessionScoped
public class CompDetDirecciones implements Serializable {

    private Direccion direccion;
    private List<Direccion> direcciones;

    @PostConstruct
    private void init() {
        inicializa();
    }

    public List<Direccion> getFamilias() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void agrega() {
        direcciones.add(direccion);
    }

    public void remover(Direccion d) {
        direcciones.remove(d);
    }

    public void inicializa() {
        direcciones = new ArrayList<Direccion>();
    }
}

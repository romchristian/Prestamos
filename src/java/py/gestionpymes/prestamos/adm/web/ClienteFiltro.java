/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web;

/**
 *
 * @author Acer
 */
public class ClienteFiltro {
    private String nroDoc;
    private String nombre;
    private boolean buscarPorDoc =true; 

    public boolean isBuscarPorDoc() {
        return buscarPorDoc;
    }

    public void setBuscarPorDoc(boolean buscarPorDoc) {
        this.buscarPorDoc = buscarPorDoc;
    }

    
    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}

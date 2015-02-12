/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.CobradorFacade;
import py.gestionpymes.prestamos.adm.persistencia.Cobrador;


/**
 *
 * @author cromero
 */
@Named
@SessionScoped
public class AutoCompleteCobrador implements Serializable {

    private Cobrador elegido;
    @EJB
    private CobradorFacade dao;
    private List<Cobrador> lista;

    @PostConstruct
    public void init() {
        carga();
    }

    public Cobrador getElegido() {
        return elegido;
    }

    public void setElegido(Cobrador elegido) {
        this.elegido = elegido;
    }

    public List<Cobrador> completar(String query) {
        List<Cobrador> sugerencias = new ArrayList<>();

        for (Cobrador p : getLista()) {
            
            String cadenaAComparar = p.getNroDocumento()+ " " + p.toString();
            if (cadenaAComparar.trim().toUpperCase().contains(query.toUpperCase().trim())) {
                sugerencias.add(p);
            }
        }

        return sugerencias;
    }
    
    public List<Cobrador> getLista() {
        if(lista == null || lista.isEmpty()){
            carga();
        }
        return lista;
    }

    public void setLista(List<Cobrador> lista) {
        this.lista = lista;
    }
    
    public void carga(){
        lista = dao.findAll();
    }

}

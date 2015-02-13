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
import py.gestionpymes.prestamos.adm.dao.VendedorFacade;
import py.gestionpymes.prestamos.adm.persistencia.Vendedor;


/**
 *
 * @author cromero
 */
@Named
@SessionScoped
public class AutoCompleteVendedor implements Serializable {

    private Vendedor elegido;
    @EJB
    private VendedorFacade dao;
    private List<Vendedor> lista;

    @PostConstruct
    public void init() {
        carga();
    }

    public Vendedor getElegido() {
        return elegido;
    }

    public void setElegido(Vendedor elegido) {
        this.elegido = elegido;
    }

    public List<Vendedor> completar(String query) {
        List<Vendedor> sugerencias = new ArrayList<>();

        for (Vendedor p : getLista()) {
            
            String cadenaAComparar = p.getNroDocumento()+ " " + p.devuelveNombreCompleto();
            if (cadenaAComparar.trim().toUpperCase().contains(query.toUpperCase().trim())) {
                sugerencias.add(p);
            }
        }

        return sugerencias;
    }
    
    public List<Vendedor> getLista() {
        if(lista == null || lista.isEmpty()){
            carga();
        }
        return lista;
    }

    public void setLista(List<Vendedor> lista) {
        this.lista = lista;
    }
    
    public void carga(){
        lista = dao.findAll();
    }

}

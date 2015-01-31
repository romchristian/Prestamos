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
import py.gestionpymes.prestamos.adm.dao.ClienteFacade;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;

/**
 *
 * @author cromero
 */
@Named
@SessionScoped
public class AutoCompleteCliente implements Serializable {

    private Cliente elegido;
    @EJB
    private ClienteFacade dao;
    private List<Cliente> lista;

    @PostConstruct
    public void init() {
        carga();
    }

    public Cliente getElegido() {
        return elegido;
    }

    public void setElegido(Cliente elegido) {
        this.elegido = elegido;
    }

    public List<Cliente> completar(String query) {
        List<Cliente> sugerencias = new ArrayList<>();

        for (Cliente p : getLista()) {
            
            String cadenaAComparar = p.getNroDocumento()+ " " + p.devuelveNombreCompleto();
            if (cadenaAComparar.trim().toUpperCase().contains(query.toUpperCase().trim())) {
                sugerencias.add(p);
            }
        }

        return sugerencias;
    }
    
    public List<Cliente> getLista() {
        if(lista == null || lista.isEmpty()){
            carga();
        }
        return lista;
    }

    public void setLista(List<Cliente> lista) {
        this.lista = lista;
    }
    
    public void carga(){
        lista = dao.findAll();
    }

}

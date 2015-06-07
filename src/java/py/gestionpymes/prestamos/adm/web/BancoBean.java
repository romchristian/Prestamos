/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web;


import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.dao.BancoDAO;
import py.gestionpymes.prestamos.adm.modelo.Banco;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class BancoBean extends BeanGenerico<Banco> implements Serializable{

    @EJB private BancoDAO ejb;
    
    
    @Override
    public AbstractDAO<Banco> getEjb() {
        return ejb;
    }

    @Override
    public Banco getNuevo() {
        return new Banco();
    }

    
}

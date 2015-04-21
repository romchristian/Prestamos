/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.tesoreria.dao.BancoDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Banco;


/**
 *
 * @author christian
 */
@ManagedBean
@RequestScoped
public class BancoBean extends BeanGenerico<Banco>{

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

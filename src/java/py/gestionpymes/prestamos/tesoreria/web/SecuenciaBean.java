/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.finanza.modelo.CuentaBancaria;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;
import py.gestionpymes.prestamos.tesoreria.dao.SecuenciaDAO;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class SecuenciaBean extends BeanGenerico<Secuencia> {

    @EJB
    private SecuenciaDAO ejb;
    List<Secuencia> chequeras;

    @Override
    public AbstractDAO<Secuencia> getEjb() {
        return ejb;
    }

    @Override
    public Secuencia getNuevo() {
        return new Secuencia();
    }

    public List<Secuencia> findAllChequeras() {
        if (chequeras == null) {
            chequeras = ejb.findAllChequeras();
        }
        return chequeras;
    }

    public SelectItem[] obtItemsAvailableSelectOne(CuentaBancaria cuentaBancaria) {
        return JsfUtil.getSelectItems(findAllChequeras(cuentaBancaria), true);
    }

    public List<Secuencia> findAllChequeras(CuentaBancaria cuentaBancaria) {
        if (cuentaBancaria != null) {
            return ejb.findAllChequeras(cuentaBancaria);
        } else {
            return new ArrayList<>();
        }
    }

    
}

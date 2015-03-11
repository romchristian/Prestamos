/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.servicio.MetodoPagoDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorMoneda;

/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class MetodoPagoBean extends BeanGenerico<MetodoPago> implements Serializable {

    @EJB
    private MetodoPagoDAO ejb;
    private ValorMoneda valorMoneda;

    @Override
    public AbstractDAO<MetodoPago> getEjb() {
        return ejb;
    }

    @Override
    public MetodoPago getNuevo() {
        return new MetodoPago();
    }

    public ValorMoneda getValorMoneda() {
        if (valorMoneda == null) {
            valorMoneda = new ValorMoneda();
        }
        return valorMoneda;
    }

    public void setValorMoneda(ValorMoneda valorMoneda) {
        this.valorMoneda = valorMoneda;
    }

    public String reinit() {
        valorMoneda = new ValorMoneda();
        return null;
    }

    public void agregar() {
        if (getActual().getValoresMonedas() == null) {
            getActual().setValoresMonedas(new ArrayList<ValorMoneda>());
        }

        valorMoneda.setMetodoPago(getActual());
        getActual().getValoresMonedas().add(valorMoneda);

        valorMoneda = new ValorMoneda();
    }

    public void quitarValorMoneda(ValorMoneda v) {
        int indice = 0;
        int o2 = v.getDenominacion();
        for (ValorMoneda va : getActual().getValoresMonedas()) {
            
            int o1 = va.getDenominacion();
            System.out.println("o1: " + o1);
            System.out.println("o2: " + o2);
            System.out.println("Comparacion: " + (o1 == o2));
            if (o1 == o2) {
                System.out.println("Indice 1: " + indice);
                
                System.out.println("Indice 2: " + indice);
                break;
            }
            
            indice++;
        }

        System.out.println("Indice 3: " + indice);
        getActual().getValoresMonedas().remove(indice);

    }
}

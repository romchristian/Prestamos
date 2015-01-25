/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web.util;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


/**
 *
 * @author christian
 */
@Named
@ApplicationScoped
public class Formato implements Serializable {

    private Locale locale;
    private String patronFecha = "dd/MM/yyyy";
    private String patronFechaHora = "dd/MM/yyyy hh:mm";
    

    @PostConstruct
    private void init() {
        locale = new Locale("es", "py");
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getPatronFecha() {
        return patronFecha;
    }

    public void setPatronFecha(String patronFecha) {
        this.patronFecha = patronFecha;
    }

    public String getPatronFechaHora() {
        return patronFechaHora;
    }

    public void setPatronFechaHora(String patronFechaHora) {
        this.patronFechaHora = patronFechaHora;
    }
}

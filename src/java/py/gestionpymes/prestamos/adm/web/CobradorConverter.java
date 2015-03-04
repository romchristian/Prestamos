/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import py.gestionpymes.prestamos.adm.persistencia.Cobrador;

/**
 *
 * @author cromero
 */
public class CobradorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        CobradorController controller = (CobradorController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "cobradorController");
        return controller.getCobrador(getKey(value));
    }

    java.lang.Long getKey(String value) {
        java.lang.Long key;
         try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0L;
            }
       
        return key;
    }

    String getStringKey(java.lang.Long value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Cobrador) {
            Cobrador o = (Cobrador) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cobrador.class.getName()});
            return null;
        }
    }

}

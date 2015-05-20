/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import py.gestionpymes.prestamos.finanza.persistencia.CuentaBancaria;
import py.gestionpymes.prestamos.finanza.web.CuentaBancariaBean;


/**
 *
 * @author christian
 */
@FacesConverter(forClass = CuentaBancaria.class)
public class CuentaBancariaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
          CuentaBancariaBean controller = (CuentaBancariaBean) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cuentaBancariaBean");
        return controller.find(getKey(value));
    }

    java.lang.Long getKey(String value) {
        java.lang.Long key;
        key = Long.valueOf(value);
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
        if (object instanceof CuentaBancaria) {
            CuentaBancaria o = (CuentaBancaria) object;
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CuentaBancaria.class.getName());
        }
    }
}

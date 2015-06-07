/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo.validadores;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoDocumento;

/**
 *
 * @author cromero
 */
@FacesValidator("validadorCI")
public class ValidadorCI implements Validator {

    @EJB
    private ValidadoresDAO ejb;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (ejb.hayDocumento((String) value, TipoDocumento.CI)) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La cédula " + ((String) value) + " ya existe!",
                    "La cédula " + ((String) value) + " ya existe!");
            

            throw new ValidatorException(message);
        }
    }

}

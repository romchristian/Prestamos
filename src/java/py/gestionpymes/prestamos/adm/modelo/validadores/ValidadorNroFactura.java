/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo.validadores;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author cromero
 */
@FacesValidator("validadorNroFactura")
public class ValidadorNroFactura implements Validator {

    @EJB
    private ValidadoresDAO ejb;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        UIInput uiInputTimbrado = (UIInput) component.getAttributes().get("timbrado");
        UIInput uiInputCodEstablecimiento = (UIInput) component.getAttributes().get("codEstablecimiento");
        UIInput uiInputPuntoExpedicion = (UIInput) component.getAttributes().get("puntoExpedicion");

        System.out.println("uiInputTimbrado: " + uiInputTimbrado);
        System.out.println("uiInputCodEstablecimiento: " + uiInputCodEstablecimiento);
        System.out.println("uiInputPuntoExpedicion: " + uiInputPuntoExpedicion);
        
        String timbrado = uiInputTimbrado == null?null: uiInputTimbrado.getValue()+"";
        String codEstablecimiento = uiInputCodEstablecimiento== null?null:uiInputCodEstablecimiento.getValue()+"";
        String puntoExpedicion = uiInputPuntoExpedicion== null?null:uiInputPuntoExpedicion.getValue()+"";
        String numero = (String) value;
        String desc = timbrado +": " + codEstablecimiento+"-"+puntoExpedicion+"-"+numero;

        System.out.println("Desc: " + desc);
        
        if (ejb.hayNroFactura(timbrado, codEstablecimiento, puntoExpedicion, numero)) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Factura: " + desc + " ya existe!",
                    "La Factura: " + desc + " ya existe!");

            throw new ValidatorException(message);
        }
    }

}

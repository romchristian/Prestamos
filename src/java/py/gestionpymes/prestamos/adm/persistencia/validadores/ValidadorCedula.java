/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.persistencia.validadores;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoDocumento;


/**
 *
 * @author christian
 */
@Named
@Stateless
public class ValidadorCedula implements Serializable {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;
   

    public void validateCedula(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (hayDocumento((String) value, TipoDocumento.CI)) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"La cédula " + ((String) value) + " ya existe!",
                    "La cédula " + ((String) value) + " ya existe!");
            
            throw new ValidatorException(message);
        }
    }

    public boolean hayDocumento(String nro, TipoDocumento tipoDocumento) {

        boolean R = false;
        try {

            Cliente c = (Cliente) em.createQuery("select c from Cliente c where c.nroDocumento = ?1 and c.tipoDocumento = ?2 ").
                    setParameter(1, nro).
                    setParameter(2, tipoDocumento).
                    getSingleResult();
            R = true;
        } catch (Exception e) {
        }
        return R;
    }

    public boolean hayDocumento(Cliente cliente) {
        return hayDocumento(cliente.getNroDocumento(), cliente.getTipoDocumento());
    }

}

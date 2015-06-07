/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.modelo.validadores;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoDocumento;

/**
 *
 * @author christian
 */
@Named
@Stateless
public class ValidadoresDAO implements Serializable {

    @PersistenceContext(unitName = "PrestamosPU")
    private EntityManager em;

    public boolean hayNroFactura(String nroTimbrado, String nroEstablecimiento, String nroPuntoExpedicion, String nro) {

        boolean R = false;
        try {

            FacturaVenta f = (FacturaVenta) em.createQuery("SELECT f FROM FacturaVenta f where f.timbrado = :t and f.codEstablecimiento = :codEst and f.puntoExpedicion = :pexp and f.numero = :nro")
                    .setParameter("t", nroTimbrado)
                    .setParameter("codEst", nroEstablecimiento)
                    .setParameter("pexp", nroPuntoExpedicion)
                    .setParameter("nro", nro)
                    .getSingleResult();
            R = true;
        } catch (Exception e) {
        }
        return R;
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

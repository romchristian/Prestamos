/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import javax.inject.Named;
import org.apache.commons.beanutils.converters.DateConverter;
import org.joda.time.DateTime;
import org.joda.time.JodaTimePermission;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil.PersistAction;
import static py.gestionpymes.prestamos.adm.web.util.MesEnTexto.fechaMesEnTexto;
import py.gestionpymes.prestamos.adm.web.util.NumeroALetras;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.reportes.jasper.ReporteController;

/**
 *
 * @author christian
 */
@Named("prestamoController")
@ViewScoped
public class PrestamoController implements Serializable {

    @EJB
    private py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO ejbFacade;
    private List<Prestamo> items = null;
    private Prestamo selected;
    private long id;
    @Inject
    private ReporteController reporteController;
    private Pagare pagare;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void cargaDatos() {
        if (id > 0) {
            selected = getPrestamo(id);
        }

    }

    public String calcular() {
        selected.setSistema(null);
        selected.setDetalles(null);
        selected.getDetalles();
        return null;
    }

    public Pagare getPagare() {
        return pagare;
    }

    public void setPagare(Pagare pagare) {
        this.pagare = pagare;
    }

    public void imprimePagare() {

        pagare = new Pagare(selected);

       

        List<Pagare> data = new ArrayList<>();
        data.add(pagare);

        reporteController.generaPDF(new HashMap(), data, "reportes/prestamos/pagares.jasper");
    }

    public void desembolsa() {
        ejbFacade.desembolsa(selected);
    }

    public void confirmaPagare() {
        selected.setFirmaPagare(true);
        selected.setEstado(EstadoPrestamo.EN_DESEMBOLSO);
        ejbFacade.edit(selected);
    }

    public void anulaConfimacion() {
        selected.setFirmaPagare(false);
        selected.setEstado(EstadoPrestamo.PENDIENTE_DESEMBOLSO);
        ejbFacade.edit(selected);
    }

    public PrestamoController() {
    }

    public Prestamo getSelected() {
        if (selected == null) {
            selected = new Prestamo();
        }
        return selected;
    }

    public void setSelected(Prestamo selected) {
        this.selected = selected;
    }

    public boolean haySeleccion() {
        return getSelected().getId() != null;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PrestamoDAO getFacade() {
        return ejbFacade;
    }

    public String prepareCreate() {
        selected = new Prestamo();
        initializeEmbeddableKey();
        return "Create.xhtml?faces-redirect=true";
    }

    public String create() {
        persist(PersistAction.CREATE, "El prestamo se creo EXITOSAMENTE!");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }

        return "List.xhtml?faces-redirect=true";
    }

    public void update() {
        persist(PersistAction.UPDATE, "El prestamo se actualizo se creO EXITOSAMENTE!");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "El prestamo se elimino se creO EXITOSAMENTE!");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Prestamo> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Prestamo getPrestamo(java.lang.Long id) {
        return getFacade().find(id);
    }

    
    public List<Prestamo> getPrestamosADesembolsar() {
        return getFacade().findAllEstado(EstadoPrestamo.EN_DESEMBOLSO);
    }
    
    public List<Prestamo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Prestamo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Prestamo.class)
    public static class PrestamoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PrestamoController controller = (PrestamoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoriaController");
            return controller.getPrestamo(getKey(value));
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
            if (object instanceof Prestamo) {
                Prestamo o = (Prestamo) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Prestamo.class.getName()});
                return null;
            }
        }

    }

}

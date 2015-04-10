package py.gestionpymes.prestamos.prestamos.web;

import py.gestionpymes.prestamos.prestamos.persistencia.PlanGastos;
import py.gestionpymes.prestamos.prestamos.web.util.JsfUtil;
import py.gestionpymes.prestamos.prestamos.web.util.JsfUtil.PersistAction;
import py.gestionpymes.prestamos.prestamos.dao.PlanGastosFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

import py.gestionpymes.prestamos.prestamos.persistencia.DetPlanGastos;

@Named("planGastosController")
@ViewScoped
public class PlanGastosController implements Serializable {

    @EJB
    private py.gestionpymes.prestamos.prestamos.dao.PlanGastosFacade ejbFacade;
    private List<PlanGastos> items = null;
    private PlanGastos selected;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    
    public void cargaDatos(){
        selected = ejbFacade.find(id);
    }

    public PlanGastosController() {
    }

    public PlanGastos getSelected() {
        return selected;
    }

    public void setSelected(PlanGastos selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PlanGastosFacade getFacade() {
        return ejbFacade;
    }

    public PlanGastos prepareCreate() {
        selected = new PlanGastos();
        initializeEmbeddableKey();
        return selected;
    }

    public void agregaDetalle() {
        if (selected.getDetalles() == null) {
            selected.setDetalles(new ArrayList<DetPlanGastos>());
        }

        selected.getDetalles().add(new DetPlanGastos(selected));
    }

    public void remueveDetalle(DetPlanGastos d){
        selected.getDetalles().remove(d);
    }
    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PlanGastosCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        
        return "List";
    }

    public String update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PlanGastosUpdated"));
        return "List";
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PlanGastosDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PlanGastos> getItems() {
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

    public PlanGastos getPlanGastos(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PlanGastos> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PlanGastos> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PlanGastos.class)
    public static class PlanGastosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlanGastosController controller = (PlanGastosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "planGastosController");
            return controller.getPlanGastos(getKey(value));
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
            if (object instanceof PlanGastos) {
                PlanGastos o = (PlanGastos) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PlanGastos.class.getName()});
                return null;
            }
        }

    }

}

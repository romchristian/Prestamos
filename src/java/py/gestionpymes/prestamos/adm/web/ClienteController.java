package py.gestionpymes.prestamos.adm.web;

import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil.PersistAction;
import py.gestionpymes.prestamos.adm.dao.ClienteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.inject.Inject;
import py.gestionpymes.prestamos.adm.persistencia.Direccion;
import py.gestionpymes.prestamos.prestamos.persistencia.ActividadLaboral;
import py.gestionpymes.prestamos.prestamos.persistencia.ContactoTelefonico;
import py.gestionpymes.prestamos.prestamos.persistencia.ReferenciaCliente;
import py.gestionpymes.prestamos.prestamos.web.AutoCompleteCliente;
import py.gestionpymes.prestamos.prestamos.web.RegistroFirma;
import py.gestionpymes.prestamos.reportes.jasper.ReporteController;

@Named("clienteController")
@ViewScoped
public class ClienteController implements Serializable {

    @EJB
    private py.gestionpymes.prestamos.adm.dao.ClienteFacade ejbFacade;
    private List<Cliente> items = null;
    private Cliente selected;
    private Direccion direccionSeleccionada;
    private ActividadLaboral actividadLaboralSelecccionada;
    private ContactoTelefonico contactoTelefonicoSeleccionado;
    private ReferenciaCliente referenciaClienteSeleccionada;
    @Inject
    private AutoCompleteCliente autoCompleteCliente;
    @Inject
    private ReporteController reporteController;
    private RegistroFirma registroFirma;
    
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public void imprimeRegistroFirma() {

        registroFirma = new RegistroFirma(selected);

       

        List<RegistroFirma> data = new ArrayList<>();
        data.add(registroFirma);

        reporteController.generaPDF(new HashMap(), data, "reportes/clientes/registroDeFirmas.jasper","registro_firma_"+selected.getNroDocumento());
    }
    
    public void cargaDatos(){
        selected = getCliente(id);
    }

    public ReferenciaCliente getReferenciaClienteSeleccionada() {
        if (referenciaClienteSeleccionada == null) {
            referenciaClienteSeleccionada = new ReferenciaCliente();
        }

        return referenciaClienteSeleccionada;
    }

    public void setReferenciaClienteSeleccionada(ReferenciaCliente referenciaClienteSeleccionada) {
        this.referenciaClienteSeleccionada = referenciaClienteSeleccionada;
    }

    public void agregaReferenciaClienteSelecionada() {
        if (selected.getReferenciaClientes() == null) {
            selected.setReferenciaClientes(new ArrayList<ReferenciaCliente>());
        }
        referenciaClienteSeleccionada.setCliente(selected);
        selected.getReferenciaClientes().add(referenciaClienteSeleccionada);
        referenciaClienteSeleccionada = new ReferenciaCliente();
    }

    public void removerReferenciaClienteSelecionada(ReferenciaCliente a) {
        selected.getReferenciaClientes().remove(a);
    }

    public ContactoTelefonico getContactoTelefonicoSeleccionado() {

        if (contactoTelefonicoSeleccionado == null) {
            contactoTelefonicoSeleccionado = new ContactoTelefonico();
        }

        return contactoTelefonicoSeleccionado;
    }

    public void setContactoTelefonicoSeleccionado(ContactoTelefonico contactoTelefonicoSeleccionado) {
        this.contactoTelefonicoSeleccionado = contactoTelefonicoSeleccionado;
    }

    public void agregaContactoTelefonicoSeleccionado() {
        if (selected.getContactoTelefonicos() == null) {
            selected.setContactoTelefonicos(new ArrayList<ContactoTelefonico>());
        }
        contactoTelefonicoSeleccionado.setCliente(selected);
        selected.getContactoTelefonicos().add(contactoTelefonicoSeleccionado);
        contactoTelefonicoSeleccionado = new ContactoTelefonico();
    }

    public void removerContactoTelefonicoSeleccionado(ContactoTelefonico a) {
        selected.getContactoTelefonicos().remove(a);
    }

    public ActividadLaboral getActividadLaboralSelecccionada() {
        if (actividadLaboralSelecccionada == null) {
            actividadLaboralSelecccionada = new ActividadLaboral();
        }
        return actividadLaboralSelecccionada;
    }

    public void setActividadLaboralSelecccionada(ActividadLaboral actividadLaboralSelecccionada) {
        this.actividadLaboralSelecccionada = actividadLaboralSelecccionada;
    }

    public void agregaActividadLaboralSeleccionada() {
        if (selected.getActividadesLaborales() == null) {
            selected.setActividadesLaborales(new ArrayList<ActividadLaboral>());
        }
        actividadLaboralSelecccionada.setCliente(selected);
        selected.getActividadesLaborales().add(actividadLaboralSelecccionada);
        actividadLaboralSelecccionada = new ActividadLaboral();
    }

    public void removerActividadLaboralSeleccionada(ActividadLaboral a) {
        selected.getActividadesLaborales().remove(a);
    }

    public Direccion getDireccionSeleccionada() {
        if (direccionSeleccionada == null) {
            direccionSeleccionada = new Direccion();
        }
        return direccionSeleccionada;
    }

    public void setDireccionSeleccionada(Direccion direccionSeleccionada) {
        this.direccionSeleccionada = direccionSeleccionada;
    }

    public void agregaDireccion() {
        if (selected.getDirecciones() == null) {
            selected.setDirecciones(new ArrayList<Direccion>());
        }
        direccionSeleccionada.setCliente(selected);
        selected.getDirecciones().add(direccionSeleccionada);
        direccionSeleccionada = new Direccion();
    }

    public void removerDireccion(Direccion d) {
        selected.getDirecciones().remove(d);
    }

    public ClienteController() {
    }

    public Cliente getSelected() {
        
        return selected;
    }

    public void setSelected(Cliente selected) {
        this.selected = selected;
    }

    public RegistroFirma getRegistroFirma() {
        return registroFirma;
    }

    public void setRegistroFirma(RegistroFirma registroFirma) {
        this.registroFirma = registroFirma;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClienteFacade getFacade() {
        return ejbFacade;
    }

    public void prepareCreate() {
        selected = new Cliente();
      
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return "List.xhtml?faces-redirect=true";
    }

    public String update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClienteUpdated"));
        return "List.xhtml?faces-redirect=true";
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClienteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Cliente> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                autoCompleteCliente.carga();
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

    public Cliente getCliente(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Cliente> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Cliente> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
            return controller.getCliente(getKey(value));
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
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cliente.class.getName()});
                return null;
            }
        }

    }

}

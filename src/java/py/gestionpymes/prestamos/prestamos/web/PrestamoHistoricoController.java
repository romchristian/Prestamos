/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil.PersistAction;
import py.gestionpymes.prestamos.contabilidad.servicio.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoHistoricoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPlanGastos;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamoHistorico;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.PrestamoHistorico;

/**
 *
 * @author christian
 */
@Named("prestamoHistoricoController")
@ViewScoped
public class PrestamoHistoricoController implements Serializable {

    @EJB
    private py.gestionpymes.prestamos.prestamos.dao.PrestamoHistoricoDAO ejbFacade;
    @EJB
    private CobranzaDAO cobranzaDAO;
    
    private List<PrestamoHistorico> items = null;
    private PrestamoHistorico selected;
    private long id;

    private Empresa empresaFiltro;
    private Sucursal sucursalFiltro;
    private Date inicioFiltro;
    private Date finFiltro;
    private EstadoPrestamo estadoPrestamoFiltro;
    private Cliente clienteFiltro;

    private boolean buscaPorCliente;

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

    public PrestamoHistoricoController() {
    }

    public PrestamoHistorico getSelected() {
        if (selected == null) {
            selected = new PrestamoHistorico();
        }
        return selected;
    }

    public void setSelected(PrestamoHistorico selected) {
        this.selected = selected;
    }

    public boolean haySeleccion() {
        return getSelected().getId() != null;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PrestamoHistoricoDAO getFacade() {
        return ejbFacade;
    }

    public String prepareCreate() {
        selected = new PrestamoHistorico();
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
        persist(PersistAction.UPDATE, "El prestamo se actualizo se creo EXITOSAMENTE!");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "El prestamo se elimino se creo EXITOSAMENTE!");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PrestamoHistorico> getItems() {
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    
                    
                   selected =  getFacade().guardar(selected);
                    
                    getFacade().desembolsa(selected);
                    
                    for(DetPrestamoHistorico d: selected.getDetalles()){
                        d.setPrestamo(selected);
                        cobranzaDAO.create(d);
                    }
                    
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

    public PrestamoHistorico getPrestamo(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PrestamoHistorico> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PrestamoHistorico> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    public Empresa getEmpresaFiltro() {
        return empresaFiltro;
    }

    public void setEmpresaFiltro(Empresa empresaFiltro) {
        this.empresaFiltro = empresaFiltro;
    }

    public Sucursal getSucursalFiltro() {
        return sucursalFiltro;
    }

    public void setSucursalFiltro(Sucursal sucursalFiltro) {
        this.sucursalFiltro = sucursalFiltro;
    }

    public Date getInicioFiltro() {
        return inicioFiltro;
    }

    public void setInicioFiltro(Date inicioFiltro) {
        this.inicioFiltro = inicioFiltro;
    }

    public Date getFinFiltro() {
        return finFiltro;
    }

    public void setFinFiltro(Date finFiltro) {
        this.finFiltro = finFiltro;
    }

    public EstadoPrestamo getEstadoPrestamoFiltro() {
        return estadoPrestamoFiltro;
    }

    public void setEstadoPrestamoFiltro(EstadoPrestamo estadoPrestamoFiltro) {
        this.estadoPrestamoFiltro = estadoPrestamoFiltro;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente clienteFiltro) {
        this.clienteFiltro = clienteFiltro;
    }

    public boolean isBuscaPorCliente() {
        return buscaPorCliente;
    }

    public void setBuscaPorCliente(boolean buscaPorCliente) {
        this.buscaPorCliente = buscaPorCliente;
    }

    @PostConstruct
    public void init() {
        inicioFiltro = new Date();
        finFiltro = new Date();
    }

    public void buscar() {
        if (buscaPorCliente) {
            items = ejbFacade.findAllPorEmpresaFechaEstadoCliente(empresaFiltro, sucursalFiltro, estadoPrestamoFiltro, inicioFiltro, finFiltro, clienteFiltro);
        } else {
            items = ejbFacade.findAllPorEmpresaFechaEstado(empresaFiltro, sucursalFiltro, estadoPrestamoFiltro, inicioFiltro, finFiltro);
        }
    }

    
    public void pagar(DetPrestamoHistorico d){
        if(d.isPagado()){
            d.setMontoPago(d.getMontoCuota().add(d.getMontoMora() == null?new BigDecimal(BigInteger.ZERO):d.getMontoMora()));
            d.setUltimoPago(d.getFechaVencimiento());
        }else{
            d.setMontoPago(new BigDecimal(BigInteger.ZERO));
            d.setUltimoPago(null);
        }
        
    }
    public String calcular() {

        if (selected != null) {

            selected.setDetalles(new ArrayList<DetPrestamoHistorico>());

            for (int i = 0; i < selected.getPlazo(); i++) {
                DetPrestamoHistorico d = new DetPrestamoHistorico(selected, i + 1, selected.getMontoCuota());
                selected.getDetalles().add(d);
            }
        }

        selected.setTotalOperacion(selected.getMontoCuota().multiply(new BigDecimal(selected.getPlazo())));
        return null;
    }

    @FacesConverter(forClass = PrestamoHistorico.class)
    public static class PrestamoHistoricoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PrestamoHistoricoController controller = (PrestamoHistoricoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "prestamoHistoricoController");
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
            if (object instanceof PrestamoHistorico) {
                PrestamoHistorico o = (PrestamoHistorico) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PrestamoHistorico.class.getName()});
                return null;
            }
        }

    }

}

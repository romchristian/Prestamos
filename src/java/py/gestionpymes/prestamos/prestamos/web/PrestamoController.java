/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import com.sun.faces.config.WebConfiguration;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.inject.Inject;

import javax.inject.Named;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil.PersistAction;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPlanGastos;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
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
    private DetPrestamo detPrestamo;

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

    public boolean isBuscaPorCliente() {
        return buscaPorCliente;
    }

    public void setBuscaPorCliente(boolean buscaPorCliente) {
        this.buscaPorCliente = buscaPorCliente;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente clienteFiltro) {
        this.clienteFiltro = clienteFiltro;
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

    @PostConstruct
    public void init() {
        inicioFiltro = new Date();
        finFiltro = new Date();
    }

    public void ajustar() {
        selected.setSistema(null);
        selected.setDetalles(null);
        selected.calcula();
    }

    public String calcular() {
        if (selected.getPlanGastos() != null) {
            DetPlanGastos detPlan = null;
            int periocidad = 360;
            switch (selected.getPeriodoPago()) {
                case QUINCENAL:
                    periocidad = 15;
                    break;
                case SEMANAL:
                    periocidad = 7;
                    break;
                case DIARIO:
                    periocidad = 30;
                    break;
            }

            int plazosdias = selected.getPlazo() * periocidad;//22-03-2015 modifique para poder calcular bien los días, no traía el PanGastos por no calcular bien los días.
            int i = 0;
            for (DetPlanGastos d : selected.getPlanGastos().getDetalles()) {
                if (d.getPlazo() == plazosdias) {
                    detPlan = d;
                    break;
                }
                i++;
            }
            if (detPlan != null) {
                selected.setTasa(detPlan.getTasa());
                if (selected.getMontoPrestamo() != null) {
                    selected.setGastos(selected.getMontoPrestamo().multiply(detPlan.getPorcentanjeGastos().divide(new BigDecimal(100))).setScale(6, RoundingMode.HALF_EVEN));
                    selected.setComisiones(selected.getMontoPrestamo().multiply(detPlan.getPorcentanjeComision().divide(new BigDecimal(100))).setScale(6, RoundingMode.HALF_EVEN));
                }
            }
            selected.setSistema(null);
            selected.setDetalles(null);
            selected.calcula();

            BigDecimal _totalOperacion_teorica = selected.getMontoCuota().multiply(new BigDecimal(selected.getPlazo()));
            BigDecimal _diffTotaloperacion = new BigDecimal(BigInteger.ZERO);
//
//            if (_totalOperacion_teorica != selected.getTotalOperacion()) {
//
//                _diffTotaloperacion = _totalOperacion_teorica.subtract(selected.getTotalOperacion());
//
//                selected.setComisiones(selected.getComisiones().add(_diffTotaloperacion));
//
//                //Recalculo con gastos modificados
//                selected.setSistema(null);
//                selected.setDetalles(null);
//                selected.calcula();
//            }

        }

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

        reporteController.generaPDF(new HashMap(), data, "reportes/prestamos/pagares.jasper", "pagare_" + selected.getCliente().getNroDocumento());
    }

    public void imprimeLiquidacionPrestamo() {

        List<LiquidacionPrestamo> data = new ArrayList<>();

        for (DetPrestamo dp : selected.getDetalles()) {
            data.add(new LiquidacionPrestamo(dp));
        }

        Comparator<LiquidacionPrestamo> comp = new Comparator<LiquidacionPrestamo>() {

            @Override
            public int compare(LiquidacionPrestamo o1, LiquidacionPrestamo o2) {
                return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
            }
        };

        Collections.sort(data, comp);
        DateTime dateTimeOpercion = new DateTime(selected.getFechaInicioOperacion());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

        DateTime dateTimeVencimiento = new DateTime(selected.getFechaPrimerVencimiento());
        DateTimeFormatter fmtVenc = DateTimeFormat.forPattern("dd/MM/yyyy");

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

        Map<String, String> params = new HashMap<String, String>();
        params.put("nroOperacion", nf.format(selected.getId()));//nf.format(selected.getId())
        params.put("empresa", selected.getEmpresa() == null ? " " : selected.getEmpresa().getRazonSocial());
        params.put("sucursal", selected.getSucursal() == null ? " " : selected.getSucursal().getNombre());
        params.put("vendedor", selected.getVendedor() == null ? " " : selected.getVendedor().devuelveNombreCompleto());
        params.put("nombreCliente", selected.getCliente() == null ? " " : selected.getCliente().devuelveNombreCompleto());
        params.put("codeudor", selected.getCodeudor() == null ? "" : selected.getCodeudor().devuelveNombreCompleto());
        params.put("montoPrestamo", nf.format(selected.getMontoPrestamo()));
        params.put("capital", nf.format(selected.getCapital()));
        params.put("plazo", nf.format(selected.getPlazo()));
        params.put("tasa", nf.format(selected.getTasa()));
        params.put("periodoPago", selected.getPeriodoPago().name());
        params.put("sistemaAmortizacion", selected.getSistemaAmortizacion().name());
        params.put("fechaOperacion", fmt.print(dateTimeOpercion));
        params.put("fechaPrimerVencimiento", fmt.print(dateTimeVencimiento));
        params.put("periodoPago", selected.getPeriodoPago().name());
        params.put("gastos", nf.format(selected.getGastos()));
        params.put("comisiones", nf.format(selected.getComisiones()));
        params.put("impuestoIVA", nf.format(selected.getImpuestoIVA()));
        params.put("montoDeCuota", nf.format(selected.getMontoCuota().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("totalIntereses", nf.format(selected.getTotalIntereses()));
        params.put("totalOperacion", nf.format(selected.getTotalOperacion()));
        params.put("moneda", selected.getMoneda() == null ? "" : selected.getMoneda().getNombre());
        params.put("firmaConyugeTitular", selected.isFirmaConyugeTitular() == false ? "no" : "si");
        params.put("firmaConyugeCodeudor", selected.isFirmaConyugeCodeudor() == false ? "no" : "si");

        reporteController.generaPDF(params, data, "reportes/prestamos/liquidacion.jasper", "liquidacion_prestamo_" + selected.getCliente().getNroDocumento());
    }

    public void imprimeDetalleParaCliente() {

        List<LiquidacionPrestamo> data = new ArrayList<>();

        for (DetPrestamo dp : selected.getDetalles()) {
            data.add(new LiquidacionPrestamo(dp));
        }

        Comparator<LiquidacionPrestamo> comp = new Comparator<LiquidacionPrestamo>() {

            @Override
            public int compare(LiquidacionPrestamo o1, LiquidacionPrestamo o2) {
                return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
            }
        };

        Collections.sort(data, comp);
        DateTime dateTimeOpercion = new DateTime(selected.getFechaInicioOperacion());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

        DateTime dateTimeVencimiento = new DateTime(selected.getFechaPrimerVencimiento());
        DateTimeFormatter fmtVenc = DateTimeFormat.forPattern("dd/MM/yyyy");

        int diaVencimiento = dateTimeVencimiento.getDayOfMonth();

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

        Map<String, String> params = new HashMap<String, String>();
        params.put("nroOperacion", nf.format(selected.getId()));//nf.format(selected.getId())
        //params.put("empresa", selected.getEmpresa() == null ? " " : selected.getEmpresa().getRazonSocial());
        //params.put("sucursal", selected.getSucursal() == null ? " " : selected.getSucursal().getNombre());
        //params.put("vendedor", selected.getVendedor() == null ? " " : selected.getVendedor().devuelveNombreCompleto());
        params.put("nombreCliente", selected.getCliente() == null ? " " : selected.getCliente().devuelveNombreCompleto());
        params.put("nroDocumento", selected.getCliente() == null ? " " : selected.getCliente().getNroDocumento());
        //params.put("codeudor", selected.getCodeudor() == null ? "" : selected.getCodeudor().devuelveNombreCompleto());
        params.put("montoPrestamo", nf.format(selected.getMontoPrestamo().setScale(0, RoundingMode.HALF_EVEN)));
        //params.put("capital", nf.format(selected.getCapital()));
        params.put("plazo", nf.format(selected.getPlazo()));
        params.put("diaVencimiento", nf.format(diaVencimiento));
        //params.put("periodoPago", selected.getPeriodoPago().name());
        //params.put("sistemaAmortizacion", selected.getSistemaAmortizacion().name());
        params.put("fechaOperacion", fmt.print(dateTimeOpercion));
        //params.put("fechaPrimerVencimiento", fmt.print(dateTimeVencimiento));
        //params.put("periodoPago", selected.getPeriodoPago().name());
        //params.put("gastos", nf.format(selected.getGastos()));
        //params.put("comisiones", nf.format(selected.getComisiones()));
        //params.put("impuestoIVA", nf.format(selected.getImpuestoIVA()));
        params.put("montoDeCuota", nf.format(selected.getMontoCuota().setScale(0, RoundingMode.HALF_EVEN)));
        //params.put("totalIntereses", nf.format(selected.getTotalIntereses()));
        //params.put("totalOperacion", nf.format(selected.getTotalOperacion()));
        params.put("moneda", selected.getMoneda() == null ? "" : selected.getMoneda().getNombre());
        //params.put("firmaConyugeTitular", selected.isFirmaConyugeTitular() == false ? "no" : "si");
        //params.put("firmaConyugeCodeudor", selected.isFirmaConyugeCodeudor() == false ? "no" : "si");

        reporteController.generaPDF(params, data, "reportes/prestamos/DetalleParaCliente.jasper", "detalle_cliente_" + selected.getCliente().getNroDocumento());
    }

    public void desembolsa() {
        ejbFacade.desembolsa(selected);
        buscarADesembolsar();
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
        persist(PersistAction.UPDATE, "El prestamo se actualizo se creo EXITOSAMENTE!");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "El prestamo se elimino se creo EXITOSAMENTE!");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void buscar() {
        if (buscaPorCliente) {
            items = ejbFacade.findAllPorEmpresaFechaEstadoCliente(empresaFiltro, sucursalFiltro, estadoPrestamoFiltro, inicioFiltro, finFiltro, clienteFiltro);
        } else {
            items = ejbFacade.findAllPorEmpresaFechaEstado(empresaFiltro, sucursalFiltro, estadoPrestamoFiltro, inicioFiltro, finFiltro);
        }
    }

    public void buscarADesembolsar() {
        items = ejbFacade.findAllPorEmpresaFechaEstadoCliente(empresaFiltro, sucursalFiltro, EstadoPrestamo.EN_DESEMBOLSO, inicioFiltro, finFiltro, clienteFiltro);
    }

    public List<Prestamo> getItems() {
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
        return items;
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
                    getValue(facesContext.getELContext(), null, "prestamoController");
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

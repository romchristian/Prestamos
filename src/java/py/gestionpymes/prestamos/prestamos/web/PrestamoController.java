/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.event.SelectEvent;
import py.gestionpymes.prestamos.adm.modelo.Empresa;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil.PersistAction;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPlanGastos;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;
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
    private boolean sePuedeGuardar = false;

    private boolean buscaPorCliente;

    public boolean isSePuedeGuardar() {
        return sePuedeGuardar;
    }

    public void setSePuedeGuardar(boolean sePuedeGuardar) {
        this.sePuedeGuardar = sePuedeGuardar;
    }

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
        DateTime dt = DateTime.now();
        dt.dayOfMonth().getMaximumValue();
        this.inicioFiltro = new DateTime(dt.getYear(), dt.getMonthOfYear(), 1, 0, 0).toDate();
        this.finFiltro = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.dayOfMonth().getMaximumValue(), 0, 0).toDate();
    }

    public void ajustar() {
        selected.setSistema(null);
        selected.setDetalles(null);
        selected.calcula();
        sePuedeGuardar = true;
    }

    public void afectaPrimerVencimieto(SelectEvent event) {
        Date date = (Date) event.getObject();
        DateTime d = new DateTime(date);

        selected.setFechaPrimerVencimiento(d.plusMonths(1).toDate());
    }

    public String calcular() {
        if (selected.getPlanGastos() != null) {
            DetPlanGastos detPlan = null;
            int periocidad = 30;
            switch (selected.getPeriodoPago()) {
                case QUINCENAL:
                    periocidad = 15;
                    break;
                case SEMANAL:
                    periocidad = 7;
                    break;
                case DIARIO:
                    periocidad = 1;
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

            sePuedeGuardar = true;
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

    public void cargarDescuento(DetPrestamo d) {
        System.out.println("isTieneDescuento: " + d.isTieneDescuento());
        if (d.isTieneDescuento()) {

            BigDecimal moratorio = d.calculaSaldoMoratorio() == null ? new BigDecimal(BigInteger.ZERO) : d.calculaSaldoMoratorio();
            BigDecimal punitorio = d.calculaSaldoPunitorio() == null ? new BigDecimal(BigInteger.ZERO) : d.calculaSaldoPunitorio();

            d.setDescuento(punitorio.add(moratorio));

            System.out.println("Valor moratorio: " + moratorio);
            System.out.println("Valor punitorio: " + punitorio);
            System.out.println("Valor de descuento: " + d.getDescuento());
        } else {
            d.setDescuento(new BigDecimal(BigInteger.ZERO));

        }

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "py"));
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

        Map<String, String> params = new HashMap<String, String>();
        params.put("nroOperacion", nf.format(selected.getId()));//nf.format(selected.getId())
        params.put("empresa", selected.getEmpresa() == null ? " " : selected.getEmpresa().getRazonSocial());
        params.put("sucursal", selected.getSucursal() == null ? " " : selected.getSucursal().getNombre());
        params.put("vendedor", selected.getVendedor() == null ? " " : selected.getVendedor().devuelveNombreCompleto());
        params.put("nroDocumento", selected.getCliente() == null ? " " : selected.getCliente().getNroDocumento());
        params.put("nombreCliente", selected.getCliente() == null ? " " : selected.getCliente().devuelveNombreCompleto());
        params.put("codeudor", selected.getCodeudor() == null ? "" : selected.getCodeudor().devuelveNombreCompleto());
        params.put("montoPrestamo", nf.format(selected.getMontoPrestamo()));
        params.put("capital", nf.format(selected.getCapital()));
        params.put("plazo", nf.format(selected.getPlazo()));
        params.put("tasa", nf.format(selected.getTasa()));
        params.put("periodoPago", selected.getPeriodoPago().name());
        params.put("sistemaAmortizacion", selected.getSistemaAmortizacion().name());
        params.put("fechaOperacion", sdf.format(selected.getFechaInicioOperacion()));
        params.put("fechaPrimerVencimiento", sdf.format(selected.getFechaPrimerVencimiento()));
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

        //dateTimeVencimiento.t
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault(), new Locale("es", "py"));
        gc.setTime(selected.getFechaPrimerVencimiento());
        int diaVencimiento = gc.get(Calendar.DAY_OF_MONTH);

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
        params.put("diaVencimiento", diaVencimiento + "");
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

    public void imprimiExtractoPrestamo() {

        List<DetPrestamo> data = new ArrayList<>();

        data = selected.getDetalles();
        for (DetPrestamo dp : data) {
            System.out.println("dp:" + dp.getNroCuota());
        }

        Comparator<DetPrestamo> comp = new Comparator<DetPrestamo>() {

            @Override
            public int compare(DetPrestamo o1, DetPrestamo o2) {
                return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
            }
        };

        Collections.sort(data, comp);

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

        DateTime dateTimeOpercion = new DateTime(selected.getFechaInicioOperacion());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

        DateTime dateTimeVencimiento = new DateTime(selected.getFechaPrimerVencimiento());
        DateTimeFormatter fmtVenc = DateTimeFormat.forPattern("dd/MM/yyyy");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("prestamo_id", selected.getId());
        params.put("cliente", selected.getCliente() == null ? "" : selected.getCliente().devuelveNombreCompleto());
        params.put("nroDocumento", selected.getCliente() == null ? "" : selected.getCliente().getNroDocumento());
        params.put("montoPrestamo", nf.format(selected.getMontoPrestamo()));
        params.put("plazo", nf.format(selected.getPlazo()));
        params.put("periodicidad", selected.getPeriodoPago().name());
        params.put("saldo", selected.devuelveSaldoPrestamo() == null ? "" : nf.format(selected.devuelveSaldoPrestamo()));
        params.put("fechaOperacion", fmt.print(dateTimeOpercion));
        params.put("fechaPrimerVencimiento", fmt.print(dateTimeVencimiento));

        reporteController.generaExcel(params, data, "reportes/prestamos/ExtractoDePrestamo.jasper", "detalle_cliente_" + selected.getCliente().devuelveNombreCompleto());

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

    public String update() {
        persist(PersistAction.UPDATE, "El prestamo se actualizo se creo EXITOSAMENTE!");

        return "List.xhtml?faces-redirect=true";
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

    public void imprimeExtracto() {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("Hoja 1");
        HSSFRow row1 = worksheet.createRow((short) 0);

        HSSFCell cellA1 = row1.createCell(0);
        cellA1.setCellValue("Extracto de Prestamo");
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        //cellStyle.setFillForegroundColor(HSSFColor.AUTOMATIC.index);
        cellStyle.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
        cellA1.setCellStyle(cellStyle);
        HSSFCell cellK1 = row1.createCell(11);
        mergeAndAlignCenter(workbook, cellA1, cellK1);
        
         HSSFRow row2 = worksheet.createRow((short) 1);
        HSSFCell cellA2 = row2.createCell(0);
        cellA2.setCellValue("Cliente:" + selected.getCliente().devuelveNombreCompleto() + " -" + selected.getCliente().getNroDocumento() + ":Préstamo #" + selected.getId() + "-" + selected.getMontoPrestamo());
        cellA2.setCellStyle(cellStyle);
        HSSFCell cellK2 = row2.createCell(11);
        mergeAndAlignCenter(workbook, cellA2, cellK2);

        HSSFFont fontAriaBOLDWEIGHT_NORMAL = workbook.createFont();
        fontAriaBOLDWEIGHT_NORMAL.setFontHeightInPoints((short) 12);
        fontAriaBOLDWEIGHT_NORMAL.setFontName("Arial");
        fontAriaBOLDWEIGHT_NORMAL.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        HSSFCellStyle estiloNegrita = workbook.createCellStyle();
        cellStyle.setFont(fontAriaBOLDWEIGHT_NORMAL);

        int filaIndex = 2;

        HSSFRow filaTitulo = worksheet.createRow(filaIndex);

        HSSFCell cellNroCuotaHeader = filaTitulo.createCell(0);
        cellNroCuotaHeader.setCellValue("NroCuota");
        cellNroCuotaHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNroCuotaHeader.setCellStyle(estiloNegrita);

        HSSFCell cellCapitalHeader = filaTitulo.createCell(1);
        cellCapitalHeader.setCellValue("Capital");
        cellCapitalHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellCapitalHeader.setCellStyle(estiloNegrita);

        HSSFCell cellInteresesHeader = filaTitulo.createCell(2);
        cellInteresesHeader.setCellValue("Intereses");
        cellInteresesHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellInteresesHeader.setCellStyle(estiloNegrita);

        HSSFCell cellCuotaHeader = filaTitulo.createCell(3);
        cellCuotaHeader.setCellValue("Cuota");
        cellCuotaHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellCuotaHeader.setCellStyle(estiloNegrita);

        HSSFCell cellVencimientoHeader = filaTitulo.createCell(4);
        cellVencimientoHeader.setCellValue("Vencimiento");
        cellVencimientoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellVencimientoHeader.setCellStyle(estiloNegrita);

        HSSFCell cellDiasMoraHeader = filaTitulo.createCell(5);
        cellDiasMoraHeader.setCellValue("Dìas mora");
        cellDiasMoraHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellDiasMoraHeader.setCellStyle(estiloNegrita);

        HSSFCell cellMoraHeader = filaTitulo.createCell(6);
        cellMoraHeader.setCellValue("Mora");
        cellMoraHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellMoraHeader.setCellStyle(estiloNegrita);

        HSSFCell cellDescuentoHeader = filaTitulo.createCell(7);
        cellDescuentoHeader.setCellValue("Descuento");
        cellDescuentoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellDescuentoHeader.setCellStyle(estiloNegrita);

        HSSFCell cellSaldoHeader = filaTitulo.createCell(8);
        cellSaldoHeader.setCellValue("Saldo");
        cellSaldoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellSaldoHeader.setCellStyle(estiloNegrita);

        HSSFCell cellMontoPagadoHeader = filaTitulo.createCell(9);
        cellMontoPagadoHeader.setCellValue("Monto pagado");
        cellMontoPagadoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellMontoPagadoHeader.setCellStyle(estiloNegrita);

        HSSFCell cellFechaPagoHeader = filaTitulo.createCell(10);
        cellFechaPagoHeader.setCellValue("Fecha pago");
        cellFechaPagoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellFechaPagoHeader.setCellStyle(estiloNegrita);

        HSSFCell cellEstadoHeader = filaTitulo.createCell(11);
        cellEstadoHeader.setCellValue("Estado");
        cellEstadoHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellEstadoHeader.setCellStyle(estiloNegrita);

        //totalizadores
        double totalizaCapital = 0, totalizaInteres = 0, totalizaCuota = 0, totalizaMoratorio = 0, totalizaSaldo = 0, totalizaMontoPago = 0, totalizaDescuento = 0;

        for (DetPrestamo dt : selected.getDetalles()) {
            
            //NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

            filaIndex++;

            HSSFRow fila = worksheet.createRow(filaIndex);

            HSSFCell cellNroCuota = fila.createCell(0);
            cellNroCuota.setCellValue(dt.getNroCuota());
            cellNroCuota.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellNroCuota.setCellStyle(estiloNegrita);

            HSSFCell cellCapital = fila.createCell(1);
            cellCapital.setCellValue(dt.getCuotaCapital().doubleValue());
            cellCapital.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellCapital.setCellStyle(estiloNegrita);

            HSSFCell cellIntereses = fila.createCell(2);
            cellIntereses.setCellValue(dt.getCuotaInteres().doubleValue());
            cellIntereses.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellIntereses.setCellStyle(estiloNegrita);

            HSSFCell cellCuota = fila.createCell(3);
            cellCuota.setCellValue(dt.getMontoCuota().doubleValue());
            cellCuota.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellCuota.setCellStyle(estiloNegrita);

            DateTime dateTimeVencimiento = new DateTime(dt.getFechaVencimiento());
            DateTimeFormatter fmtVencimiento = DateTimeFormat.forPattern("dd/MM/yyyy");

            HSSFCell cellVencimiento = fila.createCell(4);
            cellVencimiento.setCellValue(fmtVencimiento.print(dateTimeVencimiento));
            cellVencimiento.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellVencimiento.setCellStyle(estiloNegrita);

            HSSFCell cellDiasMora = fila.createCell(5);
            cellDiasMora.setCellValue(dt.getDiasMora());
            cellDiasMora.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellDiasMora.setCellStyle(estiloNegrita);
            
            double moraMoratorio=(dt.getMoraMoratorio() == null ? 0 : dt.getMoraMoratorio().doubleValue());
            double moraPunitorio=(dt.getMoraPunitorio()==null?0:dt.getMoraPunitorio().doubleValue());
            

            HSSFCell cellMora = fila.createCell(6);
            cellMora.setCellValue(moraMoratorio+moraPunitorio);
            cellMora.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellMora.setCellStyle(estiloNegrita);

            HSSFCell cellDescuento = fila.createCell(7);
            cellDescuento.setCellValue((dt.getDescuento() == null ? 0 : dt.getDescuento().doubleValue()));
            cellDescuento.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellDescuento.setCellStyle(estiloNegrita);

            HSSFCell cellSaldo = fila.createCell(8);
            cellSaldo.setCellValue((dt.getSaldoCuota() == null ? 0 : dt.getSaldoCuota().doubleValue()));
            cellSaldo.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellSaldo.setCellStyle(estiloNegrita);

            HSSFCell cellMontoPagado = fila.createCell(9);
            cellMontoPagado.setCellValue((dt.getMontoPago() == null ? 0 : dt.getMontoPago().doubleValue()));
            cellMontoPagado.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cellMontoPagado.setCellStyle(estiloNegrita);
            
            DateTime dateTimePago = new DateTime(dt.getUltimoPago() == null ? null : dt.getUltimoPago());
            DateTimeFormatter fmtPago = DateTimeFormat.forPattern("dd/MM/yyyy");

            HSSFCell cellFechaPago = fila.createCell(10);
            cellFechaPago.setCellValue( fmtPago==null?"":fmtPago.print(dateTimePago));
            cellFechaPago.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellFechaPago.setCellStyle(estiloNegrita);

            HSSFCell cellEstado = fila.createCell(11);
            cellEstado.setCellValue(dt.getEstado().name());
            cellEstado.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellEstado.setCellStyle(estiloNegrita);

            totalizaCapital = totalizaCapital + dt.getCuotaCapital().doubleValue();
            totalizaInteres = totalizaInteres + dt.getCuotaInteres().doubleValue();
            totalizaCuota = totalizaCuota + dt.getMontoCuota().doubleValue();
            totalizaMoratorio = totalizaMoratorio + moraMoratorio + moraPunitorio;
            totalizaSaldo = totalizaSaldo + (dt.getSaldoCuota() == null ? 0 : dt.getSaldoCuota().doubleValue());
            totalizaMontoPago = totalizaMontoPago + (dt.getMontoPago() == null ? 0 : dt.getMontoPago().doubleValue());
            totalizaDescuento = totalizaDescuento + (dt.getDescuento() == null ? 0 : dt.getDescuento().doubleValue());

        }
        
        filaIndex++;
        
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));

        HSSFRow filaTotales = worksheet.createRow(filaIndex);

        HSSFCell cellCapitalTotal = filaTotales.createCell(1);
        cellCapitalTotal.setCellValue(totalizaCapital);
        cellCapitalTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellCapitalTotal.setCellStyle(estiloNegrita);

        HSSFCell cellInteresesTotal = filaTotales.createCell(2);
        cellInteresesTotal.setCellValue(totalizaInteres);
        cellInteresesTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellInteresesTotal.setCellStyle(estiloNegrita);

        HSSFCell cellCuotaTotal = filaTotales.createCell(3);
        cellCuotaTotal.setCellValue(totalizaCuota);
        cellCuotaTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellCuotaTotal.setCellStyle(estiloNegrita);

        HSSFCell cellMoraTotal = filaTotales.createCell(6);
        cellMoraTotal.setCellValue(totalizaMoratorio);
        cellMoraTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellMoraTotal.setCellStyle(estiloNegrita);

        HSSFCell cellDescuentoTotal = filaTotales.createCell(7);
        cellDescuentoTotal.setCellValue(totalizaDescuento);
        cellDescuentoTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellDescuentoTotal.setCellStyle(estiloNegrita);

        HSSFCell cellSaldoTotal = filaTotales.createCell(8);
        cellSaldoTotal.setCellValue(totalizaSaldo);
        cellSaldoTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellSaldoTotal.setCellStyle(estiloNegrita);

        HSSFCell cellMontoPagadoTotal = filaTotales.createCell(9);
        cellMontoPagadoTotal.setCellValue(totalizaMontoPago);
        cellMontoPagadoTotal.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellMontoPagadoTotal.setCellStyle(estiloNegrita);
        
//        filaIndex++;
//        
//        HSSFRow filaSaldoDeuda = worksheet.createRow(filaIndex);
//        
//        HSSFCell cellSaldoDeudaHeader = filaSaldoDeuda.createCell(0);
//        cellSaldoDeudaHeader.setCellValue("Saldo Deuda:");
//        cellSaldoDeudaHeader.setCellType(HSSFCell.CELL_TYPE_STRING);
//        cellSaldoDeudaHeader.setCellStyle(estiloNegrita);
//        
//        HSSFCell cellSaldoDeuda = filaSaldoDeuda.createCell(1);
//        cellSaldoDeuda.setCellValue(totalizaSaldo);
//        cellSaldoDeuda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//        cellSaldoDeuda.setCellStyle(estiloNegrita);

        reporteController.generaExcel(workbook, "Extracto_Prestamo_" + selected.getCliente().devuelveNombreCompleto());

    }

    public static void mergeAndAlignCenter(Workbook wb, HSSFCell startCell, HSSFCell endCell) {
        //finding reference of start and end cell; will result like $A$1
        CellReference startCellRef = new CellReference(startCell.getRowIndex(), startCell.getColumnIndex());
        CellReference endCellRef = new CellReference(endCell.getRowIndex(), endCell.getColumnIndex());
        // forming string of references; will result like $A$1:$B$5 
        String cellRefernce = startCellRef.formatAsString() + ":" + endCellRef.formatAsString();
        //removing $ to make cellRefernce like A1:B5
        cellRefernce = cellRefernce.replace("$", "");
        //passing cellRefernce to make a region 
        CellRangeAddress region = CellRangeAddress.valueOf(cellRefernce);
        //use region to merge; though other method like sheet.addMergedRegion(new CellRangeAddress(1,1,4,1));
        // is also available, but facing some problem right now.
        startCell.getRow().getSheet().addMergedRegion(region);
        //setting alignment to center
        CellUtil.setAlignment(startCell, wb, CellStyle.ALIGN_CENTER);
    }
}

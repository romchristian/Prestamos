/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.dao.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;

/**
 *
 * @author cromero
 */
@Named
@SessionScoped
public class CobraCuotaBean implements Serializable {

    @EJB
    private PrestamoDAO prestamoDAO;
    @EJB
    private MonedaFacade monedaDAO;
    @EJB
    private CobranzaDAO cobranzaDAO;

    private TreeNode root;
    private Cliente cliente;
    private List<TreeCuota> seleccionados = new ArrayList<TreeCuota>();
    private List<TreeCuota> disponibles;
    private BigDecimal totalAPagar;
    private TreeCuota cuotaSeleccionada;
    private BigDecimal montoActual;

    private FacturaVenta facturaVenta;

    public FacturaVenta getFacturaVenta() {
        if (facturaVenta == null) {
            facturaVenta = new FacturaVenta();
        }
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    public String obtDescCuota() {
        String R = "";
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));
            R = cuotaSeleccionada.getPrestamo().getId() + ";" + nf.format(cuotaSeleccionada.getPrestamo().getMontoPrestamo()) + " - Cuota Nro "
                    + cuotaSeleccionada.getDetPrestamo().getNroCuota();
        }
        return R;
    }

    public String obtDescMora() {
        String R = "";
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            R = cuotaSeleccionada.getDetPrestamo().getDiasMora() < 0 ? 0 + " días" : cuotaSeleccionada.getDetPrestamo().getDiasMora() + " días";
        }
        return R;
    }

    public Date obtUltimoPago() {
        Date R = null;
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            R = cuotaSeleccionada.getPrestamo().getUltimoPago();

        }

        return R;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    public TreeCuota getCuotaSeleccionada() {
        if (cuotaSeleccionada == null) {
            cuotaSeleccionada = new TreeCuota();
        }
        return cuotaSeleccionada;
    }

    public void setCuotaSeleccionada(TreeCuota cuotaSeleccionada) {

        this.cuotaSeleccionada = cuotaSeleccionada;
        montoActual = cuotaSeleccionada == null ? new BigDecimal(BigInteger.ZERO) : cuotaSeleccionada.getSaldoCuota();
    }

    public String generaFactura() {
        if (cliente == null) {
            JsfUtil.addErrorMessage("Debe Seleccionar un cliente");
            return null;
        }

        if (cuotaSeleccionada == null) {
            JsfUtil.addErrorMessage("Debe seleccionar al menos uno cuota");
            return null;
        }

        facturaVenta = new FacturaVenta();
        facturaVenta.setCliente(cliente);
        if (cuotaSeleccionada != null) {
            facturaVenta.setEmpresa(cuotaSeleccionada.getEmpresa());
            facturaVenta.setSucursal(cuotaSeleccionada.getSucursal());
        }

        
        facturaVenta.setFechaCreacion(new Date());
        facturaVenta.setFechaEmision(new Date());
        facturaVenta.setDireccion(cliente.devuelveDireccionParticular());
        facturaVenta.setRazonSocial(cliente.devuelveNombreCompleto());
        facturaVenta.setRuc(cliente.getNroDocumento());
        facturaVenta.setCodEstablecimiento("001");
        facturaVenta.setPuntoExpedicion("001");
        facturaVenta.setNumero("0000001");

        facturaVenta.setDetalles(new ArrayList<FacturaVentaDetalle>());
        int nrolinea = 1;
        for (TreeCuota t : seleccionados) {
            FacturaVentaDetalle d = new FacturaVentaDetalle();
            d.setFacturaVenta(facturaVenta);
            d.setNrolinea(nrolinea);
            d.setCantidad(new BigDecimal(BigInteger.ONE));

            d.setDescripcion("Pago de " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
            d.setPrecioUnitario(t.getMontoCuota());
            d.setGravada10(d.getCantidad().multiply(d.getPrecioUnitario()));
            facturaVenta.getDetalles().add(d);
            nrolinea++;

            if (t.getMontoMoratorio().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                FacturaVentaDetalle d2 = new FacturaVentaDetalle();
                d2.setFacturaVenta(facturaVenta);
                d2.setNrolinea(nrolinea);
                d2.setCantidad(new BigDecimal(BigInteger.ONE));

                d2.setDescripcion("Pago por mora de la " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
                d2.setPrecioUnitario(t.getMontoMoratorio());
                d2.setGravada10(d2.getCantidad().multiply(d2.getPrecioUnitario()));
                facturaVenta.getDetalles().add(d2);
                nrolinea++;
            }

            if (t.getMontoPunitorio().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                FacturaVentaDetalle d3 = new FacturaVentaDetalle();
                d3.setFacturaVenta(facturaVenta);
                d3.setNrolinea(nrolinea);
                d3.setCantidad(new BigDecimal(BigInteger.ONE));

                d3.setDescripcion("Pago punitorio de la " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
                d3.setPrecioUnitario(t.getMontoPunitorio());
                d3.setGravada10(d3.getCantidad().multiply(d3.getPrecioUnitario()));
                facturaVenta.getDetalles().add(d3);
                nrolinea++;
            }

            BigDecimal totalexento = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalgravada05 = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalgravada10 = new BigDecimal(BigInteger.ZERO);
            for (FacturaVentaDetalle fd : facturaVenta.getDetalles()) {
                if(fd.getExenta() != null ){
                    totalexento = totalexento.add(fd.getExenta());
                }
                if(fd.getGravada05() != null ){
                    totalgravada05 = totalgravada05.add(fd.getGravada05());
                }
                
                if(fd.getGravada10()!= null ){
                    totalgravada10 = totalgravada10.add(fd.getGravada10());
                }
                
                
            }

            BigDecimal total = totalgravada10.add(totalgravada05).add(totalexento);
            BigDecimal iva05 = new BigDecimal(BigInteger.ZERO);
            BigDecimal iva10 = new BigDecimal(BigInteger.ZERO);
            if (iva10.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                iva10 = iva10.divide(new BigDecimal(11), 0, RoundingMode.HALF_EVEN);
            }
            if (iva05.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                iva05 = iva05.divide(new BigDecimal(21), 0, RoundingMode.HALF_EVEN);
            }

            BigDecimal ivatotal = iva05.add(iva10);

            facturaVenta.setTotalExento(totalexento.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalGravada10(totalgravada05.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalGravada10(totalgravada10.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotal(total.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setIva05(iva05.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setIva10(iva10.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalIva(ivatotal.setScale(0, RoundingMode.HALF_EVEN));

        }

        return "/contabilidad/facturaVenta/CreaFactura?faces-redirect=true";
    }

    public void paga() {
        try {
            for (TreeCuota t : seleccionados) {

                cobranzaDAO.create(t);
                t.setSeleccionado(false);

            }

            seleccionados.clear();
            cargaPrestamos();
        } catch (PagoExcedidoException ex) {
            Logger.getLogger(CobraCuotaBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @PostConstruct
    public void init() {

        cargaPrestamos();
    }

    public BigDecimal getTotalAPagar() {
        totalAPagar = new BigDecimal(BigInteger.ZERO);
        for (TreeCuota t : seleccionados) {
            totalAPagar = totalAPagar.add(t.getMontoPago());
        }
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public List<TreeCuota> getSeleccionados() {

        return seleccionados;
    }

    public void setSeleccionados(List<TreeCuota> seleccionados) {
        this.seleccionados = seleccionados;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String buscaPrestamos() {

        cargaPrestamos();
        return "cobraCuota?faces-redirect=true";
    }

    private void cargaPrestamos() {
        root = new DefaultTreeNode("prestamos", null);
        disponibles = new ArrayList<TreeCuota>();

        Comparator<DetPrestamo> comp = new Comparator<DetPrestamo>() {

            @Override
            public int compare(DetPrestamo o1, DetPrestamo o2) {
                return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
            }
        };

        for (Prestamo p : prestamoDAO.findAllClienteEstado(cliente, EstadoPrestamo.VIGENTE)) {
            TreeNode nodoPrestamo = new DefaultTreeNode(new TreeCuota(p), root);

            Collections.sort(p.getDetalles(), comp);

            for (DetPrestamo d : p.getDetalles()) {
                TreeCuota cuota = new TreeCuota(d);
                TreeNode nodoCuota = new DefaultTreeNode(cuota, nodoPrestamo);
                disponibles.add(cuota);
            }
        }
    }

    public void agregaAPagar() {
        System.out.println("En selcciona");

        if (seleccionados == null) {
            seleccionados = new ArrayList<>();
        }

        cuotaSeleccionada.setMontoPago(montoActual);

        seleccionados.add(cuotaSeleccionada);

        montoActual = new BigDecimal(BigInteger.ZERO);
    }

    @FacesValidator(value = "pagoValidator")
    public static class PagoValidator implements Validator {

        @Override
        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            if (value instanceof BigDecimal) {
                System.out.println("Valueeeeeeeee: " + value);
                BigDecimal montoPago = ((BigDecimal) value).setScale(0, RoundingMode.HALF_EVEN);

                CobraCuotaBean controller = (CobraCuotaBean) context.getApplication().getELResolver().
                        getValue(context.getELContext(), null, "cobraCuotaBean");
                if (montoPago.compareTo(controller.getCuotaSeleccionada().getSaldoCuota().add(controller.getCuotaSeleccionada().getMontoMora())) > 0) {
                    FacesMessage msg = new FacesMessage("El pago excede el monto de la cuota");
                    throw new ValidatorException(msg);
                }
            }
        }

    }
}

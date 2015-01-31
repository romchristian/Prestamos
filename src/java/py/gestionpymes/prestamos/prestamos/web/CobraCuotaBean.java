/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
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
@ViewScoped
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
    private double totalAPagar;
    private TreeCuota cuotaSeleccionada;
    private Double montoActual;

    public String obtDescCuota() {
        String R = "";
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            NumberFormat nf = NumberFormat.getInstance(new Locale("es","py"));
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
        if(cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null){
            R = cuotaSeleccionada.getPrestamo().getUltimoPago();
        }
        
        return R;
    }

    public Double getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(Double montoActual) {
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
        montoActual = cuotaSeleccionada == null ? 0 : cuotaSeleccionada.getSaldoCuota();
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

    public double getTotalAPagar() {
        totalAPagar = 0;
        for (TreeCuota t : seleccionados) {
            totalAPagar += t.getMontoPago();
        }
        return totalAPagar;
    }

    public void setTotalAPagar(double totalAPagar) {
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
        return null;
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
            seleccionados = new ArrayList<TreeCuota>();
        }

        cuotaSeleccionada.setMontoPago(montoActual);

        seleccionados.add(cuotaSeleccionada);

        montoActual = 0D;
    }

    @FacesValidator(value = "pagoValidator")
    public static class PagoValidator implements Validator {

        @Override
        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            if (value instanceof Double) {
                Double montoPago = (Double) value;

                CobraCuotaBean controller = (CobraCuotaBean) context.getApplication().getELResolver().
                        getValue(context.getELContext(), null, "cobraCuotaBean");
                if (montoPago > (controller.getCuotaSeleccionada().getSaldoCuota() + controller.getCuotaSeleccionada().getMontoMora())) {
                    FacesMessage msg = new FacesMessage("El pago excede el monto de la cuota");
                    throw new ValidatorException(msg);
                }
            }
        }

    }
}

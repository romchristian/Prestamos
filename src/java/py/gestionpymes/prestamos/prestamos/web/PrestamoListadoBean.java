/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.adm.modelo.Empresa;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.contabilidad.servicio.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;

/**
 *
 * @author cromero
 */
@Named
@ViewScoped
public class PrestamoListadoBean implements Serializable {

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
    private Empresa empresa;
    private Sucursal sucursal;
    private EstadoPrestamo estado;
    private boolean buscaPorCliente;
    private Date inicio;
    private Date fin;

    
    public boolean isBuscaPorCliente() {
        return buscaPorCliente;
    }

    public void setBuscaPorCliente(boolean buscaPorCliente) {
        this.buscaPorCliente = buscaPorCliente;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
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

    public void paga() {
        try {
            for (TreeCuota t : seleccionados) {

                cobranzaDAO.create(t);
                t.setSeleccionado(false);

            }

            seleccionados.clear();
            cargaPrestamos();
        } catch (PagoExcedidoException ex) {
            Logger.getLogger(PrestamoListadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        return null;
    }

    @PostConstruct
    public void init() {
        DateTime dt = new DateTime(new Date(),DateTimeZone.getDefault());
//        dt.withTime(LocalTime.fromCalendarFields(new GregorianCalendar(new Locale("es", "py"))));
//        
//        inicio = (new DateTime(dt.getYear(), dt.getMonthOfYear(),dt.dayOfMonth().getMinimumValue(),
//                dt.hourOfDay().getMinimumValue(),dt.minuteOfHour().getMinimumValue()))
//                .withTime(LocalTime.fromCalendarFields(new GregorianCalendar(new Locale("es", "py"))))
//                .toDate();
//        fin = (new DateTime(dt.getYear(), dt.getMonthOfYear(),dt.dayOfMonth().getMaximumValue(),
//                dt.hourOfDay().getMaximumValue(),dt.minuteOfHour().getMaximumValue()))
//                .withTime(LocalTime.fromCalendarFields(new GregorianCalendar(new Locale("es", "py"))))
//                .toDate();
        
        
        
        cargaPrestamos();
     
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

        if (empresa != null && sucursal != null) {
            List<Prestamo> items;
            if (buscaPorCliente) {
                items = prestamoDAO.findAllPorEmpresaFechaEstadoCliente(empresa, sucursal, estado, inicio, fin, cliente);
            } else {
                items = prestamoDAO.findAllPorEmpresaFechaEstado(empresa, sucursal, estado, inicio, fin);
            }

            for (Prestamo p : items) {
                System.out.println("Pres: " + p);
                TreeNode nodoPrestamo = new DefaultTreeNode(new TreeCuota(p), root);

                Collections.sort(p.getDetalles(), comp);

                for (DetPrestamo d : p.getDetalles()) {
                    System.out.println("Det: " + d);
                    TreeCuota cuota = new TreeCuota(d);
                    TreeNode nodoCuota = new DefaultTreeNode(cuota, nodoPrestamo);
                    disponibles.add(cuota);
                }
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

}

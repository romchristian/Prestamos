/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.prestamos.dao.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.dao.PagoDAO;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCobroCuota;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.Efectivo;
import py.gestionpymes.prestamos.prestamos.persistencia.EstadoPrestamo;
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
    @EJB
    private PagoDAO pagoDAO;

    private TreeNode root;
    private Cliente cliente;
    private List<TreeCuota> seleccionados = new ArrayList<TreeCuota>();
    private List<TreeCuota> disponibles;
    private double totalAPagar;
    private TreeCuota cuotaSeleccionada;

    public TreeCuota getCuotaSeleccionada() {
        if(cuotaSeleccionada == null){
            cuotaSeleccionada = new TreeCuota();
        }
        return cuotaSeleccionada;
    }

    public void setCuotaSeleccionada(TreeCuota cuotaSeleccionada) {
        this.cuotaSeleccionada = cuotaSeleccionada;
    }

    public void paga() {

        System.out.println("Paga");
        for (TreeCuota t : seleccionados) {
            CobroCuota cc = new CobroCuota(t.getPrestamo());
System.out.println("Paga 1");
            Efectivo efe = new Efectivo();
            efe.setFecha(new Date());
            efe.setMoneda(t.getMoneda());
            efe.setMonto(t.getMontoPago());
            pagoDAO.create(efe);
            
            System.out.println("Paga 2");
            DetCobroCuota dcc = new DetCobroCuota();
            dcc.setCobroCuota(cc);
            dcc.setPago(efe);
            dcc.setMoneda(t.getMoneda());
            dcc.setMonto(efe.getMonto());
            System.out.println("Paga 3");
            if (!t.isEsPrestamo()) {
                dcc.setDetPrestamo(t.getDetPrestamo());
            }
            dcc.setFecha(new Date());
            cc.getDetalles().add(dcc);
            
            cobranzaDAO.create(cc);
            System.out.println("Paga 4");
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

        for (Prestamo p : prestamoDAO.findAllClienteEstado(cliente,EstadoPrestamo.VIGENTE)) {
            TreeNode nodoPrestamo = new DefaultTreeNode(new TreeCuota(p), root);

            Collections.sort(p.getDetalles(), comp);

            for (DetPrestamo d : p.getDetalles()) {
                TreeCuota cuota = new TreeCuota(d);
                TreeNode nodoCuota = new DefaultTreeNode(cuota, nodoPrestamo);
                disponibles.add(cuota);
            }
        }
    }

    public void selecciona() {
        System.out.println("En selcciona");
        cuotaSeleccionada.setSeleccionado(true);
        
        seleccionados = new ArrayList<TreeCuota>();
        for (TreeCuota cuota : disponibles) {
            if (cuota.isSeleccionado()) {
                cuota.setMontoPago(cuota.getMontoCuota());
                seleccionados.add(cuota);
            } else {
                cuota.setMontoPago(0d);
            }
        }
    }

}

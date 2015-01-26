/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;
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
    private TreeNode root;
    private Cliente cliente;
    private List<TreeCuota> seleccionados = new ArrayList<TreeCuota>();
    private List<TreeCuota> disponibles;
    private double totalAPagar;
    
    
    @PostConstruct
    public void init(){
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

        for (Prestamo p : prestamoDAO.findAll(cliente)) {
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

        seleccionados = new ArrayList<TreeCuota>();
        for(TreeCuota cuota: disponibles){
            if(cuota.isSeleccionado()){
                cuota.setMontoPago(cuota.getMontoCuota());
                seleccionados.add(cuota);
            }else{
                cuota.setMontoPago(0d);
            }
        }
        
        

    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestion.contabilidad.servicio.DiarioDAO;
import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.contabilidad.persistencia.Diario;
import py.gestionpymes.prestamos.contabilidad.persistencia.TipoDiario;


/**
 *
 * @author christian
 */
@Named
@ViewScoped
public class DiarioTreeBean extends BeanGenerico<Diario> {

    @EJB
    private DiarioDAO ejb;

    private TreeNode rootNode;
    
    @Override
    public String cargaDatos() {
        List<Diario> roots = ejb.findAllRoots();
        rootNode = new DefaultTreeNode(new Diario("Diarios", TipoDiario.VENTA), null);
        for (Diario d : roots) {
            TreeNode t1 = createTree(d, rootNode);
        }
        return null;
    }

    public TreeNode createTree(Diario treeObj, TreeNode rootNode) {
        TreeNode newNode = new DefaultTreeNode(treeObj, rootNode);

        List<Diario> childNodes1 = ejb.findAllChildren(treeObj);

        for (Diario tt : childNodes1) {
            TreeNode newNode2 = createTree(tt, newNode);
        }

        return newNode;
    }

    @Override
    public AbstractDAO<Diario> getEjb() {
        return ejb;
    }

    @Override
    public Diario getNuevo() {
        return new Diario();
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    
    
}

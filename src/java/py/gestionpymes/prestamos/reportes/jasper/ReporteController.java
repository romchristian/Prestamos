/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.reportes.jasper;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.gestionpymes.prestamos.prestamos.web.Pagare;

/**
 *
 * @author Acer
 */
@Named
@ApplicationScoped
public class ReporteController implements Serializable {

    private JasperPrint jasperPrint;

    @PostConstruct
    private void inicializa() {
        try {
            List<Pagare> lista = new ArrayList<>();
            init(new HashMap(), lista, "reportes/prestamos/pagares.jasper", FacesContext.getCurrentInstance());
        } catch (JRException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init(Map parametros, Collection<?> lista, String nombre, FacesContext context) throws JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        String reportpath = context
                .getExternalContext()
                .getRealPath(nombre);
        jasperPrint = JasperFillManager.fillReport(reportpath, parametros, beanCollectionDataSource);

    }

    private void init(Map parametros, String nombre, FacesContext context) throws JRException {

        String reportpath = context
                .getExternalContext()
                .getRealPath(nombre);

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prestamos", "postgres", "postgres");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        
        System.out.println("con: "+conn);
        jasperPrint = JasperFillManager.fillReport(reportpath, parametros, conn);

    }

    public void generaPDF(Map parametros, String archivoPath, String nombre) {
        try {

            
            System.out.println("Entre a generaPDF");
            FacesContext context = FacesContext.getCurrentInstance();
            init(parametros, archivoPath, context);

            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + nombre + ".pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            context.responseComplete();

        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generaPDF(Map parametros, Collection<?> detalles, String archivoPath, String nombre) {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            init(parametros, detalles, archivoPath, context);

            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + nombre + ".pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            context.responseComplete();

        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generaExcel(Map parametros, Collection<?> detalles, String archivoPath, String nombre) {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            init(parametros, detalles, archivoPath, context);

            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + nombre + ".xls");
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
            context.responseComplete();

        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

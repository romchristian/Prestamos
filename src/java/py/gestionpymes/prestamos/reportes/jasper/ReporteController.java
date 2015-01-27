/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.reportes.jasper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Acer
 */
@Named
@RequestScoped
public class ReporteController {

    private JasperPrint jasperPrint;

    private void init(Map parametros, Collection<?> lista, String nombre, FacesContext context) throws JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
        String reportpath = context
                .getExternalContext()
                .getRealPath(nombre);
        jasperPrint = JasperFillManager.fillReport(reportpath, parametros, beanCollectionDataSource);

    }

    public void generaPDF(Map parametros, Collection<?> detalles, String archivoPath) {
        try {
            
            FacesContext context = FacesContext.getCurrentInstance();
            init(parametros, detalles, archivoPath,context);
            
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            context.responseComplete();
            
        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            Logger.getLogger(ReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

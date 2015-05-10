/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.dao.VistaGrafico;

/**
 *
 * @author Acer
 */
@Named
@ViewScoped
public class ControlTesoreriaBean implements Serializable {

    @EJB
    private TransaccionDAO transaccionDAO;

    private VistaGrafico vistaGrafico;

    public VistaGrafico getVistaGrafico() {
        if (vistaGrafico == null) {
            vistaGrafico = transaccionDAO.obtVistaGrafico();
        }
        return vistaGrafico;
    }

    public void setVistaGrafico(VistaGrafico vistaGrafico) {
        this.vistaGrafico = vistaGrafico;
    }

    private BarChartModel barModel;

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    @PostConstruct
    public void init() {

        //createBarModel();
    }

    private void createBarModel() {
      
        barModel.setTitle("Disponibilidades en cajas");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Transacciones");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Monto");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
    }

 
}

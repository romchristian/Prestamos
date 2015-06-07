/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import py.gestionpymes.prestamos.contabilidad.modelo.ChequeRecibido;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.dao.VistaGrafico;
import py.gestionpymes.prestamos.tesoreria.modelo.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.modelo.Transaccion;

/**
 *
 * @author Acer
 */
@Named
@ViewScoped
public class ControlTesoreriaBean implements Serializable {

    private final String HOY = "hoy";
    private final String SEMANA = "semana";
    private final String MES = "mes";

    private final String HOY_T = "hoy";
    private final String SEMANA_T = "semana";
    private final String MES_T = "mes";

    @EJB
    private TransaccionDAO transaccionDAO;

    private VistaGrafico vistaGrafico;

    private Date fecha = new Date();
    private List<ChequeRecibido> cheques;
    private List<Transaccion> transacciones;
    private PuntoVenta puntoVentaSeleccionado;
    private String botonSeleccionado;
    private String botonSeleccionadoT;

    public List<Transaccion> getTransacciones() {
         if (transacciones == null) {
            buscaTransaccionesHoy();
        }
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public String getBotonSeleccionadoT() {
        return botonSeleccionadoT;
    }

    public void setBotonSeleccionadoT(String botonSeleccionadoT) {
        this.botonSeleccionadoT = botonSeleccionadoT;
    }

    public String getBotonSeleccionado() {
        return botonSeleccionado;
    }

    public void setBotonSeleccionado(String botonSeleccionado) {
        this.botonSeleccionado = botonSeleccionado;
    }

    public PuntoVenta getPuntoVentaSeleccionado() {
        return puntoVentaSeleccionado;
    }

    public void setPuntoVentaSeleccionado(PuntoVenta puntoVentaSeleccionado) {
        this.puntoVentaSeleccionado = puntoVentaSeleccionado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ChequeRecibido> getCheques() {
        if (cheques == null) {
            buscaChequesHoy();
        }
        return cheques;
    }

    public void setCheques(List<ChequeRecibido> cheques) {
        this.cheques = cheques;
    }

    public void buscaChequesHoy() {
        cheques = transaccionDAO.getChequesDelDia(puntoVentaSeleccionado);
        botonSeleccionado = HOY;
    }

    public void buscaChequesMes() {
        cheques = transaccionDAO.getChequesMes(puntoVentaSeleccionado);
        botonSeleccionado = MES;
    }

    public void buscaChequesSemana() {
        cheques = transaccionDAO.getChequesSemana(puntoVentaSeleccionado);
        botonSeleccionado = SEMANA;
    }

    public void buscaTransaccionesHoy() {
        transacciones = transaccionDAO.getTransaccionesDelDia(puntoVentaSeleccionado);
        botonSeleccionadoT = HOY;
    }

    public void buscaTransaccionesMes() {
        transacciones = transaccionDAO.getTransaccionesMes(puntoVentaSeleccionado);
        botonSeleccionadoT = MES;
    }

    public void buscaTransaccionesSemana() {
        transacciones = transaccionDAO.getTransaccionesSemana(puntoVentaSeleccionado);
        botonSeleccionadoT = SEMANA;
    }

    public VistaGrafico getVistaGrafico() {
        if (vistaGrafico == null) {
            vistaGrafico = transaccionDAO.obtVistaGrafico(puntoVentaSeleccionado);
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

    public void siCambiaPuntoVenta() {
        vistaGrafico = null;

        switch (botonSeleccionado) {
            case HOY:
                cheques = transaccionDAO.getChequesDelDia(puntoVentaSeleccionado);
                
                break;
            case SEMANA:
                cheques = transaccionDAO.getChequesSemana(puntoVentaSeleccionado);
                break;
            case MES:
                cheques = transaccionDAO.getChequesMes(puntoVentaSeleccionado);
                break;
        }
        
        
        switch (botonSeleccionadoT) {
            case HOY_T:
                transacciones = transaccionDAO.getTransaccionesDelDia(puntoVentaSeleccionado);
                
                break;
            case SEMANA_T:
                transacciones = transaccionDAO.getTransaccionesSemana(puntoVentaSeleccionado);
                break;
            case MES_T:
                transacciones = transaccionDAO.getTransaccionesMes(puntoVentaSeleccionado);
                break;
        }
    }
}

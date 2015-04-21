/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import py.gestionpymes.prestamos.contabilidad.servicio.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.DetPrestamo;

/**
 *
 * @author Christian
 */
@Named
@ViewScoped
public class AgendaCobrosBean implements Serializable {

    @EJB
    private CobranzaDAO ejb;

    private ScheduleModel lazyEventModel;
     private ScheduleEvent event = new DefaultScheduleEvent();

    @PostConstruct
    public void init() {

        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                System.out.println("Inicio:" + start);
                System.out.println("Fin:" + end);

                List<DetPrestamo> vencimientos = ejb.findVencientos(start, end);
                System.out.println("Lista:" + vencimientos);
                for (DetPrestamo v : vencimientos) {
                    addEvent(new DefaultScheduleEvent(v.getPrestamo().getCliente().devuelveNombreCompleto() + "\nVencimiento cuota #" + v.getNroCuota() + "\nMonto: " + v.getSaldoCuota(),
                            (new DateTime(v.getFechaVencimiento()).plusHours(8)).toDate(),
                            (new DateTime(v.getFechaVencimiento()).plusHours(10).plusMinutes(30)).toDate()));
                }

            }
        };
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

}

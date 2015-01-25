/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.persistencia.Estado;
import py.gestionpymes.prestamos.adm.persistencia.TipoPersona;
import py.gestionpymes.prestamos.prestamos.persistencia.PeriodoPago;
import py.gestionpymes.prestamos.prestamos.persistencia.SistemaAmortizacion;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.Calificacion;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.CicloIngreso;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoCivil;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoContactoTelefonico;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.Sexo;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoContactoTelefonico;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoDireccion;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoDocumento;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoEmpresa;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.TipoReferencia;


/**
 *
 * @author christian
 */
@Named
@ApplicationScoped
public class ProductorEnums implements Serializable {

    public SelectItem[] obtEstados() {
        SelectItem[] R = new SelectItem[Estado.values().length];
        Estado[] lista = Estado.values();
        for (int i = 0; i < lista.length; i++) {
            Estado e = lista[i];
            R[i] = new SelectItem(e, e.toString());
        }
        return R;
    }

    public SelectItem[] obtTiposPersonas() {
        SelectItem[] R = new SelectItem[TipoPersona.values().length];
        TipoPersona[] lista = TipoPersona.values();
        for (int i = 0; i < lista.length; i++) {
            TipoPersona tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtTiposEmpresas() {
        SelectItem[] R = new SelectItem[TipoEmpresa.values().length];
        TipoEmpresa[] lista = TipoEmpresa.values();
        for (int i = 0; i < lista.length; i++) {
            TipoEmpresa tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }
    
    public SelectItem[] obtCicloIngresos() {
        SelectItem[] R = new SelectItem[CicloIngreso.values().length];
        CicloIngreso[] lista = CicloIngreso.values();
        for (int i = 0; i < lista.length; i++) {
            CicloIngreso tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtSexo() {
        SelectItem[] R = new SelectItem[Sexo.values().length];
        Sexo[] lista = Sexo.values();
        for (int i = 0; i < lista.length; i++) {
            Sexo tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtTipoReferencia() {
        SelectItem[] R = new SelectItem[TipoReferencia.values().length];
        TipoReferencia[] lista = TipoReferencia.values();
        for (int i = 0; i < lista.length; i++) {
            TipoReferencia tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtTiposDoc() {
        SelectItem[] R = new SelectItem[TipoDocumento.values().length];
        TipoDocumento[] lista = TipoDocumento.values();
        for (int i = 0; i < lista.length; i++) {
            TipoDocumento tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    

    public SelectItem[] obtTiposDireccion() {
        SelectItem[] R = new SelectItem[TipoDireccion.values().length];
        TipoDireccion[] lista = TipoDireccion.values();
        for (int i = 0; i < lista.length; i++) {
            TipoDireccion tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtSistemasAmortizacion() {
        SelectItem[] R = new SelectItem[SistemaAmortizacion.values().length];
        SistemaAmortizacion[] lista = SistemaAmortizacion.values();
        for (int i = 0; i < lista.length; i++) {
            SistemaAmortizacion tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtPeriodosPago() {
        SelectItem[] R = new SelectItem[PeriodoPago.values().length];
        PeriodoPago[] lista = PeriodoPago.values();
        for (int i = 0; i < lista.length; i++) {
            PeriodoPago tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

   
    
   

    public SelectItem[] obtEstadosCivil() {
        SelectItem[] R = new SelectItem[EstadoCivil.values().length];
        EstadoCivil[] lista = EstadoCivil.values();
        for (int i = 0; i < lista.length; i++) {
            EstadoCivil tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }
    
  
    public SelectItem[] obtTiposContactosTelefonicos() {
        SelectItem[] R = new SelectItem[TipoContactoTelefonico.values().length];
        TipoContactoTelefonico[] lista = TipoContactoTelefonico.values();
        for (int i = 0; i < lista.length; i++) {
            TipoContactoTelefonico tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }

    public SelectItem[] obtEstadosContactosTelefonicos() {
        SelectItem[] R = new SelectItem[EstadoContactoTelefonico.values().length];
        EstadoContactoTelefonico[] lista = EstadoContactoTelefonico.values();
        for (int i = 0; i < lista.length; i++) {
            EstadoContactoTelefonico tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }
    
    public SelectItem[] obtCategoria() {
        SelectItem[] R = new SelectItem[Calificacion.values().length];
        Calificacion[] lista = Calificacion.values();
        for (int i = 0; i < lista.length; i++) {
            Calificacion tp = lista[i];
            R[i] = new SelectItem(tp, tp.toString());
        }
        return R;
    }
   
}

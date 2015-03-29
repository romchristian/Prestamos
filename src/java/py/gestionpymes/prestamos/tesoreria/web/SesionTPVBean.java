/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.persistencia.Usuario;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.UsuarioLogueado;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.servicio.MetodoPagoDAO;
import py.gestionpymes.prestamos.tesoreria.dao.PuntoVentaDAO;
import py.gestionpymes.prestamos.tesoreria.dao.ResumenTransaccion;
import py.gestionpymes.prestamos.tesoreria.persisitencia.PuntoVenta;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoMetodoPago;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoValorEfectivo;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorEfectivo;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorMoneda;

import py.gestionpymes.prestamos.tesoreria.dao.SesionTPVDAO;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Transaccion;

/**
 *
 * @author christian
 */
@Named
@javax.enterprise.context.SessionScoped
public class SesionTPVBean extends BeanGenerico<SesionTPV> implements Serializable {

    @EJB
    private SesionTPVDAO ejb;
    @EJB
    private MetodoPagoDAO ejbMetodoPago;
    private ValorMoneda valorMoneda;
    @EJB
    private TransaccionDAO transaccionDAO;
    private BigDecimal totalTransacciones = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoTeorico = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoCierre = new BigDecimal(BigInteger.ZERO);
    private BigDecimal diferencia = new BigDecimal(BigInteger.ZERO);
    @Inject
    private Credencial credencial;
    @EJB
    private PuntoVentaDAO puntoVentaDAO;
    private List<Transaccion> transacciones;
    private List<ResumenTransaccion> resumenTransacciones;

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public List<ResumenTransaccion> getResumenTransacciones() {
        return resumenTransacciones;
    }

    public void setResumenTransacciones(List<ResumenTransaccion> resumenTransacciones) {
        this.resumenTransacciones = resumenTransacciones;
    }

        
    
    @Override
    public SesionTPV getActual() {
        SesionTPV R = super.getActual();
        R.setUsuario(credencial.getUsuario());
        PuntoVenta p = puntoVentaDAO.find(credencial.getUsuario());
        R.setPuntoVenta(p);
        R.setSaldoInicial(p.getSaldo());
        return R; //To change body of generated methods, choose Tools | Templates.
    }

    public void actualizaTotalTransacciones() {
        totalTransacciones = transaccionDAO.findSumPorSesion(getActual());
    }

    public BigDecimal getTotalTransacciones() {
        return totalTransacciones;
    }

    public void setTotalTransacciones(BigDecimal totalTransacciones) {
        this.totalTransacciones = totalTransacciones;
    }

    public BigDecimal getSaldoTeorico() {
        if (getActual().getSaldoInicial() == null) {
            getActual().setSaldoInicial(getActual().getPuntoVenta().getSaldo());
        }
        if (getActual().getSaldoInicial() == null) {
            getActual().setSaldoInicial(new BigDecimal(BigInteger.ZERO));
        }
        saldoTeorico = getActual().getSaldoInicial().add(totalTransacciones);
        return saldoTeorico;
    }

    public void setSaldoTeorico(BigDecimal saldoTeorico) {
        this.saldoTeorico = saldoTeorico;
    }

    public BigDecimal getDiferencia() {
        diferencia = saldoTeorico.subtract(saldoCierre).setScale(0, RoundingMode.HALF_EVEN);
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public BigDecimal getSaldoCierre() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for (ValorEfectivo ve : getActual().getValorEfectivos()) {
            BigDecimal moneda = new BigDecimal(ve.getDenominacionMoneda());
            BigDecimal cantidad = new BigDecimal(ve.getCantidad());

            total = total.add(moneda.multiply(cantidad));
        }
        saldoCierre = total;
        return saldoCierre;
    }

    public void setSaldoCierre(BigDecimal saldoCierre) {
        this.saldoCierre = saldoCierre;
    }

    @Override
    public AbstractDAO<SesionTPV> getEjb() {
        return ejb;
    }

    @Override
    public SesionTPV getNuevo() {
        return new SesionTPV();
    }

    @Override
    public String edit() {
        SesionTPV s = ejb.edit(getActual());

        // Simular tickets
        setActual(null);
        return "listado.xhtml";
    }

    public ValorMoneda getValorMoneda() {
        if (valorMoneda == null) {
            valorMoneda = new ValorMoneda();
        }
        return valorMoneda;
    }

    public void setValorMoneda(ValorMoneda valorMoneda) {
        this.valorMoneda = valorMoneda;
    }

    @Override
    public String preparaCreacion() {
        setActual(new SesionTPV());

        //getActual().setUsuario("");
        getActual().setEstado("CREADO");

        return "nuevo.xhtml";
    }

    public String inciaSesion(SesionTPV s) {
        setActual(s);
        getActual().setEstado("ABIERTA");
        ejb.edit(getActual());
        return "terminalCaja.xhtml";
    }

    public String suspendeSesion() {
        getActual().setEstado("SUSPENDIDO");
        ejb.edit(getActual());
        return "listado.xhtml";

    }

    public String preparaCierre() {
        
        transacciones = transaccionDAO.findAllSesionTPV(getActual());
        
        actualizaTotalTransacciones();
        cargaValoresFinales();
        
        resumenTransacciones = ejb.resumenTransaccion(getActual());
        
        return "/tesoreria/sesionTPV/cierre.xhtml";
    }

    public String cierraSesion() {

        System.out.println("DIFERENCIA: " + getDiferencia().setScale(0, RoundingMode.HALF_EVEN));
        BigDecimal dif = getDiferencia().setScale(0, RoundingMode.HALF_EVEN);
        if (dif.compareTo(new BigDecimal(BigInteger.ZERO)) != 0) {
            JsfUtil.addErrorMessage("Tiene diferencias, NO se puede cerrar");
            return null;
        }
        getActual().getPuntoVenta().setSaldo(saldoCierre.setScale(0, RoundingMode.HALF_EVEN));
        getActual().setSaldoCierre(saldoCierre.setScale(0, RoundingMode.HALF_EVEN));
        getActual().setFechaCierre(new Date());
        getActual().setEstado("CERRADA");
        ejb.cierre(getActual());
        return "listado.xhtml";

    }

    public void cargaValoresFinales() {
        if (getActual().getPuntoVenta() != null) {

            getActual().setValorEfectivos(new ArrayList<ValorEfectivo>());

            
            List<MetodoPago> metodos = getActual().getPuntoVenta().getMetodoPagos();

            if (metodos == null || metodos.isEmpty()) {
               
                metodos = ejbMetodoPago.findAll();

            }

            for (MetodoPago m : metodos) {
                

                if (m.getTipoMetodoPago() == TipoMetodoPago.EFECTIVO) {
                    
                    for (ValorMoneda vm : m.getValoresMonedas()) {
                        
                        ValorEfectivo ve = new ValorEfectivo();
                        ve.setDenominacionMoneda(vm.getDenominacion());
                        ve.setCantidad(0);
                        ve.setTipo(TipoValorEfectivo.INICIAL);
                        ve.setSesionTPV(getActual());

                        getActual().getValorEfectivos().add(ve);
                    }
                }
            }

        }
    }

    public void siCamabiaTPV(ValueChangeEvent event) {
        PuntoVenta pv = (PuntoVenta) event.getNewValue();

        if (pv != null) {
            if (getActual().getValorEfectivos() == null) {
                getActual().setValorEfectivos(new ArrayList<ValorEfectivo>());
            }

            

            List<MetodoPago> metodos = pv.getMetodoPagos();

            if (metodos == null || metodos.isEmpty()) {
                
                metodos = ejbMetodoPago.findAll();

            }

            for (MetodoPago m : metodos) {
                

                if (m.getTipoMetodoPago() == TipoMetodoPago.EFECTIVO) {
                    
                    for (ValorMoneda vm : m.getValoresMonedas()) {
                        
                        ValorEfectivo ve = new ValorEfectivo();
                        ve.setDenominacionMoneda(vm.getDenominacion());
                        ve.setCantidad(0);
                        ve.setTipo(TipoValorEfectivo.INICIAL);
                        ve.setSesionTPV(getActual());

                        getActual().getValorEfectivos().add(ve);
                    }
                }
            }

        }

    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().compareToIgnoreCase("valores") == 0) {
        }
        return event.getNewStep();
    }
}

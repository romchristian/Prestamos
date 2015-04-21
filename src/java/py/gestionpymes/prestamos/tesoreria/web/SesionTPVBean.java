/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.tesoreria.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.dao.QueryParameter;
import py.gestionpymes.prestamos.adm.persistencia.Estado;
import py.gestionpymes.prestamos.adm.persistencia.Usuario;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.adm.web.util.MesEnTexto;
import py.gestionpymes.prestamos.adm.web.util.UsuarioLogueado;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.servicio.MetodoPagoDAO;
import py.gestionpymes.prestamos.reportes.jasper.ReporteController;
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
    @Inject
    private ReporteController reporteController;
    private List<SesionTPV> items;
    private PuntoVenta puntoVentaFiltro;
    private String estadoFiltro;
    private Date inicioFiltro;
    private Date finFiltro;

    public PuntoVenta getPuntoVentaFiltro() {
        return puntoVentaFiltro;
    }

    public void setPuntoVentaFiltro(PuntoVenta puntoVentaFiltro) {
        this.puntoVentaFiltro = puntoVentaFiltro;
    }

    public String getEstadoFiltro() {
        return estadoFiltro;
    }

    public void setEstadoFiltro(String estadoFiltro) {
        this.estadoFiltro = estadoFiltro;
    }

   

    public Date getInicioFiltro() {
        return inicioFiltro;
    }

    public void setInicioFiltro(Date inicioFiltro) {
        this.inicioFiltro = inicioFiltro;
    }

    public Date getFinFiltro() {
        return finFiltro;
    }

    public void setFinFiltro(Date finFiltro) {
        this.finFiltro = finFiltro;
    }
    
    
    public void buscar(){
        StringBuilder consulta;
        consulta = new StringBuilder("select s from SesionTPV s where 1=1 ");
        Map<String,Object> params = new HashMap<>();
        
        if(puntoVentaFiltro != null){
            consulta.append(" and s.puntoVenta = :puntoVenta");
            params.put("puntoVenta", puntoVentaFiltro);
        }
        
        if(inicioFiltro != null && finFiltro != null){
            consulta.append(" and s.fechaApertura between :inicio and :fin");
            params.put("inicio", inicioFiltro);
            params.put("fin", finFiltro);
        }
       
        if(estadoFiltro != null && estadoFiltro.length() > 0){
            consulta.append(" and s.estado = :estado ");
            params.put("estado", estadoFiltro);
        }
        
        items = ejb.findAll(consulta.toString(),params);
    }

    public List<SesionTPV> getItems() {
        return items;
    }

    public void setItems(List<SesionTPV> items) {
        this.items = items;
    }
   
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
        if (totalTransacciones == null) {
            actualizaTotalTransacciones();
        }
        if (totalTransacciones == null) {
            totalTransacciones = new BigDecimal(BigInteger.ZERO);
        }
        return totalTransacciones;
    }

    public void setTotalTransacciones(BigDecimal totalTransacciones) {
        this.totalTransacciones = totalTransacciones;
    }

    public BigDecimal getSaldoTeorico() {

        BigDecimal saldoInicial = null;
        if (getActual().getSaldoInicial() == null) {
            saldoInicial = new BigDecimal(BigInteger.ZERO);
        } else {
            saldoInicial = getActual().getSaldoInicial();
        }
        System.out.println("Actual: " + getActual());
        System.out.println("Saldo Inicial: " + getActual().getSaldoInicial());
        System.out.println("Saldo Inicial 2: " + saldoInicial);

        saldoTeorico = saldoInicial.add(getTotalTransacciones());
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
        getActual().setTotalTransacciones(totalTransacciones.setScale(0, RoundingMode.HALF_EVEN));
        getActual().setDiferencia(diferencia.setScale(0, RoundingMode.HALF_EVEN));
        getActual().setFechaCierre(new Date());
        getActual().setEstado("CERRADA");
        
        //imprimeReporteCajaCierre();
        
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
    
    public void imprimeReporteCajaCierre() {
        
       
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idSesionTPV", getActual().getId());//nf.format(selected.getId())
        params.put("cajero", credencial.getUsuario().getNombre() + " " + credencial.getUsuario().getApellido());
        params.put("saldoInicial",(getActual().getSaldoInicial()==null?new BigDecimal(BigInteger.ZERO):getActual().getSaldoInicial().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("totalTransacciones",(getActual().getTotalTransacciones()==null?new BigDecimal(BigInteger.ZERO):getActual().getTotalTransacciones().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("saldoCierre",(getActual().getSaldoCierre()==null?new BigDecimal(BigInteger.ZERO):getActual().getSaldoCierre().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("diferencia",(getActual().getDiferencia()==null?new BigDecimal(BigInteger.ZERO):getActual().getDiferencia().setScale(0, RoundingMode.HALF_EVEN)));


        reporteController.generaPDF(params, "reportes/tesoreria/ReporteTesoreria.jasper", "cierre_caja" + getActual().getPuntoVenta().getNombre());
    }

    public void imprimeReporteCajaCierre(SesionTPV s) {
        
        setActual(s);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idSesionTPV", getActual().getId());//nf.format(selected.getId())
        params.put("cajero", credencial.getUsuario().getNombre() + " " + credencial.getUsuario().getApellido());
        params.put("saldoInicial",(getActual().getSaldoInicial()==null?new BigDecimal(BigInteger.ZERO):getActual().getSaldoInicial().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("totalTransacciones",(getActual().getTotalTransacciones()==null?new BigDecimal(BigInteger.ZERO):getActual().getTotalTransacciones().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("saldoCierre",(getActual().getSaldoCierre()==null?new BigDecimal(BigInteger.ZERO):getActual().getSaldoCierre().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("diferencia",(getActual().getDiferencia()==null?new BigDecimal(BigInteger.ZERO):getActual().getDiferencia().setScale(0, RoundingMode.HALF_EVEN)));


        reporteController.generaPDF(params, "reportes/tesoreria/ReporteTesoreria.jasper", "cierre_caja" + getActual().getPuntoVenta().getNombre());
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

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import py.gestionpymes.prestamos.adm.dao.AbstractDAO;
import py.gestionpymes.prestamos.adm.web.util.BeanGenerico;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.persistencia.ChequeRecibido;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
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
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;
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

    private TreeNode root = new DefaultTreeNode("resumenCierre", null);
    private List<TreeCierre> listaResumen = new ArrayList<>();

    private List<ChequeRecibido> listaChequesRecibidosTCC=new ArrayList<>();
    
    private List<Pago> detalleTrasacciones;

    public List<Pago> getDetalleTrasacciones() {
        return detalleTrasacciones;
    }

    public void setDetalleTrasacciones(List<Pago> detalleTrasacciones) {
        this.detalleTrasacciones = detalleTrasacciones;
    }


    public List<ChequeRecibido> getListaChequesRecibidosTCC() {
        return listaChequesRecibidosTCC;
    }

    public void setListaChequesRecibidosTCC(List<ChequeRecibido> listaChequesRecibidosTCC) {
        this.listaChequesRecibidosTCC = listaChequesRecibidosTCC;
    }
    

    public void cargaListaChequesRecibidosTCC() {

        //listaChequesRecibidosTCC = transaccionDAO.findTccCh(getActual());
        
    }
    public List<TreeCierre> getListaResumen() {
        return listaResumen;
    }

    public void setListaResumen(List<TreeCierre> listaResumen) {
        this.listaResumen = listaResumen;
    }

    public TreeNode getRoot() {
        if (root != null) {
            cargaTreeCierre();
        }
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

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

    public void buscar() {
        StringBuilder consulta;
        consulta = new StringBuilder("select s from SesionTPV s where 1=1 ");
        Map<String, Object> params = new HashMap<>();

        if (puntoVentaFiltro != null) {
            consulta.append(" and s.puntoVenta = :puntoVenta");
            params.put("puntoVenta", puntoVentaFiltro);
        }

        if (inicioFiltro != null && finFiltro != null) {
            consulta.append(" and s.fechaApertura between :inicio and :fin");
            params.put("inicio", inicioFiltro);
            params.put("fin", finFiltro);
        }

        if (estadoFiltro != null && estadoFiltro.length() > 0) {
            consulta.append(" and s.estado = :estado ");
            params.put("estado", estadoFiltro);
        }

        items = ejb.findAll(consulta.toString(), params);
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
        cargaListaChequesRecibidosTCC();                

        resumenTransacciones = ejb.resumenTransaccion(getActual());
        limpiaTotales();

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
        params.put("saldoInicial", (getActual().getSaldoInicial() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoInicial().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("totalTransacciones", (getActual().getTotalTransacciones() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getTotalTransacciones().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("saldoCierre", (getActual().getSaldoCierre() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoCierre().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("diferencia", (getActual().getDiferencia() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getDiferencia().setScale(0, RoundingMode.HALF_EVEN)));

        reporteController.generaPDF(params, "reportes/tesoreria/ReporteTesoreria.jasper", "cierre_caja" + getActual().getPuntoVenta().getNombre());
    }

    public void imprimeReporteCajaCierre(SesionTPV s) {

        setActual(s);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idSesionTPV", s.getId());//nf.format(selected.getId())
        params.put("cajero", credencial.getUsuario().getNombre() + " " + credencial.getUsuario().getApellido());
        params.put("saldoInicial", (s.getSaldoInicial() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoInicial().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("totalTransacciones", (s.getTotalTransacciones() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getTotalTransacciones().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("saldoCierre", (s.getSaldoCierre() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoCierre().setScale(0, RoundingMode.HALF_EVEN)));
        params.put("diferencia", (s.getDiferencia() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getDiferencia().setScale(0, RoundingMode.HALF_EVEN)));

        reporteController.generaPDF(params, "reportes/tesoreria/ReporteTesoreria.jasper", "cierre_caja" + s.getPuntoVenta().getNombre());
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
    
    
    public void abreDetalleTransaccion(TreeCierre t){
       detalleTrasacciones = transaccionDAO.findPagosDetalle(getActual(), t);
        
    }

    public void cargaTreeCierre() {
        root = new DefaultTreeNode("resumenCierre", null);
        TreeNode saldoInicialNode = new DefaultTreeNode(new TreeCierre("Saldo Inicio", null, getActual().getSaldoInicial() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoInicial(), 0, root, "nodoTotal"), root);
        TreeNode cobrosCuotasNode = new DefaultTreeNode(new TreeCierre("Cobros Cuotas (+)", TipoTransaccion.ENTRADA, getTotalCobroCuotas(), 0, root, "nodoTotal",TreeCierre.TCC), root);
        TreeNode cobrosCuotasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (+)", TipoTransaccion.ENTRADA, getTotalCobrosCuotasEfe(), 0, cobrosCuotasNode, "nodoSubTotal",TreeCierre.TCC_EFE), cobrosCuotasNode);
        TreeNode cobrosCuotasChNode = new DefaultTreeNode(new TreeCierre("Cheques (+)", TipoTransaccion.ENTRADA, getTotalCobrosCuotasCh(), 0, cobrosCuotasNode, "nodoSubTotal",TreeCierre.TCC_CH), cobrosCuotasNode);

        List<Object[]> lista = transaccionDAO.findSumPorBanco(getActual(), "TransaccionCobraCuota", "ENTRADA", "ChequeRecibido");

        for (Object[] obj : lista) {
            TreeNode cobrosCuotasChBancoNode = new DefaultTreeNode(new TreeCierre((String) obj[1] + " (+)", TipoTransaccion.ENTRADA,
                    (BigDecimal) obj[2], 0, cobrosCuotasChNode, "nodoSubTotal",TreeCierre.TCC_BANCO,((Integer)obj[0]).longValue()), cobrosCuotasChNode);
        }

        TreeNode entradasVariasNode = new DefaultTreeNode(new TreeCierre("Entradas Varias (+)", TipoTransaccion.ENTRADA, getTotalEntradasVarias(), 0, root, "nodoTotal"), root);
        TreeNode entradasVariasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (+)", TipoTransaccion.ENTRADA, getTotalEntradasVariasEfe(), 0, entradasVariasNode, "nodoSubTotal"), entradasVariasNode);
        TreeNode desembolsosNode = new DefaultTreeNode(new TreeCierre("Desembolsos (-)", TipoTransaccion.SALIDA, getTotalDesembolsos(), 0, root, "nodoTotal"), root);
        TreeNode desembolsosEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (-)", TipoTransaccion.SALIDA, getTotalDesembolsosEfe(), 0, desembolsosNode, "nodoSubTotal"), desembolsosNode);
        TreeNode salidasVariasNode = new DefaultTreeNode(new TreeCierre("Salidas Varias (-)", TipoTransaccion.SALIDA, getTotalSalidasVarias(), 0, root, "nodoTotal"), root);
        TreeNode salidasVariasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (-)", TipoTransaccion.SALIDA, getTotalSalidasVariasEfe(), 0, salidasVariasNode, "nodoSubTotal"), salidasVariasNode);

    }

    private void limpiaTotales() {
        totalCobroCuotas = null;
        totalCobrosCuotasEfe = null;
        totalCobrosCuotasCh = null;
        totalEntradasVarias = null;
        totalEntradasVariasEfe = null;
        totalSalidasVarias = null;
        totalSalidasVariasEfe = null;
        totalDesembolsos = null;
        totalDesembolsosEfe = null;
    }
    private BigDecimal totalCobroCuotas;
    private BigDecimal totalCobrosCuotasEfe;
    private BigDecimal totalCobrosCuotasCh;
    private BigDecimal totalEntradasVarias;
    private BigDecimal totalEntradasVariasEfe;
    private BigDecimal totalSalidasVarias;
    private BigDecimal totalSalidasVariasEfe;
    private BigDecimal totalDesembolsos;
    private BigDecimal totalDesembolsosEfe;

    public BigDecimal getTotalCobroCuotas() {
        if (totalCobroCuotas == null) {
            totalCobroCuotas = obtTotalTransaccion("TransaccionCobraCuota", "ENTRADA");
        }
        return totalCobroCuotas;
    }

    public void setTotalCobroCuotas(BigDecimal totalCobroCuotas) {
        this.totalCobroCuotas = totalCobroCuotas;
    }

    public BigDecimal getTotalCobrosCuotasEfe() {
        if (totalCobrosCuotasEfe == null) {
            totalCobrosCuotasEfe = obtTotalPago("TransaccionCobraCuota", "ENTRADA", "Efectivo");
        }
        return totalCobrosCuotasEfe;
    }

    public void setTotalCobrosCuotasEfe(BigDecimal totalCobrosCuotasEfe) {
        this.totalCobrosCuotasEfe = totalCobrosCuotasEfe;
    }

    public BigDecimal getTotalCobrosCuotasCh() {
        if (totalCobrosCuotasCh == null) {
            totalCobrosCuotasCh = obtTotalPago("TransaccionCobraCuota", "ENTRADA", "ChequeRecibido");
        }
        return totalCobrosCuotasCh;
    }

    public void setTotalCobrosCuotasCh(BigDecimal totalCobrosCuotasCh) {
        this.totalCobrosCuotasCh = totalCobrosCuotasCh;
    }

    public BigDecimal getTotalEntradasVarias() {
        if (totalEntradasVarias == null) {
            totalEntradasVarias = obtTotalTransaccion("Transaccion", "ENTRADA");
        }
        return totalEntradasVarias;
    }

    public void setTotalEntradasVarias(BigDecimal totalEntradasVarias) {
        this.totalEntradasVarias = totalEntradasVarias;
    }

    public BigDecimal getTotalEntradasVariasEfe() {
        if (totalEntradasVariasEfe == null) {
            totalEntradasVariasEfe = obtTotalPago("Transaccion", "ENTRADA", "Efectivo");
        }
        return totalEntradasVariasEfe;
    }

    public void setTotalEntradasVariasEfe(BigDecimal totalEntradasVariasEfe) {
        this.totalEntradasVariasEfe = totalEntradasVariasEfe;
    }

    public BigDecimal getTotalSalidasVarias() {
        if (totalSalidasVarias == null) {
            totalSalidasVarias = obtTotalTransaccion("Transaccion", "SALIDA");
        }
        return totalSalidasVarias;
    }

    public void setTotalSalidasVarias(BigDecimal totalSalidasVarias) {
        this.totalSalidasVarias = totalSalidasVarias;
    }

    public BigDecimal getTotalSalidasVariasEfe() {
        if (totalSalidasVariasEfe == null) {
            totalSalidasVariasEfe = obtTotalPago("Transaccion", "SALIDA", "Efectivo");
        }
        return totalSalidasVariasEfe;
    }

    public void setTotalSalidasVariasEfe(BigDecimal totalSalidasVariasEfe) {
        this.totalSalidasVariasEfe = totalSalidasVariasEfe;
    }

    public BigDecimal getTotalDesembolsos() {
        if (totalDesembolsos == null) {
            totalDesembolsos = obtTotalTransaccion("TransaccionDesembolso", "SALIDA");
        }
        return totalDesembolsos;
    }

    public void setTotalDesembolsos(BigDecimal totalDesembolsos) {
        this.totalDesembolsos = totalDesembolsos;
    }

    public BigDecimal getTotalDesembolsosEfe() {
        if (totalDesembolsosEfe == null) {
            totalDesembolsosEfe = obtTotalPago("TransaccionDesembolso", "SALIDA", "Efectivo");
        }
        return totalDesembolsosEfe;
    }

    public void setTotalDesembolsosEfe(BigDecimal totalDesembolsosEfe) {
        this.totalDesembolsosEfe = totalDesembolsosEfe;
    }

    public BigDecimal obtTotalTransaccion(String dtype, String tipo) {
        return transaccionDAO.findSumTransaccion(getActual(), dtype, tipo);
    }

    public BigDecimal obtTotalPago(String dtype, String tipo, String pagoDtype) {
        return transaccionDAO.findSumPago(getActual(), dtype, tipo, pagoDtype);
    }

}

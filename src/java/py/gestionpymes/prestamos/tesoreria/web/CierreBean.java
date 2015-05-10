/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.web.util.Credencial;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.persistencia.ChequeRecibido;
import py.gestionpymes.prestamos.contabilidad.persistencia.MetodoPago;
import py.gestionpymes.prestamos.contabilidad.persistencia.Pago;
import py.gestionpymes.prestamos.contabilidad.servicio.MetodoPagoDAO;
import py.gestionpymes.prestamos.reportes.jasper.ReporteController;
import py.gestionpymes.prestamos.tesoreria.dao.SesionTPVDAO;
import py.gestionpymes.prestamos.tesoreria.dao.TransaccionDAO;
import py.gestionpymes.prestamos.tesoreria.persisitencia.SesionTPV;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoMetodoPago;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoTransaccion;
import py.gestionpymes.prestamos.tesoreria.persisitencia.TipoValorEfectivo;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorEfectivo;
import py.gestionpymes.prestamos.tesoreria.persisitencia.ValorMoneda;

/**
 *
 * @author Acer
 */
@Named
@ViewScoped
public class CierreBean implements Serializable {

    @EJB
    private SesionTPVDAO ejb;
    @EJB
    private MetodoPagoDAO ejbMetodoPago;
    private ValorMoneda valorMoneda;
    @EJB
    private TransaccionDAO transaccionDAO;
    @Inject
    private SesionTPVActual stpva;
    @Inject
    private Credencial credencial;
    @Inject
    private ReporteController reporteController;
    private SesionTPV actual;
    private BigDecimal totalCobroCuotas;
    private BigDecimal totalCobrosCuotasEfe;
    private BigDecimal totalCobrosCuotasCh;
    private BigDecimal totalEntradasVarias;
    private BigDecimal totalEntradasVariasEfe;
    private BigDecimal totalSalidasVarias;
    private BigDecimal totalSalidasVariasEfe;
    private BigDecimal totalDesembolsos;
    private BigDecimal totalDesembolsosEfe;
    private BigDecimal totalTransacciones = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoTeorico = new BigDecimal(BigInteger.ZERO);
    private BigDecimal saldoCierre = new BigDecimal(BigInteger.ZERO);
    private BigDecimal diferencia = new BigDecimal(BigInteger.ZERO);
    private List<TreeCierre> valoresBanco;

    private TreeNode root = new DefaultTreeNode("resumenCierre", null);
    private List<TreeCierre> listaResumen = new ArrayList<>();

    private List<ChequeRecibido> listaChequesRecibidosTCC = new ArrayList<>();

    private List<Pago> detalleTrasacciones;

    public void cargaDatos() {
        cargaValoresFinales();
        cargaListaChequesRecibidosTCC();

        limpiaTotales();
    }

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

    public SesionTPV getActual() {
        if (actual == null) {
            actual = stpva.getSesion();
        }
        return actual;
    }

    public void setActual(SesionTPV actual) {
        this.actual = actual;
    }

    public BigDecimal getTotalTransacciones() {
        if (totalTransacciones == null || totalTransacciones.compareTo(new BigDecimal(BigInteger.ZERO)) == 0) {
            totalTransacciones = transaccionDAO.findSumPorSesion(getActual());
        }
        if (totalTransacciones == null) {
            totalTransacciones = new BigDecimal(BigInteger.ZERO);
        }
        return totalTransacciones;
    }

    
    public BigDecimal obtSaldoTeoricoEfe() {
        return getSaldoTeorico().subtract(obtSaldoTeoricoCh());
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
        diferencia = getSaldoTeorico().subtract(getSaldoCierre()).setScale(0, RoundingMode.HALF_EVEN);
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    
    public BigDecimal obtSaldoTeoricoCh(){
       BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for(TreeCierre ch: valoresBanco){
                total = total.add(ch.getMonto());
        }
      return total;
    }
    
    public BigDecimal obtSaldoCierreEfe() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        
        for (ValorEfectivo ve : getActual().getValorEfectivos()) {
            BigDecimal moneda = new BigDecimal(ve.getDenominacionMoneda());
            BigDecimal cantidad = new BigDecimal(ve.getCantidad());

            total = total.add(moneda.multiply(cantidad));
        }
        
        return total;
    }
    
    
    public BigDecimal obtSaldoCierreCh() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for(TreeCierre ch: valoresBanco){
            if(ch.isConfirmado()){
                total = total.add(ch.getMonto());
            }
        }
        
        return total;
    }
    
    public BigDecimal getSaldoCierre() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for(TreeCierre ch: valoresBanco){
            if(ch.isConfirmado()){
                total = total.add(ch.getMonto());
            }
        }
        
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

    public String cierraSesion() {

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

    public void abreDetalleTransaccion(TreeCierre t) {
        System.out.println("Empieza_2: " + new Date());
        detalleTrasacciones = transaccionDAO.findPagosDetalle(getActual(), t);
        System.out.println("Empieza_2: " + new Date());

    }

    public List<TreeCierre> getValoresBanco() {
        if (valoresBanco == null) {
            valoresBanco = new ArrayList<>();
            List<Object[]> lista = transaccionDAO.findSumPorBanco(getActual(), "TransaccionCobraCuota", "ENTRADA", "ChequeRecibido");
            for (Object[] obj : lista) {
                TreeCierre tc = new TreeCierre((String) obj[1] + " (+)", TipoTransaccion.ENTRADA,
                        (BigDecimal) obj[2], 0, null, "nodoSubTotal", TreeCierre.TCC_BANCO, ((Integer) obj[0]).longValue());

                tc.setCantidad(((Long) obj[3]).intValue());

                valoresBanco.add(tc);

            }
        }
        return valoresBanco;
    }

    public void setValoresBanco(List<TreeCierre> valoresBanco) {
        this.valoresBanco = valoresBanco;
    }

    public void cargaTreeCierre() {
        root = new DefaultTreeNode("resumenCierre", null);
        TreeNode saldoInicialNode = new DefaultTreeNode(new TreeCierre("Saldo Inicio", null, getActual().getSaldoInicial() == null ? new BigDecimal(BigInteger.ZERO) : getActual().getSaldoInicial(), 0, root, "nodoTotal"), root);
        TreeNode cobrosCuotasNode = new DefaultTreeNode(new TreeCierre("Cobros Cuotas (+)", TipoTransaccion.ENTRADA, transaccionDAO.getTotalCobroCuotas(getActual()), 0, root, "nodoTotal", TreeCierre.TCC), root);
        TreeNode cobrosCuotasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (+)", TipoTransaccion.ENTRADA, transaccionDAO.getTotalCobrosCuotasEfe(getActual()), 0, cobrosCuotasNode, "nodoSubTotal", TreeCierre.TCC_EFE), cobrosCuotasNode);
        TreeNode cobrosCuotasChNode = new DefaultTreeNode(new TreeCierre("Cheques (+)", TipoTransaccion.ENTRADA, transaccionDAO.getTotalCobrosCuotasCh(getActual()), 0, cobrosCuotasNode, "nodoSubTotal", TreeCierre.TCC_CH), cobrosCuotasNode);

        List<Object[]> lista = transaccionDAO.findSumPorBanco(getActual(), "TransaccionCobraCuota", "ENTRADA", "ChequeRecibido");

        //valoresBanco = new ArrayList<>();
        for (Object[] obj : lista) {
            TreeCierre tc = new TreeCierre((String) obj[1] + " (+)", TipoTransaccion.ENTRADA,
                    (BigDecimal) obj[2], 0, cobrosCuotasChNode, "nodoSubTotal", TreeCierre.TCC_BANCO, ((Integer) obj[0]).longValue());

            tc.setCantidad(((Long) obj[3]).intValue());

            

            TreeNode cobrosCuotasChBancoNode = new DefaultTreeNode(tc, cobrosCuotasChNode);
        }

        TreeNode entradasVariasNode = new DefaultTreeNode(new TreeCierre("Entradas Varias (+)", TipoTransaccion.ENTRADA, transaccionDAO.getTotalEntradasVarias(getActual()), 0, root, "nodoTotal"), root);
        TreeNode entradasVariasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (+)", TipoTransaccion.ENTRADA, transaccionDAO.getTotalEntradasVariasEfe(getActual()), 0, entradasVariasNode, "nodoSubTotal"), entradasVariasNode);
        TreeNode desembolsosNode = new DefaultTreeNode(new TreeCierre("Desembolsos (-)", TipoTransaccion.SALIDA, transaccionDAO.getTotalDesembolsos(getActual()), 0, root, "nodoTotal"), root);
        TreeNode desembolsosEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (-)", TipoTransaccion.SALIDA, transaccionDAO.getTotalDesembolsosEfe(getActual()), 0, desembolsosNode, "nodoSubTotal"), desembolsosNode);
        TreeNode salidasVariasNode = new DefaultTreeNode(new TreeCierre("Salidas Varias (-)", TipoTransaccion.SALIDA, transaccionDAO.getTotalSalidasVarias(getActual()), 0, root, "nodoTotal"), root);
        TreeNode salidasVariasEfeNode = new DefaultTreeNode(new TreeCierre("Efectivo (-)", TipoTransaccion.SALIDA, transaccionDAO.getTotalSalidasVariasEfe(getActual()), 0, salidasVariasNode, "nodoSubTotal"), salidasVariasNode);

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

   

}

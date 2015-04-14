/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.joda.time.DateTime;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.adm.persistencia.Empresa;
import py.gestionpymes.prestamos.adm.persistencia.Estado;
import py.gestionpymes.prestamos.adm.persistencia.Sucursal;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.persistencia.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.dao.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.dao.NumeroInvalidoException;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.Prestamo;
import py.gestionpymes.prestamos.prestamos.persistencia.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Secuencia;
import py.gestionpymes.prestamos.tesoreria.web.SesionTPVBean;

/**
 *
 * @author cromero
 */
@Named(value = "desembolsaBean")
@FlowScoped(value = "desembolsa")
public class DesembolsaBean implements Serializable {

    @EJB
    private PrestamoDAO prestamoDAO;
    @EJB
    private MonedaFacade monedaDAO;
    @EJB
    private CobranzaDAO cobranzaDAO;
    @Inject
    private SesionTPVBean sesionTPVBean;

    private FacturaVenta facturaVenta;
    private Prestamo selected;

    private Empresa empresaFiltro;
    private Sucursal sucursalFiltro;
    private Date inicioFiltro;
    private Date finFiltro;
    private EstadoPrestamo estadoPrestamoFiltro;
    private Cliente clienteFiltro;
    private List<Prestamo> items = null;
    
    
    @PostConstruct
    public void inint(){
     DateTime dt = DateTime.now();
        dt.dayOfMonth().getMaximumValue();
        this.inicioFiltro = new DateTime(dt.getYear(), dt.getMonthOfYear(), 1, 0,0).toDate();
        this.finFiltro = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.dayOfMonth().getMaximumValue(), 0,0).toDate();
    }

    public boolean haySeleccion() {
        return getSelected().getId() != null;
    }

    public Prestamo getSelected() {
        if (selected == null) {
            selected = new Prestamo();
        }
        return selected;
    }

    public void setSelected(Prestamo selected) {
        this.selected = selected;
    }

    public Empresa getEmpresaFiltro() {
        return empresaFiltro;
    }

    public void setEmpresaFiltro(Empresa empresaFiltro) {
        this.empresaFiltro = empresaFiltro;
    }

    public Sucursal getSucursalFiltro() {
        return sucursalFiltro;
    }

    public void setSucursalFiltro(Sucursal sucursalFiltro) {
        this.sucursalFiltro = sucursalFiltro;
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

    public EstadoPrestamo getEstadoPrestamoFiltro() {
        return estadoPrestamoFiltro;
    }

    public void setEstadoPrestamoFiltro(EstadoPrestamo estadoPrestamoFiltro) {
        this.estadoPrestamoFiltro = estadoPrestamoFiltro;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente clienteFiltro) {
        this.clienteFiltro = clienteFiltro;
    }

    public List<Prestamo> getItems() {
        return items;
    }

    public void setItems(List<Prestamo> items) {
        this.items = items;
    }

    public List<Prestamo> getPrestamosADesembolsar() {
        return items;
    }

    public void buscarADesembolsar() {
        items = prestamoDAO.findAllPorEmpresaFechaEstadoCliente(empresaFiltro, sucursalFiltro, EstadoPrestamo.EN_DESEMBOLSO, inicioFiltro, finFiltro, clienteFiltro);
    }

    public FacturaVenta getFacturaVenta() {
        if (facturaVenta == null) {
            facturaVenta = new FacturaVenta();
        }
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    public String desembolsa() {
        return generaFactura();
    }

    public String generaFactura() {

        if (sesionTPVBean.getActual() == null) {
            return null;
        }

        Secuencia secuencia = sesionTPVBean.getActual().getPuntoVenta().getSecuencia();
        if (secuencia.getEstado() == Estado.INACTIVO) {
            JsfUtil.addErrorMessage("Se agotó su talonario!");
            return null;
        }

        facturaVenta = new FacturaVenta();
        facturaVenta.setCodEstablecimiento(secuencia.getEstablecimiento());
        facturaVenta.setPuntoExpedicion(secuencia.getPuntoExpedicion());
        facturaVenta.setTimbrado(secuencia.getTimbrado());
        Long numeroactual = secuencia.obtSiguienteNumero();
        facturaVenta.setNumero(secuencia.getNumeroFormateado(numeroactual));
        

        Cliente c = selected.getCliente();
        facturaVenta.setCliente(c);
        facturaVenta.setEmpresa(selected.getEmpresa());
        facturaVenta.setSucursal(selected.getSucursal());

        facturaVenta.setFechaCreacion(new Date());
        facturaVenta.setFechaEmision(new Date());
        facturaVenta.setDireccion(c.devuelveDireccionParticular());
        facturaVenta.setRazonSocial(c.devuelveNombreCompleto());
        facturaVenta.setRuc(c.getNroDocumento());

        facturaVenta.setMoneda(selected.getMoneda());

        facturaVenta.setDetalles(new ArrayList<FacturaVentaDetalle>());

        FacturaVentaDetalle d1 = new FacturaVentaDetalle();
        d1.setFacturaVenta(facturaVenta);
        d1.setNrolinea(1);
        d1.setCantidad(new BigDecimal(BigInteger.ONE));
        d1.setDescripcion("Gastos Administrativos por préstamo #" + selected.getId());
        d1.setPrecioUnitario(selected.getGastos());
        d1.setGravada10(d1.getCantidad().multiply(d1.getPrecioUnitario()));
        d1.setRefMonto(FacturaVentaDetalle.MONTO_GASTOS);

        facturaVenta.getDetalles().add(d1);

        FacturaVentaDetalle d2 = new FacturaVentaDetalle();
        d2.setFacturaVenta(facturaVenta);
        d2.setNrolinea(2);
        d2.setCantidad(new BigDecimal(BigInteger.ONE));

        d2.setDescripcion("Comisión de cobranza por préstamo #" + selected.getId());
        d2.setPrecioUnitario(selected.getComisiones());
        d2.setGravada10(d2.getCantidad().multiply(d2.getPrecioUnitario()));
        d2.setRefMonto(FacturaVentaDetalle.MONTO_COMISIONES);

        facturaVenta.getDetalles().add(d2);

        BigDecimal totalGravadas = selected.getComisiones().add(selected.getGastos());

        BigDecimal total = totalGravadas;
        BigDecimal iva05 = new BigDecimal(BigInteger.ZERO);
        BigDecimal iva10 = totalGravadas.divide(new BigDecimal(11), 0, RoundingMode.HALF_EVEN);
        BigDecimal ivatotal = iva05.add(iva10);

        facturaVenta.setTotalExento(new BigDecimal(BigInteger.ZERO));
        facturaVenta.setTotalGravada05(new BigDecimal(BigInteger.ZERO));
        facturaVenta.setTotalGravada10(totalGravadas.setScale(0, RoundingMode.HALF_EVEN));
        facturaVenta.setTotal(total.setScale(0, RoundingMode.HALF_EVEN));
        facturaVenta.setIva05(new BigDecimal(BigInteger.ZERO));
        facturaVenta.setIva10(iva10.setScale(0, RoundingMode.HALF_EVEN));
        facturaVenta.setTotalIva(ivatotal.setScale(0, RoundingMode.HALF_EVEN));

        limpia();
        return "/desembolsa/creaFactura";
    }

    public void paga() {

        try {
            prestamoDAO.desembolsa(selected, facturaVenta, sesionTPVBean.getActual());
            sesionTPVBean.actualizaTotalTransacciones();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }

    }

    public void limpia() {
        buscarADesembolsar();
    }
}

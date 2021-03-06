/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gestionpymes.prestamos.adm.dao.CargoPorMoraDAO;
import py.gestionpymes.prestamos.adm.dao.MonedaFacade;
import py.gestionpymes.prestamos.adm.modelo.CargoPorMora;
import py.gestionpymes.prestamos.adm.modelo.Estado;
import py.gestionpymes.prestamos.adm.web.util.JsfUtil;
import py.gestionpymes.prestamos.contabilidad.modelo.ChequeRecibido;
import py.gestionpymes.prestamos.contabilidad.modelo.Efectivo;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.contabilidad.modelo.Pago;
import py.gestionpymes.prestamos.contabilidad.modelo.TipoPago;
import py.gestionpymes.prestamos.contabilidad.servicio.CobranzaDAO;
import py.gestionpymes.prestamos.contabilidad.web.ProductorFacturaPagoCuota;
import py.gestionpymes.prestamos.prestamos.dao.MontoCancelacionIncorrectoException;
import py.gestionpymes.prestamos.prestamos.dao.NumeroInvalidoException;
import py.gestionpymes.prestamos.prestamos.dao.PagoExcedidoException;
import py.gestionpymes.prestamos.prestamos.dao.PrestamoDAO;
import py.gestionpymes.prestamos.prestamos.modelo.AplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.EstadoPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.Prestamo;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;
import py.gestionpymes.prestamos.tesoreria.web.SesionTPVBean;

/**
 *
 * @author cromero
 */
@Named(value = "cobraCuotaBean")
@FlowScoped(value = "cobraCuota")
public class CobraCuotaBean implements Serializable {

    @EJB
    private PrestamoDAO prestamoDAO;
    @EJB
    private MonedaFacade monedaDAO;
    @EJB
    private CobranzaDAO cobranzaDAO;
    @Inject
    private SesionTPVBean sesionTPVBean;
    @EJB
    private CargoPorMoraDAO cagoDAO;
    @Inject
    private ProductorFacturaPagoCuota productorFactura;

    private TreeNode root = new DefaultTreeNode("prestamos", null);
    private Cliente cliente;
    private List<TreeCuota> seleccionados = new ArrayList<TreeCuota>();
    private List<TreeCuota> disponibles = new ArrayList<TreeCuota>();
    List<AplicacionPagoCuota> aplicacionPagoCuotas = new ArrayList<>();
    private BigDecimal totalAPagar;
    private TreeCuota cuotaSeleccionada;
    private BigDecimal montoActual;
    private TipoPago tipoPago;

    private FacturaVenta facturaVenta;
    private Efectivo efectivo;
    private ChequeRecibido chequeRecibido;

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Efectivo getEfectivo() {

        if (efectivo == null) {
            efectivo = new Efectivo();
        }

        return efectivo;
    }

    public void setEfectivo(Efectivo efectivo) {
        this.efectivo = efectivo;
    }

    public ChequeRecibido getChequeRecibido() {
        if (chequeRecibido == null) {
            chequeRecibido = new ChequeRecibido();
        }
        return chequeRecibido;
    }

    public void removerPago(Pago p) {

        int indice = 0;
        String descripcion2 = p.getDescripcion();
        BigDecimal monto2 = p.getMonto();

        for (Pago pa : facturaVenta.getPagos()) {
            String descripcion1 = pa.getDescripcion();
            BigDecimal monto1 = pa.getMonto();

            if (descripcion1.compareToIgnoreCase(descripcion2) == 0 && monto1.compareTo(monto2) == 0) {
                break;
            }

            indice++;
        }

        facturaVenta.getPagos().remove(indice);
    }

    public void agregaEfectivo() {
        if (facturaVenta.getPagos() == null) {
            facturaVenta.setPagos(new ArrayList<Pago>());
        }
        if (efectivo.getMontoPagado().compareTo(obtRestante()) < 0) {
            efectivo.setMonto(efectivo.getMontoPagado());
            efectivo.setCambio(new BigDecimal(BigInteger.ZERO));
        } else {
            efectivo.setMonto(obtRestante());
        }

        efectivo.setFecha(facturaVenta.getFechaEmision());
        efectivo.setMoneda(facturaVenta.getMoneda());
        efectivo.setFacturaVenta(facturaVenta);
        facturaVenta.getPagos().add(efectivo);

    }

    public void agregaChequeRecibido() {
        if (facturaVenta.getPagos() == null) {
            facturaVenta.setPagos(new ArrayList<Pago>());
        }
        chequeRecibido.setFecha(facturaVenta.getFechaEmision());
        chequeRecibido.setMoneda(facturaVenta.getMoneda());
        chequeRecibido.setFacturaVenta(facturaVenta);
        if (chequeRecibido.getBanco() != null) {
            chequeRecibido.setDescripcion(chequeRecibido.getBanco().getNombre() + "-" + chequeRecibido.getNumero() + "-" + chequeRecibido.getLibrador());
        } else {
            chequeRecibido.setDescripcion(chequeRecibido.getNumero() + "-" + chequeRecibido.getLibrador());
        }
        facturaVenta.getPagos().add(chequeRecibido);

    }

    public void setChequeRecibido(ChequeRecibido chequeRecibido) {
        this.chequeRecibido = chequeRecibido;
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

    public String obtDescCuota() {
        String R = "";
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            NumberFormat nf = NumberFormat.getInstance(new Locale("es", "py"));
            R = cuotaSeleccionada.getPrestamo().getId() + ";" + nf.format(cuotaSeleccionada.getPrestamo().getMontoPrestamo()) + " - Cuota Nro "
                    + cuotaSeleccionada.getDetPrestamo().getNroCuota();
        }
        return R;
    }

    public String obtDescMora() {
        String R = "";
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            R = cuotaSeleccionada.getDetPrestamo().getDiasMora() < 0 ? 0 + " días" : cuotaSeleccionada.getDetPrestamo().getDiasMora() + " días";
        }
        return R;
    }

    public Date obtUltimoPago() {
        Date R = null;
        if (cuotaSeleccionada != null && cuotaSeleccionada.getPrestamo() != null) {
            R = cuotaSeleccionada.getPrestamo().getUltimoPago();

        }

        return R;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    public TreeCuota getCuotaSeleccionada() {
        if (cuotaSeleccionada == null) {
            cuotaSeleccionada = new TreeCuota();
        }
        return cuotaSeleccionada;
    }

    public void setCuotaSeleccionada(TreeCuota cuotaSeleccionada) {

        this.cuotaSeleccionada = cuotaSeleccionada;
        montoActual = cuotaSeleccionada == null ? new BigDecimal(BigInteger.ZERO) : cuotaSeleccionada.getSaldoCuota();
    }

    public void seleccionaEfectivo() {
        tipoPago = TipoPago.EFECTIVO;
        seleccionaTipoPago();
    }

    public void seleccionaCheque() {
        tipoPago = TipoPago.CHEQUE_RECIBIDO;
        seleccionaTipoPago();
    }

    public BigDecimal obtTotalPagado() {
        BigDecimal totalPagado = new BigDecimal(BigInteger.ZERO);
        if (facturaVenta.getPagos() != null) {
            for (Pago p : facturaVenta.getPagos()) {
                if (p instanceof Efectivo) {
                    Efectivo e = (Efectivo) p;
                    totalPagado = totalPagado.add(e.getMontoPagado());
                } else {
                    totalPagado = totalPagado.add(p.getMonto());
                }

            }
        }

        return totalPagado;
    }

    public BigDecimal obtCambio() {
        if (totalAPagar == null) {
            return new BigDecimal(BigInteger.ZERO);
        }
        BigDecimal totalPagado = obtTotalPagado();

        BigDecimal R = totalPagado.subtract(totalAPagar);

        if (R.compareTo(new BigDecimal(BigInteger.ZERO)) < 0) {
            return new BigDecimal(BigInteger.ZERO);
        } else {
            return R;
        }

    }

    public BigDecimal obtRestante() {
        if (totalAPagar == null) {
            return new BigDecimal(BigInteger.ZERO);
        }

        BigDecimal R = totalAPagar.subtract(obtTotalPagado());
        if (R.compareTo(new BigDecimal(BigInteger.ZERO)) < 0) {
            return new BigDecimal(BigInteger.ZERO);
        } else {
            return R;
        }
    }

    public void seleccionaTipoPago() {

        if (tipoPago != null) {
            RequestContext context = RequestContext.getCurrentInstance();

            BigDecimal totalPagado = new BigDecimal(BigInteger.ZERO);
            if (facturaVenta.getPagos() != null) {
                for (Pago p : facturaVenta.getPagos()) {
                    totalPagado = totalPagado.add(p.getMonto());
                }
            }

            BigDecimal saldo = facturaVenta.getTotal().subtract(totalPagado);

            switch (tipoPago) {
                case CHEQUE_RECIBIDO:
                    chequeRecibido = new ChequeRecibido();
                    chequeRecibido.setMonto(saldo);
                    context.execute("PF('dialogCheque').show()");
                    break;
                case EFECTIVO:
                    efectivo = new Efectivo();
                    efectivo.setMonto(saldo);
                    efectivo.setMontoPagado(saldo);
                    context.execute("PF('dialogEfectivo').show()");
                    break;

            }

        }
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

        facturaVenta = productorFactura.genera(cuotaSeleccionada.getSucursal(),
                cliente,
                secuencia,
                cuotaSeleccionada.getMoneda(),
                seleccionados);
        
        aplicacionPagoCuotas = productorFactura.getAplicacionesPagoCuotas();


        limpia();
        return "/cobraCuota/creaFactura";
    }

    public String generaFactura2() {

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
        System.out.println("Sgte Nro: " + numeroactual);
        facturaVenta.setNumero(secuencia.getNumeroFormateado(numeroactual));
        System.out.println("Sgte Nro 2: " + numeroactual);

        facturaVenta.setCliente(cliente);
        facturaVenta.setEmpresa(cuotaSeleccionada.getEmpresa());
        facturaVenta.setSucursal(cuotaSeleccionada.getSucursal());

        facturaVenta.setFechaCreacion(new Date());
        facturaVenta.setFechaEmision(new Date());
        facturaVenta.setDireccion(cliente.devuelveDireccionParticular());
        facturaVenta.setRazonSocial(cliente.devuelveNombreCompleto());
        facturaVenta.setRuc(cliente.getNroDocumento());
        facturaVenta.setMoneda(cuotaSeleccionada.getMoneda());

        facturaVenta.setDetalles(new ArrayList<FacturaVentaDetalle>());
        int nrolinea = 1;
        for (TreeCuota t : seleccionados) {
            BigDecimal aplicaAMoratorio;
            BigDecimal aplicaAPunitorio;
            BigDecimal aplicaACuota = new BigDecimal(BigInteger.ZERO);

            boolean descuentaTodo = false;
            BigDecimal descuento = t.getDescuento();

            descuento = descuento.subtract(t.getMontoMoratorio());
            System.out.println("D1: " + descuento);
            if (descuento.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {

                descuento = descuento.subtract(t.getMontoPunitorio());
                System.out.println("D2: " + descuento);
                if (descuento.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                    descuento = descuento.subtract(t.getCuotaInteres());
                    System.out.println("D3: " + descuento);
                    if (descuento.compareTo(new BigDecimal(BigInteger.ZERO)) <= 0) {
                        //cubro todo
                        System.out.println("D4" + descuento);
                        descuentaTodo = true;
                    }
                }
            }

            if (t.getMontoAPagar().compareTo(t.getMontoMora().subtract(t.getDescuento())) < 0) {
                //afectar punitorio
                aplicaAPunitorio = t.getMontoAPagar().multiply(new BigDecimal(0.2));
                //afecta moratorio
                aplicaAMoratorio = t.getMontoAPagar().multiply(new BigDecimal(0.8));
            } else {
                //afectar punitorio
                aplicaAPunitorio = t.getMontoPunitorio();
                //afecta moratorio
                aplicaAMoratorio = t.getMontoMoratorio();
                //aplicar a cuota la diferencia
                aplicaACuota = t.getMontoAPagar().subtract(t.getMontoMora().subtract(t.getDescuento()));
            }

            FacturaVentaDetalle d = new FacturaVentaDetalle();
            d.setFacturaVenta(facturaVenta);
            d.setNrolinea(nrolinea);
            d.setCantidad(new BigDecimal(BigInteger.ONE));
            d.setDetPrestamo(t.getDetPrestamo());
            d.setPrestamo(t.getPrestamo());
            d.setRefMonto(FacturaVentaDetalle.MONTO_CUOTA);

            d.setDescripcion("Pago de " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
            //Prioridad de calculo 0.8 del monto pagomora == moratorio ; 0.20
            //montoPago - (Moratorio + punitorio)
            //saldo

            d.setPrecioUnitario(aplicaACuota);

            DetPrestamo dp = t.getDetPrestamo();
            BigDecimal diffMontoPago = dp.getMontoPago().subtract((dp.calculaMontoPorDiasMoratorio().add(dp.calculaMontoPorDiasPunitorio()).add(t.getCuotaInteres())));

            System.out.println("Descuenta Todo: " + descuentaTodo);
            if (descuentaTodo) {
                d.setExenta(aplicaACuota);
            } else if (diffMontoPago.compareTo(new BigDecimal(BigInteger.ZERO)) >= 0) {
                d.setExenta(aplicaACuota);
            } else {
                BigDecimal exento = aplicaACuota.subtract(t.getCuotaInteres());
                if (exento.compareTo(new BigDecimal(BigInteger.ZERO)) >= 0) {

                    d.setExenta(exento);
                    d.setGravada10(t.getCuotaInteres());
                }
            }

            facturaVenta.getDetalles().add(d);
            nrolinea++;

            if (t.getMontoMoratorio().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                FacturaVentaDetalle d2 = new FacturaVentaDetalle();
                d2.setFacturaVenta(facturaVenta);
                d2.setNrolinea(nrolinea);
                d2.setCantidad(new BigDecimal(BigInteger.ONE));

                d2.setDescripcion("Pago por mora de la " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
                d2.setPrecioUnitario(aplicaAMoratorio);
                d2.setGravada10(d2.getCantidad().multiply(d2.getPrecioUnitario()));
                facturaVenta.getDetalles().add(d2);
                d2.setDetPrestamo(t.getDetPrestamo());
                d2.setRefMonto(FacturaVentaDetalle.MONTO_MORATORIO);
                nrolinea++;
            }

            if (t.getMontoPunitorio().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                FacturaVentaDetalle d3 = new FacturaVentaDetalle();
                d3.setFacturaVenta(facturaVenta);
                d3.setNrolinea(nrolinea);
                d3.setCantidad(new BigDecimal(BigInteger.ONE));

                d3.setDescripcion("Pago punitorio de la " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
                d3.setPrecioUnitario(aplicaAPunitorio);
                d3.setGravada10(d3.getCantidad().multiply(d3.getPrecioUnitario()));
                facturaVenta.getDetalles().add(d3);
                d3.setDetPrestamo(t.getDetPrestamo());
                d3.setRefMonto(FacturaVentaDetalle.MONTO_PUNITORIO);
                nrolinea++;
            }

            if (t.getDescuento().compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                FacturaVentaDetalle d4 = new FacturaVentaDetalle();
                d4.setFacturaVenta(facturaVenta);
                d4.setNrolinea(nrolinea);
                d4.setCantidad(new BigDecimal(BigInteger.ONE));

                d4.setDescripcion("Exoneración de Mora " + t.getDescDetPrestamo() + ", Prestamo #" + t.getPrestamo().getId());
                d4.setPrecioUnitario(t.getDescuento().multiply(new BigDecimal(-1)));
                d4.setGravada10(d4.getCantidad().multiply(d4.getPrecioUnitario()));
                facturaVenta.getDetalles().add(d4);
                d4.setDetPrestamo(t.getDetPrestamo());
                d4.setRefMonto(FacturaVentaDetalle.MONTO_DESCUENTO);
                nrolinea++;
            }

            BigDecimal totalexento = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalgravada05 = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalgravada10 = new BigDecimal(BigInteger.ZERO);
            for (FacturaVentaDetalle fd : facturaVenta.getDetalles()) {
                if (fd.getExenta() != null) {
                    totalexento = totalexento.add(fd.getExenta());
                }
                if (fd.getGravada05() != null) {
                    totalgravada05 = totalgravada05.add(fd.getGravada05());
                }

                if (fd.getGravada10() != null) {
                    totalgravada10 = totalgravada10.add(fd.getGravada10());
                }

            }

            BigDecimal total = totalgravada10.add(totalgravada05).add(totalexento);
            BigDecimal iva05 = new BigDecimal(BigInteger.ZERO);
            BigDecimal iva10 = new BigDecimal(BigInteger.ZERO);
            if (totalgravada10.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                iva10 = totalgravada10.divide(new BigDecimal(11), 0, RoundingMode.HALF_EVEN);
            }
            if (totalgravada05.compareTo(new BigDecimal(BigInteger.ZERO)) > 0) {
                iva05 = totalgravada05.divide(new BigDecimal(21), 0, RoundingMode.HALF_EVEN);
            }

            BigDecimal ivatotal = iva05.add(iva10);

            facturaVenta.setTotalExento(totalexento.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalGravada05(totalgravada05.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalGravada10(totalgravada10.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotal(total.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setIva05(iva05.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setIva10(iva10.setScale(0, RoundingMode.HALF_EVEN));
            facturaVenta.setTotalIva(ivatotal.setScale(0, RoundingMode.HALF_EVEN));

        }
        limpia();
        return "/cobraCuota/creaFactura";
    }

    public void limpia() {
        for (TreeCuota t : seleccionados) {
            t.setSeleccionado(false);
        }
        montoActual = new BigDecimal(BigInteger.ZERO);
        seleccionados.clear();
        //aplicacionPagoCuotas.clear();
        //cuotaSeleccionada = null;
    }

    public String paga() {
        try {
            //cobranzaDAO.create(facturaVenta, sesionTPVBean.getActual());
            cobranzaDAO.create(facturaVenta, sesionTPVBean.getActual(), aplicacionPagoCuotas);
            limpia();
            cargaPrestamos();
            sesionTPVBean.actualizaTotalTransacciones();

        } catch (PagoExcedidoException | NumeroInvalidoException | MontoCancelacionIncorrectoException ex) {
            Logger.getLogger(CobraCuotaBean.class
                    .getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ex.getMessage());

            return null;
        }
        return "endFlowCuota";
    }

    public BigDecimal getTotalAPagar() {
        totalAPagar = new BigDecimal(BigInteger.ZERO);
        for (TreeCuota t : seleccionados) {
            totalAPagar = totalAPagar.add(t.getMontoAPagar());
        }
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public List<TreeCuota> getSeleccionados() {

        return seleccionados;
    }

    public void setSeleccionados(List<TreeCuota> seleccionados) {
        this.seleccionados = seleccionados;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void buscaPrestamos() {

        cargaPrestamos();

    }

    private void cargaPrestamos() {
        root = new DefaultTreeNode("prestamos", null);
        disponibles = new ArrayList<TreeCuota>();

        Comparator<DetPrestamo> comp = new Comparator<DetPrestamo>() {

            @Override
            public int compare(DetPrestamo o1, DetPrestamo o2) {
                return o1.getNroCuota() > o2.getNroCuota() ? 1 : -1;
            }
        };

        for (Prestamo p : prestamoDAO.findAllClienteEstado(cliente, EstadoPrestamo.VIGENTE)) {
            TreeNode nodoPrestamo = new DefaultTreeNode(new TreeCuota(p), root);
            //nodoPrestamo.setExpanded(true);

            Collections.sort(p.getDetalles(), comp);

            int i = 0;
            for (DetPrestamo d : p.getDetalles()) {
                System.out.println("detalle: " + d.getNroCuota());
                CargoPorMora c = cagoDAO.findCargo(d.getDiasMora(), d.getPrestamo().getPeriodoPago());
                System.out.println("Cargo: " + c);
                if (c != null) {
                    d.setPendienteCargo(c.getMonto());
                }

                TreeCuota cuota = new TreeCuota(d);
                cuota.setPadre(nodoPrestamo);
                TreeNode nodoCuota = new DefaultTreeNode(cuota, nodoPrestamo);
                calculaSiSePuedePagar(cuota, i, nodoPrestamo);
                disponibles.add(cuota);

                i++;
            }
        }
    }

    public void calculaSiSePuedePagar(TreeCuota t, int index, TreeNode root) {
        if (index > 0) {
            TreeNode nodoAnterior = root.getChildren().get(index - 1);//disponibles.get(index -1);
            TreeCuota anterior = (TreeCuota) nodoAnterior.getData();
            if (anterior.getSaldoCuota().compareTo(new BigDecimal(BigInteger.ZERO)) == 0) {
                t.setDisabledPagar(false);
            } else {
                t.setDisabledPagar(true);
            }
        }

    }

    public void agregaAPagar() {
        System.out.println("En selcciona");

        if (seleccionados == null) {
            seleccionados = new ArrayList<>();
        }

        cuotaSeleccionada.setMontoAPagar(montoActual);

        seleccionados.remove(cuotaSeleccionada);
        seleccionados.add(cuotaSeleccionada);

        if (cuotaSeleccionada.getMontoAPagar().compareTo(cuotaSeleccionada.getSaldoCuota()) == 0) {
            if (cuotaSeleccionada.getPrestamo().getPlazo() > cuotaSeleccionada.getNroCuota()) {
                TreeCuota siguiente = (TreeCuota) cuotaSeleccionada.getPadre().getChildren().get(cuotaSeleccionada.getNroCuota()).getData();
                siguiente.setDisabledPagar(false);
            }

        }

        montoActual = new BigDecimal(BigInteger.ZERO);

    }

    @FacesValidator(value = "pagoValidator")
    public static class PagoValidator implements Validator {

        @Override
        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            if (value instanceof BigDecimal) {
                System.out.println("Valueeeeeeeee: " + value);
                BigDecimal montoPago = ((BigDecimal) value).setScale(0, RoundingMode.HALF_EVEN);

                CobraCuotaBean controller = (CobraCuotaBean) context.getApplication().getELResolver().
                        getValue(context.getELContext(), null, "cobraCuotaBean");

//                if (montoPago.compareTo(controller.getCuotaSeleccionada().getCuotaInteres().add(controller.getCuotaSeleccionada().getMontoMora()).subtract(controller.getCuotaSeleccionada().getDescuento())) < 0) {
//                    FacesMessage msg = new FacesMessage("Debe pagar por lo menos el minimo");
//                    throw new ValidatorException(msg);
//                }
                if (montoPago.compareTo(controller.getCuotaSeleccionada().getSaldoCuota()) > 0) {
                    FacesMessage msg = new FacesMessage("El pago excede el monto de la cuota");
                    throw new ValidatorException(msg);
                }
            }
        }

    }
}

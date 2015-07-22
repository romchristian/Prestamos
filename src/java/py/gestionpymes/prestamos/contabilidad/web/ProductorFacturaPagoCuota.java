/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

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
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.contabilidad.servicio.CobranzaDAO;
import py.gestionpymes.prestamos.prestamos.modelo.AplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.TipoDescuento;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoAplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;

/**
 *
 * @author Acer
 */
@Named
@RequestScoped
public class ProductorFacturaPagoCuota implements Serializable {

    @EJB
    private CobranzaDAO cobranzaDAO;

    private FacturaVenta facturaVenta;
    private Sucursal sucursal;
    private Cliente cliente;
    private Secuencia secuencia;
    private Moneda moneda;
    private List<TreeCuota> cuotas;

    private BigDecimal saldoMonto;
    private List<AplicacionPagoCuota> aplicacionPagoCuotas;

    public ProductorFacturaPagoCuota() {

    }

    public FacturaVenta genera(Sucursal sucursal, Cliente cliente, Secuencia secuencia, Moneda moneda, List<TreeCuota> cuotas) {
        this.sucursal = sucursal;
        this.cliente = cliente;
        this.secuencia = secuencia;
        this.moneda = moneda;
        this.cuotas = cuotas;
        facturaVenta = new FacturaVenta();
        facturaVenta.setCodEstablecimiento(secuencia.getEstablecimiento());
        facturaVenta.setPuntoExpedicion(secuencia.getPuntoExpedicion());
        facturaVenta.setTimbrado(secuencia.getTimbrado());
        Long numeroactual = secuencia.obtSiguienteNumero();
        facturaVenta.setNumero(secuencia.getNumeroFormateado(numeroactual));

        facturaVenta.setCliente(cliente);
        facturaVenta.setEmpresa(sucursal.getEmpresa());
        facturaVenta.setSucursal(sucursal);

        facturaVenta.setFechaCreacion(new Date());
        facturaVenta.setFechaEmision(new Date());
        facturaVenta.setDireccion(cliente.devuelveDireccionParticular());
        facturaVenta.setRazonSocial(cliente.devuelveNombreCompleto());
        facturaVenta.setRuc(cliente.getNroDocumento());
        facturaVenta.setMoneda(moneda);

        facturaVenta.setDetalles(generaDetalles());
        generaTotales();

        return facturaVenta;
    }

    public List<FacturaVentaDetalle> generaDetalles() {
        List<FacturaVentaDetalle> listaAux = new ArrayList<>();
        for (TreeCuota t : cuotas) {
            BigDecimal descuentoTotal = cobranzaDAO.getDescuentoAcumuladoTotal(t.getDetPrestamo());
            saldoMonto = t.getMontoAPagar().add(descuentoTotal);
            listaAux.addAll(generaDetallesAux(t));
        }

        return generaResumen(listaAux);
    }

    public List<AplicacionPagoCuota> getAplicacionesPagoCuotas() {
        return aplicacionPagoCuotas;
    }

    private boolean verificaDetalle(FacturaVentaDetalle d1) {
        boolean R = false;
        if (d1 != null && (d1.getGravada10().compareTo(BigDecimal.ZERO) > 0
                || d1.getGravada05().compareTo(BigDecimal.ZERO) > 0
                || d1.getExenta().compareTo(BigDecimal.ZERO) > 0)) {
            System.out.println("VERIFICA DETALLE TRUE, MONTO: " + d1.getGravada10());
            R = true;
        }
        return R;
    }

    private boolean verificaDetalleDescuento(FacturaVentaDetalle d1) {
        boolean R = false;
        if (d1 != null && (d1.getGravada10().compareTo(BigDecimal.ZERO) < 0
                || d1.getGravada05().compareTo(BigDecimal.ZERO) < 0
                || d1.getExenta().compareTo(BigDecimal.ZERO) < 0)) {
            R = true;
        }
        return R;
    }

    private List<FacturaVentaDetalle> generaDetallesAux(TreeCuota t) {

        List<FacturaVentaDetalle> R = new ArrayList<>();
        FacturaVentaDetalle d5 = generaDetalleCargo(t);
        FacturaVentaDetalle d3 = generaDetalleMoraMoratorio(t);
        FacturaVentaDetalle d4 = generaDetalleMoraPunitorio(t);
        FacturaVentaDetalle d2 = generaDetalleCuotaInteres(t);
        FacturaVentaDetalle d1 = generaDetalleCuotaCapital(t);

        FacturaVentaDetalle d6 = generaDetalleDescuentoMora(t);
        FacturaVentaDetalle d7 = generaDetalleDescuentoCargo(t);
        FacturaVentaDetalle d8 = generaDetalleDescuentoInteres(t);

        if (verificaDetalle(d5)) {
            R.add(d5);
        }
        if (verificaDetalle(d4)) {
            R.add(d4);
        }
        if (verificaDetalle(d3)) {
            R.add(d3);
        }
        if (verificaDetalle(d2)) {
            System.out.println("Agregó INTERES: " + d2.getGravada10());
            R.add(d2);
        }
        if (verificaDetalle(d1)) {
            R.add(d1);
        }
        
        //descuentos
        if (verificaDetalleDescuento(d6)) {
            R.add(d6);
        }
        if (verificaDetalleDescuento(d7)) {
            R.add(d7);
        }
        if (verificaDetalleDescuento(d8)) {
            R.add(d8);
        }

        return R;
    }

    private FacturaVentaDetalle generaDetalleCuotaInteres(TreeCuota t) {

        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_CUOTA);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Pago Cuota #" + t.getDetPrestamo().getNroCuota() + ": Interes ");

        BigDecimal montoAcumulado = cobranzaDAO.getAplicacionesMontoAcumulado(t.getDetPrestamo(), TipoAplicacionPagoCuota.CUOTA_INTERES);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("Saldo Monto: " + saldoMonto);
        System.out.println("MONTO ACUMULADO CUOTA INTERES: " + montoAcumulado);
        System.out.println("CUOTA INTERES: " + t.getCuotaInteres());
        
        BigDecimal montoGravada10;
        BigDecimal saldoCuotaInteres = t.getCuotaInteres()!=null?t.getCuotaInteres().subtract(montoAcumulado):BigDecimal.ZERO;
        if (saldoMonto.compareTo(saldoCuotaInteres) >= 0) {
            montoGravada10 = saldoCuotaInteres;
        } else {
            montoGravada10 = saldoMonto;
        }

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.CUOTA_INTERES, montoGravada10);

        saldoMonto = saldoMonto.subtract(montoGravada10);
        System.out.println("Saldo Monto Restado: " + montoAcumulado);
        System.out.println("MOnto GRAVADA 10 CUOTA INTERES: " + montoGravada10);
        return R;
    }

    private FacturaVentaDetalle generaDetalleCuotaCapital(TreeCuota t) {
        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_CUOTA);
        R.setExenta(BigDecimal.ZERO);

        BigDecimal montoAcumulado = cobranzaDAO.getAplicacionesMontoAcumulado(t.getDetPrestamo(), TipoAplicacionPagoCuota.CUOTA_CAPITAL);
        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_CAPITAL);
        BigDecimal saldoCuotaCapital = t.getCuotaCapital() != null ?t.getCuotaCapital().subtract(montoAcumulado):BigDecimal.ZERO;
        BigDecimal montoExento;
        if (saldoMonto.compareTo(saldoCuotaCapital) >= 0) {
            montoExento = saldoCuotaCapital;
        } else {
            montoExento = saldoMonto;
        }

        R.setDescripcion("Pago Cuota #" + t.getDetPrestamo().getNroCuota() + ": Capital ");
        R.setExenta(montoExento);
        R.setGravada05(BigDecimal.ZERO);
        R.setGravada10(BigDecimal.ZERO);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.CUOTA_CAPITAL, montoExento);

        saldoMonto = saldoMonto.subtract(montoExento);

        return R;
    }

    private FacturaVentaDetalle generaDetalleMoraMoratorio(TreeCuota t) {
        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_MORATORIO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Pago Moratorio Cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal montoAcumulado = cobranzaDAO.getAplicacionesMontoAcumulado(t.getDetPrestamo(), TipoAplicacionPagoCuota.MORATORIO);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO ACUMULADO: " + montoAcumulado);
        BigDecimal montoGravada10;
        BigDecimal saldoMoratorio = t.getMontoMoratorio()!= null?t.getMontoMoratorio().subtract(montoAcumulado):BigDecimal.ZERO;
        if (saldoMonto.compareTo(saldoMoratorio) >= 0) {
            montoGravada10 = saldoMoratorio;
        } else {
            montoGravada10 = saldoMonto;
        }

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.MORATORIO, montoGravada10);

        saldoMonto = saldoMonto.subtract(montoGravada10);
        return R;
    }

    private FacturaVentaDetalle generaDetalleMoraPunitorio(TreeCuota t) {
        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_PUNITORIO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Pago Punitorio Cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal montoAcumulado = cobranzaDAO.getAplicacionesMontoAcumulado(t.getDetPrestamo(), TipoAplicacionPagoCuota.PUNITORIO);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO ACUMULADO: " + montoAcumulado);
        BigDecimal montoGravada10;
        BigDecimal saldoPunitorio = t.getMontoPunitorio() != null?t.getMontoPunitorio().subtract(montoAcumulado):BigDecimal.ZERO;
        if (saldoMonto.compareTo(saldoPunitorio) >= 0) {
            montoGravada10 = saldoPunitorio;
        } else {
            montoGravada10 = saldoMonto;
        }

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.PUNITORIO, montoGravada10);

        saldoMonto = saldoMonto.subtract(montoGravada10);
        return R;
    }

    private FacturaVentaDetalle generaDetalleCargo(TreeCuota t) {
        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_CARGO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Pago de Cargo por la Cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal montoAcumulado = cobranzaDAO.getAplicacionesMontoAcumulado(t.getDetPrestamo(), TipoAplicacionPagoCuota.CARGO);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO ACUMULADO: " + montoAcumulado);
        BigDecimal montoGravada10;
        BigDecimal saldoCargo = t.getCargo() != null?t.getCargo().subtract(montoAcumulado):BigDecimal.ZERO;
        if (saldoMonto.compareTo(saldoCargo) >= 0) {
            montoGravada10 = saldoCargo;
        } else {
            montoGravada10 = saldoMonto;
        }

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.CARGO, montoGravada10);

        saldoMonto = saldoMonto.subtract(montoGravada10);
        return R;
    }

    private FacturaVentaDetalle generaDetalleDescuentoMora(TreeCuota t) {
        FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_DESCUENTO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Descuento de mora por la cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal descuento = cobranzaDAO.getDescuentoAcumulado(t.getDetPrestamo(), TipoDescuento.MORA);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO Descuento: " + descuento);
        BigDecimal montoGravada10;

        montoGravada10 = descuento.multiply(new BigDecimal(-1));

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.DESCUENTO_MORA, montoGravada10);

        return R;
    }

    private FacturaVentaDetalle generaDetalleDescuentoCargo(TreeCuota t) {
         FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_DESCUENTO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Descuento de cargo por la cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal descuento = cobranzaDAO.getDescuentoAcumulado(t.getDetPrestamo(), TipoDescuento.CARGOS);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO Descuento: " + descuento);
        BigDecimal montoGravada10;

        montoGravada10 = descuento.multiply(new BigDecimal(-1));

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.DESCUENTO_CARGO, montoGravada10);

        return R;
    }

    private FacturaVentaDetalle generaDetalleDescuentoInteres(TreeCuota t) {
         FacturaVentaDetalle R = new FacturaVentaDetalle();
        R.setFacturaVenta(facturaVenta);
        R.setCantidad(new BigDecimal(BigInteger.ONE));
        R.setDetPrestamo(t.getDetPrestamo());
        R.setPrestamo(t.getPrestamo());
        R.setRefMonto(FacturaVentaDetalle.MONTO_DESCUENTO);
        R.setExenta(BigDecimal.ZERO);
        R.setGravada05(BigDecimal.ZERO);
        R.setDescripcion("Descuento de intereses por la cuota #" + t.getDetPrestamo().getNroCuota());

        BigDecimal descuento = cobranzaDAO.getDescuentoAcumulado(t.getDetPrestamo(), TipoDescuento.INTERES);

        //getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        System.out.println("MONTO Descuento: " + descuento);
        BigDecimal montoGravada10;

        montoGravada10 = descuento.multiply(new BigDecimal(-1));

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.DESCUENTO_INTERES, montoGravada10);

        return R;
    }

    private List<FacturaVentaDetalle> generaResumen(List<FacturaVentaDetalle> listaAux) {
        return listaAux;
    }

    private void generaAplicacionPago(DetPrestamo detPrestamo, FacturaVentaDetalle facturaDetalle, TipoAplicacionPagoCuota tipo, BigDecimal monto) {
        AplicacionPagoCuota a = new AplicacionPagoCuota();
        a.setDetPrestamo(detPrestamo);
        a.setFacturaVentaDetalle(facturaDetalle);
        a.setFecha(new Date());
        a.setTipo(tipo);
        a.setMonto(monto);
        if (aplicacionPagoCuotas == null) {
            aplicacionPagoCuotas = new ArrayList<>();
        }
        if(a.getMonto().compareTo(BigDecimal.ZERO) > 0 && 
                (tipo != TipoAplicacionPagoCuota.DESCUENTO_CARGO ||
                tipo !=  TipoAplicacionPagoCuota.DESCUENTO_MORA ||
                tipo != TipoAplicacionPagoCuota.DESCUENTO_INTERES)){
            aplicacionPagoCuotas.add(a);
        }else if(a.getMonto().compareTo(BigDecimal.ZERO) < 0){
            aplicacionPagoCuotas.add(a);
        }
        
    }

    private BigDecimal getAcumulado(List<AplicacionPagoCuota> lista, TipoAplicacionPagoCuota tipo) {
        BigDecimal R = BigDecimal.ZERO;
        for (AplicacionPagoCuota a : lista) {
            if (a.getTipo() == tipo) {
                R = R.add(a.getMonto());
            }
        }
        
        return R;
    }

    private void generaTotales() {
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

}

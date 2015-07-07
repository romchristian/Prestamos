/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.gestionpymes.prestamos.adm.modelo.Moneda;
import py.gestionpymes.prestamos.adm.modelo.Sucursal;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.prestamos.modelo.AplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.modelo.Cliente;
import py.gestionpymes.prestamos.prestamos.modelo.DetPrestamo;
import py.gestionpymes.prestamos.prestamos.modelo.enums.TipoAplicacionPagoCuota;
import py.gestionpymes.prestamos.prestamos.web.TreeCuota;
import py.gestionpymes.prestamos.tesoreria.modelo.Secuencia;

/**
 *
 * @author Acer
 */
public class ProductorFacturaPagoCuota implements ProductorFactura {

    private FacturaVenta facturaVenta;
    private Sucursal sucursal;
    private Cliente cliente;
    private Secuencia secuencia;
    private Moneda moneda;
    private List<TreeCuota> cuotas;

    private BigDecimal saldoMonto;
    private List<AplicacionPagoCuota> aplicacionPagoCuotas;

    public ProductorFacturaPagoCuota(Sucursal sucursal, Cliente cliente, Secuencia secuencia, Moneda moneda, List<TreeCuota> cuotas) {
        this.sucursal = sucursal;
        this.cliente = cliente;
        this.secuencia = secuencia;
        this.moneda = moneda;
        this.cuotas = cuotas;
    }
    
   
    @Override
    public FacturaVenta generaCabecera() {
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
        return facturaVenta;
    }

    @Override
    public List<FacturaVentaDetalle> generaDetalles() {
        List<FacturaVentaDetalle> listaAux = new ArrayList<>();
        for (TreeCuota t : cuotas) {

            saldoMonto = t.getMontoAPagar();
            listaAux.addAll(generaDetallesAux(t));
        }
        return generaResumen(listaAux);
    }

    public List<AplicacionPagoCuota> generaAplicacionPagoCuota() {
        return aplicacionPagoCuotas;
    }

    private List<FacturaVentaDetalle> generaDetallesAux(TreeCuota t) {

        List<FacturaVentaDetalle> R = new ArrayList<>();
        FacturaVentaDetalle d2 = generaDetalleCuotaInteres(t);
        FacturaVentaDetalle d1 = generaDetalleCuotaCapital(t);
        FacturaVentaDetalle d3 = generaDetalleMoraMoratorio(t);
        FacturaVentaDetalle d4 = generaDetalleMoraPunitorio(t);
        FacturaVentaDetalle d5 = generaDetalleCargo(t);
        FacturaVentaDetalle d6 = generaDetalleDescuentoMora(t);
        FacturaVentaDetalle d7 = generaDetalleDescuentoCargo(t);
        FacturaVentaDetalle d8 = generaDetalleDescuentoInteres(t);

        if (d1 != null) {
            R.add(d1);
        }

        if (d2 != null) {
            R.add(d2);
        }

        if (d3 != null) {
            R.add(d3);
        }
        if (d4 != null) {
            R.add(d4);
        }
        if (d5 != null) {
            R.add(d5);
        }
        if (d6 != null) {
            R.add(d6);
        }
        if (d7 != null) {
            R.add(d7);
        }
        if (d8 != null) {
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
        R.setDescripcion("Pago Cuota #"+t.getDetPrestamo().getNroCuota()+": Interes ");

        BigDecimal montoAcumulado = getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_INTERES);
        BigDecimal montoGravada10;
        BigDecimal saldoCuotaInteres = t.getCuotaInteres().subtract(montoAcumulado);
        if (saldoMonto.compareTo(saldoCuotaInteres) >= 0) {
            montoGravada10 = saldoCuotaInteres;
        } else {
            montoGravada10 = saldoMonto;
        }

        R.setGravada10(montoGravada10);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.CUOTA_INTERES, montoGravada10);

        saldoMonto = saldoMonto.subtract(montoGravada10);
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

        BigDecimal montoAcumulado = getAcumulado(t.getDetPrestamo().getAplicacionesPago(), TipoAplicacionPagoCuota.CUOTA_CAPITAL);
        BigDecimal saldoCuotaCapital = t.getCuotaCapital().subtract(montoAcumulado);
        BigDecimal montoExento;
        if (saldoMonto.compareTo(saldoCuotaCapital) >= 0) {
            montoExento = saldoCuotaCapital;
        } else {
            montoExento = saldoMonto;
        }

        R.setDescripcion("Pago Cuota #"+t.getDetPrestamo().getNroCuota()+": Capital ");
        R.setExenta(montoExento);
        R.setGravada05(BigDecimal.ZERO);
        R.setGravada10(BigDecimal.ZERO);

        generaAplicacionPago(t.getDetPrestamo(), R, TipoAplicacionPagoCuota.CUOTA_CAPITAL, montoExento);

        saldoMonto = saldoMonto.subtract(montoExento);

        return R;
    }

    private FacturaVentaDetalle generaDetalleMoraMoratorio(TreeCuota t) {
        return null;
    }

    private FacturaVentaDetalle generaDetalleMoraPunitorio(TreeCuota t) {
        return null;
    }

    private FacturaVentaDetalle generaDetalleCargo(TreeCuota t) {
        return null;
    }

    private FacturaVentaDetalle generaDetalleDescuentoMora(TreeCuota t) {
        return null;
    }

    private FacturaVentaDetalle generaDetalleDescuentoCargo(TreeCuota t) {
        return null;
    }

    private FacturaVentaDetalle generaDetalleDescuentoInteres(TreeCuota t) {
        return null;
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
        aplicacionPagoCuotas.add(a);
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

    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.web;

import java.util.List;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVenta;
import py.gestionpymes.prestamos.contabilidad.modelo.FacturaVentaDetalle;
import py.gestionpymes.prestamos.seguridad.persistencia.Auditable;

/**
 *
 * @author Acer
 */
public interface ProductorFactura {

    public FacturaVenta generaCabecera();

    public List<FacturaVentaDetalle> generaDetalles();

}

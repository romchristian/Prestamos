/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.web;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.gestionpymes.prestamos.prestamos.dao.CuentaClienteDAO;
import py.gestionpymes.prestamos.prestamos.persistencia.Cliente;
import py.gestionpymes.prestamos.prestamos.persistencia.CuentaCliente;
import py.gestionpymes.prestamos.prestamos.persistencia.DetCuentaCliente;

/**
 *
 * @author Acer
 */
@Named
@ViewScoped
public class CuentaClienteController implements Serializable {

    @EJB
    private CuentaClienteDAO ejb;
    private CuentaCliente cuentaClienteSeleccionada;
    private Cliente clienteSeleccionado;
    private List<DetCuentaCliente> detalles;

    public void busca() {
        System.out.println("HOLA 1");
        if (clienteSeleccionado != null) {
            System.out.println("HOLA 2");
            System.out.println("Cliente: " + clienteSeleccionado.devuelveNombreCompleto());
            cuentaClienteSeleccionada = ejb.findPorCliente(clienteSeleccionado);
            System.out.println("Cuenta:" + cuentaClienteSeleccionada);
            System.out.println("Detalles: " + cuentaClienteSeleccionada.getDetalles());
            if (cuentaClienteSeleccionada != null) {
                detalles = ejb.findDetalles(cuentaClienteSeleccionada);
                cuentaClienteSeleccionada.setDetalles(detalles);
            }
            
        }
    }

    public List<DetCuentaCliente> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetCuentaCliente> detalles) {
        this.detalles = detalles;
    }

    public CuentaCliente getCuentaClienteSeleccionada() {
        return cuentaClienteSeleccionada;
    }

    public void setCuentaClienteSeleccionada(CuentaCliente cuentaClienteSeleccionada) {
        this.cuentaClienteSeleccionada = cuentaClienteSeleccionada;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Cliente clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

}

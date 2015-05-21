/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.contabilidad.persistencia;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import py.gestionpymes.prestamos.tesoreria.persisitencia.Banco;

/**
 *
 * @author christian
 */
@Entity
public class ChequeRecibido extends Pago{
    @ManyToOne
    private Banco banco;
    private String numero;
    private String librador;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimiento;
    @Enumerated(EnumType.STRING)
    private EstadoCheque estadoCheque;

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLibrador() {
        return librador;
    }

    public void setLibrador(String librador) {
        this.librador = librador;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public EstadoCheque getEstadoCheque() {
        return estadoCheque;
    }

    public void setEstadoCheque(EstadoCheque estadoCheque) {
        this.estadoCheque = estadoCheque;
    }
    
    
}
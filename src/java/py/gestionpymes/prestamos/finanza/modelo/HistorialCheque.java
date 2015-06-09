/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import py.gestionpymes.prestamos.adm.modelo.Historial;
import py.gestionpymes.prestamos.adm.modelo.Usuario;

/**
 *
 * @author Acer
 */
@Entity
public class HistorialCheque extends Historial implements Serializable{

    @ManyToOne
    private ChequeEmitido chequeEmitido;

    public HistorialCheque() {
    }
    
    public HistorialCheque(ChequeEmitido chequeEmitido, Usuario usuario, String operacion, String descripcion) {
        super(usuario, operacion, descripcion);
        this.chequeEmitido = chequeEmitido;
    }

    public ChequeEmitido getChequeEmitido() {
        return chequeEmitido;
    }

    public void setChequeEmitido(ChequeEmitido chequeEmitido) {
        this.chequeEmitido = chequeEmitido;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.finanza.dao;

import javax.ejb.ApplicationException;

/**
 *
 * @author Acer
 */

@ApplicationException(rollback = true)
public class TransaccionBancariaNoCreadaException extends Exception{

    public TransaccionBancariaNoCreadaException(String message) {
        super("[No se creo la transaccion bancaria] : " + message);
    }
    
}

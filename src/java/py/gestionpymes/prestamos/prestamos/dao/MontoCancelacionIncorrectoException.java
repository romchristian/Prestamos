        /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.dao;

import javax.ejb.ApplicationException;

/**
 *
 * @author Acer
 */
@ApplicationException(rollback = true)
public class MontoCancelacionIncorrectoException extends Exception{

    public MontoCancelacionIncorrectoException(String message) {
        super(message);
    }
    
}

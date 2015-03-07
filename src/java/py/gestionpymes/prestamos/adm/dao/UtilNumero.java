/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Elias
 */
public class UtilNumero {
    
    public static BigDecimal getIva(int iva, BigDecimal montoBruto, int precision){
        double auxIva = (iva / 100d)+1;
        BigDecimal montoNeto = montoBruto.divide(new BigDecimal(auxIva),precision, RoundingMode.HALF_EVEN);
        BigDecimal R = montoBruto.subtract(montoNeto).setScale(precision, RoundingMode.HALF_EVEN);
        return R;
    }
}

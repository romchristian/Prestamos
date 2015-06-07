/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.prestamos.modelo.validadores;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author cromero
 */
public class SoloLetrasValidator implements ConstraintValidator<SoloLetras,String> {

    @Override
    public void initialize(SoloLetras constraintAnnotation) {
        
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("[a-zA-Z|ñÑ|áÁ|éÉ|íÍ|óÓ|úÚ|ç|äÄ|ëË|ïÏ|öÖ|üÜ|\\s|\\.|,]*");
    }
    
}

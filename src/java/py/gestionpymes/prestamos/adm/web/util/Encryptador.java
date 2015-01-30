/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gestionpymes.prestamos.adm.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author cromero
 */
public class Encryptador {

    public static String encrypta(String clave) {
        String R = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(clave.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String output = bigInt.toString(16);
            R = output;


        } catch (Exception e) {
        }

        return R;
    }

    public static boolean verica(final String plainPassword, final String encryptedPassword) {
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        return encryptor.checkPassword(plainPassword, encryptedPassword);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class PasswordEncrypt {

    private static final SecureRandom RAND = new SecureRandom();

    public Optional<String> generateSalt() {
        // generate random salt
        byte[] salt = new byte[512];
        RAND.nextBytes(salt);
        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }

    public Optional<String> hashPassword(String password, String salt) {
        // convert password to char array and salt to byte array
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        // mix the password with the salt with iteration of 65536 (max byte) and length of 512
        PBEKeySpec spec = new PBEKeySpec(chars, bytes, 65536, 512);

        // clean out password array (fill it back to null)
        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            // generate key with algorithm sha512
            SecretKeyFactory fac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            // encode byte key to ascii string for easier to store into database
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // if error return empty
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();
        } finally {
            // clean PBE of all password information
            spec.clearPassword();
        }
    }

    public boolean verifyPassword(String password, String key, String salt) {
        // generate key with password and salt for checking
        Optional<String> optEncrypted = hashPassword(password, salt);
        // if optional object null (error) auto return false
        if (!optEncrypted.isPresent()) {
            return false;
        }
        // return whether the result of comparison is true of false
        return optEncrypted.get().equals(key);
    }
}

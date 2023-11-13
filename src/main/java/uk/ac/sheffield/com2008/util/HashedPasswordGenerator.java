package uk.ac.sheffield.com2008.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashedPasswordGenerator {
    public static String hashPassword(char[] password, String salt) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes(salt.getBytes(), new String(password).getBytes());

            // Update the digest with the salted password bytes
            md.update(saltedPasswordBytes);

            // Get the hashed password bytes
            byte[] hashedPasswordBytes = md.digest();

            // Convert the hashed password bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}

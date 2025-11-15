package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CriptografiaUtil {
    
    
    public static String criptografar(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }
    
   
    public static boolean verificarSenha(String senha, String hashArmazenado) {
        String hashSenha = criptografar(senha);
        return hashSenha.equals(hashArmazenado);
    }
    
   
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
  
    public static String hashSimples(String texto) {
        int hash = 7;
        for (int i = 0; i < texto.length(); i++) {
            hash = hash * 31 + texto.charAt(i);
        }
        return String.valueOf(Math.abs(hash));
    }
}

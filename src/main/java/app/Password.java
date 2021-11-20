package app;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Password {
    private static final String algorithm = "Blowfish";

    private static final String secret = "myKey";
    private static SecretKeySpec secretKeySpec;
    private static Cipher cipher;

    public static void init() throws Exception {
        byte[] keyData = secret.getBytes();
        secretKeySpec = new SecretKeySpec(keyData, algorithm);
        cipher = Cipher.getInstance(algorithm);
    }

    public static String encrypt(String password) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return new String(Base64.getEncoder()
                .encode(cipher.doFinal(password.getBytes())));
    }

    public static String decrypt(String password) throws Exception {
        byte[] keyData = secret.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
    }
}

package com.skocur.imagecipher.encrypters;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryption extends Encrypter {

    private String certificateFileName;

    public RSAEncryption(String fileName, String certificateFileName) throws IOException {
        super(fileName);
        this.certificateFileName = certificateFileName;
    }

    @Override
    public void encrypt(String text) {
        try {
            String publicKey = new String(Files.readAllBytes(Paths.get(certificateFileName)));
            publicKey = publicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            Encrypter encrypter = new LowLevelBitEncryption(super.fileName);
            encrypter.encrypt(new String(encrypted, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}

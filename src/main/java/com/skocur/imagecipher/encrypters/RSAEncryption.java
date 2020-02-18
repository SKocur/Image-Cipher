package com.skocur.imagecipher.encrypters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryption extends Encrypter {

    private String certificateFileName;
    public RSAPublicKey pubkey;
    protected RSAPrivateKey privkey;
    private RSAKeyType keyformat;

    public RSAEncryption(@NotNull String fileName, @Nullable String certificateFileName) throws IOException {
        super(fileName);
        this.certificateFileName = certificateFileName;
        keyformat = RSAKeyType.RSA;
    }

    public RSAEncryption(@NotNull String fileName) throws IOException {
        super(fileName);
        this.certificateFileName = null;
        keyformat = RSAKeyType.RSA;
    }

    @Override
    public void encrypt(@NotNull String text) {
        try {
            byte[] encrypted;
            if (keyformat.equals(RSAKeyType.RSA))
                encrypted = RSATypeEncryption(text);
            else
                throw new UnsupportedOperationException(); //leaving this here as a placeholder until all RSA public key formats are implemented here

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] RSATypeEncryption(@NotNull String text) throws Exception {
        byte[] encrypted;
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        pubkey = (RSAPublicKey) pair.getPublic();
        privkey = (RSAPrivateKey) pair.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubkey);

        encrypted = cipher.doFinal(text.getBytes());
        return encrypted;
    }

    /*
    private byte[] SSHTypeEncryption(String text)
        {
            byte[] encrypted;
            //leaving this here for future implementation
            return encrypted;
        }

    private byte[] SSH2TypeEncryption(String text)
        {
            byte[] encrypted;
            //leaving this here for future implementation
            return encrypted;

    private byte[] Base64TypeEncryption(String text)
        {
            byte[] encrypted;
            //leaving this here for future implementation
            return encrypted;
        }*/
    public RSAPrivateKey getPrivKey() {
        return this.privkey;
    } //needed for decryption
}

enum RSAKeyType {
    RSA, SSH, SSH2, Base64;
}

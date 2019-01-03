package fr.mbds.org.securechat.utils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encryption {

    String provider = "AndroidKeyStore";
    String transformation = "RSA/ECB/PKCS1Padding";
    KeyPairGenerator kpg;
    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;

    public Encryption(String alias) {
        generateKeyPair(alias);
    }

    public void generateKeyPair(String alias) {
        try {
            kpg = KeyPairGenerator.getInstance("RSA", provider);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
            kpg.initialize(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        keyPair = kpg.genKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    /*public PublicKey getPublicKey(String alias) {

        KeyStore.Entry entry = keyStore.getEntry(alias, null);
        PublicKey publicKey = null;

        try {
            KeyStore keyStore = KeyStore.getInstance(provider);
            keyStore.load(null);
            publicKey = keyStore.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public PrivateKey getPrivateKey(String alias) {

        KeyStore.Entry entry = null;

        try {
            KeyStore keyStore = KeyStore.getInstance(provider);
            keyStore.load(null);
            entry = keyStore.getEntry(alias, null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
    }*/

    public byte[] encrypt(String input) {

        byte[] encryptedBytes = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            encryptedBytes = cipher.doFinal(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return encryptedBytes;
    }

    public String decrypt(byte[] input) {

        byte[] decryptedBytes = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            decryptedBytes = cipher.doFinal(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return new String(decryptedBytes);
    }

}

package com.chat.server.application.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chat.server.domain.util.EncryptUtil;

@Component
public class AESEncryptUtil implements EncryptUtil {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

  private final String encryptionKey;

  public AESEncryptUtil(@Value("${encryption.key}") String encryptionKey) {
    this.encryptionKey = encryptionKey;
  }

  @Override
  public String generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
    keyGenerator.init(256);
    SecretKey key = keyGenerator.generateKey();
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  @Override
  public String encrypt(String valueToEncrypt) throws Exception {
    return this.encrypt(valueToEncrypt, encryptionKey);
  }

  @Override
  public String encrypt(String valueToEncrypt, String key) throws Exception {
    IvParameterSpec iv = generateInitializationVector();
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

    byte[] cipherText = cipher.doFinal(valueToEncrypt.getBytes());
    byte[] combined = new byte[iv.getIV().length + cipherText.length];

    System.arraycopy(iv.getIV(), 0, combined, 0, iv.getIV().length);
    System.arraycopy(cipherText, 0, combined, iv.getIV().length, cipherText.length);

    return Base64.getEncoder().encodeToString(combined);
  }

  @Override
  public String decrypt(String encryptedValue) throws Exception {
    return this.decrypt(encryptedValue, encryptionKey);
  }

  @Override
  public String decrypt(String encryptedValue, String key) throws Exception {
    byte[] combined = Base64.getDecoder().decode(encryptedValue);
    byte[] ivBytes = Arrays.copyOfRange(combined, 0, 16);
    byte[] cipherText = Arrays.copyOfRange(combined, 16, combined.length);

    IvParameterSpec iv = new IvParameterSpec(ivBytes);
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

    byte[] decryptedBytes = cipher.doFinal(cipherText);
    return new String(decryptedBytes);
  }

  private IvParameterSpec generateInitializationVector() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }
}

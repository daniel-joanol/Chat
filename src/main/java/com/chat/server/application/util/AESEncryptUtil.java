package com.chat.server.application.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.chat.server.domain.util.EncryptUtil;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

// AES algorithm with CBC variation
@Component
public class AESEncryptUtil implements EncryptUtil {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

  @Override
  public String generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
    keyGenerator.init(256);
    SecretKey key = keyGenerator.generateKey();
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  @Override
  public char[] decrypt(char[] encryptedValue, String key) throws Exception {
    byte[] combined = this.base64DecodeCharArray(encryptedValue);
    byte[] ivBytes = Arrays.copyOfRange(combined, 0, 16);
    byte[] cipherText = Arrays.copyOfRange(combined, 16, combined.length);

    IvParameterSpec iv = new IvParameterSpec(ivBytes);
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    byte[] encryptedPass = cipher.doFinal(cipherText);

    return this.convertBytesToChars(encryptedPass);
  }

  @Override
  public String encrypt(String valueToEncrypt, String key) throws Exception {
    IvParameterSpec iv = this.generateInitialiationVector();
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
    byte[] cipherText = cipher.doFinal(valueToEncrypt.getBytes());

    byte[] combined = new byte[iv.getIV().length + cipherText.length];
    System.arraycopy(iv.getIV(), 0, combined, 0, iv.getIV().length);
    System.arraycopy(cipherText, 0, combined, iv.getIV().length, cipherText.length);

    return Base64.getEncoder().encodeToString(combined);
  }

  private IvParameterSpec generateInitialiationVector() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }

  private byte[] base64DecodeCharArray(char[] input) {
    return Base64.getDecoder().decode(this.charArrayToByteArray(input));
  }

  private byte[] charArrayToByteArray(char[] input) {
    byte[] bytes = new byte[input.length];
    for (int i = 0; i < input.length; i++) {
      bytes[i] = (byte) input[i];
    }
    return bytes;
  }

  private char[] convertBytesToChars(byte[] bytes) {
    char[] chars = new char[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      chars[i] = (char) (bytes[i] & 0xFF); //Important, use & 0xFF to avoid negative values.
    }
    return chars;
  }
  
}

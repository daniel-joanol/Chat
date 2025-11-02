package com.chat.server.domain.util;

public interface EncryptUtil {
  
  String generateKey() throws Exception;

  String encrypt(String valueToEncrypt) throws Exception;

  String encrypt(String valueToEncrypt, String key) throws Exception;

  char[] decrypt(char[] encryptedValue) throws Exception;

  char[] decrypt(char[] encryptedValue, String key) throws Exception;

}

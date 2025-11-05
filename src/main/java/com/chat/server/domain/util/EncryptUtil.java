package com.chat.server.domain.util;

public interface EncryptUtil {
  
  String generateKey() throws Exception;

  String encrypt(String valueToEncrypt) throws Exception;

  String encrypt(String valueToEncrypt, String key) throws Exception;

  String decrypt(String encryptedValue) throws Exception;

  String decrypt(String encryptedValue, String key) throws Exception;

}

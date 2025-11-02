package com.chat.server.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AESEncryptUtilTests {
  
  private AESEncryptUtil sut = new AESEncryptUtil("KEY");

  @Test
  @DisplayName("Should generate a key, encrypt a value than decrypt it. " 
      + "After decription the value should be the same as before encryption")
  void testEncryption_validateIfValueIsTheSameAfterEncryption() throws Exception {
    String value = "VA576&/(&/%khk _c)";
    String key = sut.generateKey();
    String encryptedValue = sut.encrypt(value, key);
    char[] decryptedValue = sut.decrypt(encryptedValue.toCharArray(), key);
    assertEquals(value, new String(decryptedValue));
  }
}

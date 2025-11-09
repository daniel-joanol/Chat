package com.chat.server.infrastructure.controller.annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidPasswordTest {
  
  private ValidPasswordImpl sut = new ValidPasswordImpl();

  @Test
  void testIsValid_assertPasswordIsValid() {
    assertFalse(sut.isValid(null, null));
    assertFalse(sut.isValid("   ", null));
    assertFalse(sut.isValid("longenoughhh", null));
    assertFalse(sut.isValid("Longenoughhh", null));
    assertFalse(sut.isValid("L0ngenoughhh", null));
    assertFalse(sut.isValid("LONGENOUGHHH", null));
    assertFalse(sut.isValid("090803821083010329103", null));
    assertFalse(sut.isValid("$&&@@_-+?¿()&$¡", null));
    assertFalse(sut.isValid("WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAYtoL000000000000000ng_", null));
    assertTrue(sut.isValid("V@lidPassw0rd", null));
  }

}

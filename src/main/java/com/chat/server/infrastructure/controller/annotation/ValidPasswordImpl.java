package com.chat.server.infrastructure.controller.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPasswordImpl implements ConstraintValidator<ValidPassword, String> {

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null || password.isBlank()) return false;
    boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
    boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
    boolean hasDigit = password.chars().anyMatch(Character::isDigit);
    boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
    boolean validLength = password.length() >= 12 && password.length() <= 30;

    return hasUppercase && hasLowercase && hasDigit && hasSpecial && validLength;
  }
  
}

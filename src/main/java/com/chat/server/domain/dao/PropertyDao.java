package com.chat.server.domain.dao;

public interface PropertyDao {
  
  String getValueByName(String name);

  char[] getPasswordByName(String name);

}

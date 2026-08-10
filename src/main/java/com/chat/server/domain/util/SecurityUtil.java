package com.chat.server.domain.util;


import com.chat.server.infrastructure.exception.InternalException;public interface SecurityUtil {
  
  String getUsername() throws InternalException;
  
}

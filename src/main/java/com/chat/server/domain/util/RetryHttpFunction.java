package com.chat.server.domain.util;

import com.chat.server.infrastructure.exception.InternalUserForbiddenException;

@FunctionalInterface
public interface RetryHttpFunction<T> {
  
  T execute(String jwt) throws InternalUserForbiddenException;

}

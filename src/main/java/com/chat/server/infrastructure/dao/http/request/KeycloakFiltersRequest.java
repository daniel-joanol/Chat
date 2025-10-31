package com.chat.server.infrastructure.dao.http.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KeycloakFiltersRequest {
  
  private Integer first;
  private Integer max;
  private String search;

  public static KeycloakFiltersRequest singleUserFilter(String search) {
    return new KeycloakFiltersRequest()
        .setFirst(0)
        .setMax(1)
        .setSearch(search);
  }

}

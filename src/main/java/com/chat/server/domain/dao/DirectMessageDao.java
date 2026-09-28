package com.chat.server.domain.dao;

import com.chat.server.domain.model.DirectMessage;

public interface DirectMessageDao {

  DirectMessage save(DirectMessage message);

}
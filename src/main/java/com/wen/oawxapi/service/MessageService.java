package com.wen.oawxapi.service;

import com.wen.oawxapi.mongo.db.MessageEntity;
import com.wen.oawxapi.mongo.db.MessageRefEntity;

import java.util.HashMap;
import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-07-21 16:50
 * @description:
 */
public interface MessageService {

    String insertMessage(MessageEntity messageEntity);

    List<HashMap> searchMessageByPage(long userId, long page, long size);

    HashMap searchMessageById(String id);

    String insertMessageRef(MessageRefEntity messageRefEntity);

    Long searchUnreadCount(long userId);

    Long searchLastCount(long userId);

    Long updateUnreadMessage(String id);

    Long deleteMessageRefById(String id);

    Long deleteMessageRefByUserId(Integer userId);
}

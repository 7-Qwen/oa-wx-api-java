package com.wen.oawxapi.service.impl;

import com.wen.oawxapi.mapper.mongodb.MessageMapper;
import com.wen.oawxapi.mapper.mongodb.MessageRefMapper;
import com.wen.oawxapi.mongo.db.MessageEntity;
import com.wen.oawxapi.mongo.db.MessageRefEntity;
import com.wen.oawxapi.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-07-21 16:52
 * @description:
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageRefMapper messageRefMapper;
    @Resource
    private MessageMapper messageMapper;

    @Override
    public String insertMessage(MessageEntity messageEntity) {
        return messageMapper.insert(messageEntity);
    }

    @Override
    public List<HashMap> searchMessageByPage(long userId, long page, long size) {
        return messageMapper.searchMessageByPage(userId, page, size);
    }

    @Override
    public HashMap searchMessageById(String id) {
        return messageMapper.searchMessageById(id);
    }

    @Override
    public String insertMessageRef(MessageRefEntity messageRefEntity) {
        return messageRefMapper.insert(messageRefEntity);
    }

    @Override
    public Long searchUnreadCount(long userId) {
        return messageRefMapper.searchUnreadCount(userId);
    }

    @Override
    public Long searchLastCount(long userId) {
        return messageRefMapper.searchLastCount(userId);
    }

    @Override
    public Long updateUnreadMessage(String id) {
        return messageRefMapper.updateUnreadMessage(id);
    }

    @Override
    public Long deleteMessageRefById(String id) {
        return messageRefMapper.deleteMessageRefById(id);
    }

    @Override
    public Long deleteMessageRefByUserId(Integer userId) {
        return messageRefMapper.deleteMessageRefByUserId(userId);
    }
}

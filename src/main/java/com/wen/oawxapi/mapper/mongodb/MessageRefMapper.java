package com.wen.oawxapi.mapper.mongodb;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wen.oawxapi.mongo.db.MessageRefEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author: 7wen
 * @Date: 2023-07-20 16:25
 * @description: mongodbMessageRef dao类
 */
@Repository
public class MessageRefMapper {
    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 插入message_ref集合,返回id
     */
    public String insert(MessageRefEntity messageRefEntity) {
        MessageRefEntity refEntity = mongoTemplate.save(messageRefEntity);
        return refEntity.get_id();
    }


    /**
     * 通过用户id查询用户未读消息
     */
    public Long searchUnreadCount(long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("readFlag").is(false).and("receiverId").is(userId));
        return mongoTemplate.count(query, MessageRefEntity.class);
    }


    /**
     * 通过用户id 查询距离上一次查询新消息后的消息数量;
     * 包含更新操作 查询到一条就变更last状态并返回int值 小程序根据此int值推送"您有xx条新消息"
     */
    public Long searchLastCount(long userId) {
        //先查询
        Query query = new Query().addCriteria(Criteria.where("lastFlag").is(true).and("receiverId").is(userId));
        long count = mongoTemplate.count(query, MessageRefEntity.class);
        //后变更
        UpdateResult result = mongoTemplate.updateMulti(query, new Update().set("lastFlag", false), MessageRefEntity.class);
        return result.getModifiedCount();
    }


    /**
     * 用户读取消息后需要变更消息状态
     */
    public Long updateUnreadMessage(String id) {
        UpdateResult result = mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("_id").is(id)), new Update().set("lastFlag", true), MessageRefEntity.class);
        return result.getModifiedCount();
    }


    /**
     * 根据消息id删除消息(单一
     */
    public Long deleteMessageRefById(String id) {
        DeleteResult result = mongoTemplate.remove(new Query().addCriteria(Criteria.where("_id").is(id)), MessageRefEntity.class);
        return result.getDeletedCount();
    }


    /**
     * 根据用户id删除消息 (全部已读)
     */
    public Long deleteMessageRefByUserId(Integer userId) {
        DeleteResult result = mongoTemplate.remove(new Query().addCriteria(Criteria.where("receiverId").is(userId)), MessageRefEntity.class);
        return result.getDeletedCount();
    }
}

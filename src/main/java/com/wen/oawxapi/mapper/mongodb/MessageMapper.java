package com.wen.oawxapi.mapper.mongodb;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.wen.oawxapi.mongo.db.MessageEntity;
import com.wen.oawxapi.mongo.db.MessageRefEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-07-20 16:24
 * @description: mongodb消息dao
 */
@Repository
public class MessageMapper {
    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 插入Message集合
     */
    public String insert(MessageEntity messageEntity) {
        //北京时间转换格林威治时间
        DateTime offset = DateUtil.offset(messageEntity.getSendTime(), DateField.HOUR, 8);
        messageEntity.setSendTime(offset);
        MessageEntity save = mongoTemplate.save(messageEntity);
        //插入成功返回id
        return save.get_id();
    }


    /**
     * 根据用户id 页数 数量查询数据
     */
    public List<HashMap> searchMessageByPage(long userId, long page, long size) {
        JSONObject json = new JSONObject();
        json.set("$toString", "$_id");
        //查找接收者为userId的发送的消息
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.addFields().addField("id").withValue(json).build(),
                Aggregation.lookup("message_ref", "id", "messageId", "ref"),
                Aggregation.match(Criteria.where("ref.receiverId").is(userId)),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "sendTime")),
                Aggregation.skip(page),
                Aggregation.limit(size)
        );
        //处理消息
        AggregationResults<HashMap> result = mongoTemplate.aggregate(aggregation, "message", HashMap.class);
        List<HashMap> mappedResults = result.getMappedResults();
        mappedResults.forEach(one -> {
            List<MessageRefEntity> refEntities = (List<MessageRefEntity>) one.get("ref");
            MessageRefEntity messageRefEntity = refEntities.get(0);
            //获取ref中消息是否已读标志位
            Boolean readFlag = messageRefEntity.getReadFlag();
            //获取接收消息id
            String refId = messageRefEntity.get_id();
            one.remove("ref");
            one.put("readFlag", readFlag);
            one.put("refId", refId);
            //不需要该消息的id?
//            one.remove("_id");
            //转换北京时间
            Date sendTimeT = (Date) one.get("sendTime");
            DateTime sendTime = DateUtil.offset(sendTimeT, DateField.HOUR, -8);
            if (DateUtil.today().equals(DateUtil.date(sendTime).toDateStr())) {
                //如果消息是今天发送的,那么就不显示日期,只显示时间
                one.put("sendTime", DateUtil.format(sendTime, "HH:mm"));
            } else {
                //如果是旧消息,显示日期,不显示发送时间
                one.put("sendTime", DateUtil.format(sendTime, "yyyy/MM/dd"));
            }
        });
        return mappedResults;
    }


    /**
     * 通过消息id查询数据
     */
    public HashMap searchMessageById(String id) {
        HashMap message = mongoTemplate.findById(id, HashMap.class, "message");
        Date sendTime = (Date) message.get("sendTime");
        //转换北京时间
        sendTime = DateUtil.offset(sendTime, DateField.HOUR, -8);
        message.put("sendTime", DateUtil.format(sendTime, "yyyy/MM/dd HH:mm"));
        return message;
    }
}

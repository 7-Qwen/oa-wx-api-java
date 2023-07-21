package com.wen.oawxapi.task;

import com.rabbitmq.client.*;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.mongo.db.MessageEntity;
import com.wen.oawxapi.mongo.db.MessageRefEntity;
import com.wen.oawxapi.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-07-21 17:20
 * @description:
 */
@Component
@Slf4j
public class RabbitMQTask {
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource
    private MessageService messageService;


    /**
     * 同步发送消息
     * <p>
     * 参数topic使用 用户的id
     */
    public void send(String topic, MessageEntity messageEntity) {
        //发送消息需要在message集合中保存数据
        String messageId = messageService.insertMessage(messageEntity);
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            //连接topic
            channel.queueDeclare(topic, true, false, false, null);
            HashMap header = new HashMap(2);
            header.put("messageId", messageId);
            //创建AMQP协议参数对象,添加附加属性
            AMQP.BasicProperties build = new AMQP.BasicProperties().builder().headers(header).build();
            channel.basicPublish("", topic, build, messageEntity.getMsg().getBytes(StandardCharsets.UTF_8));
            log.info("消息发送成功");
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new CustomException("向MQ发送消息失败");
        }
    }

    /**
     * 异步发送消息
     */
    @Async
    public void sendAsync(String topic, MessageEntity messageEntity) {
        send(topic, messageEntity);
    }


    /**
     * 同步接收消息
     */
    public int receive(String topic) {
        int i = 0;
        try (
                //接收消息数据
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            //应答标识为false 手动应答
            GetResponse getResponse = channel.basicGet(topic, false);
            if (getResponse != null) {
                //获取消息属性
                AMQP.BasicProperties props = getResponse.getProps();
                //获取设置的header属性
                Map<String, Object> headers = props.getHeaders();
                String messageId = headers.get("messageId").toString();
                //获取消息正文
                String msg = new String(getResponse.getBody());
                log.debug("从mq中获取到消息:" + msg);
                //接收方持久化
                MessageRefEntity messageRefEntity = new MessageRefEntity();
                messageRefEntity.setMessageId(messageId);
                messageRefEntity.setReceiverId(Long.parseLong(topic));
                messageRefEntity.setReadFlag(false);
                messageRefEntity.setLastFlag(true);
                //存储接收方数据
                messageService.insertMessageRef(messageRefEntity);
                //存储完成后应答
                long deliveryTag = getResponse.getEnvelope().getDeliveryTag();
                //false表示只处理当前这条消息的应答,而不是之前所有的
                channel.basicAck(deliveryTag, false);
                i++;
            }
        } catch (Exception e) {
            log.error("执行异常", e);
        }
        return i;
    }


    /**
     * 异步接收消息
     */
    @Async
    public int receiveAsync(String topic) {
        return receive(topic);
    }


    /**
     * 同步删除队列
     */
    public void deleteQueen(String topic) {
        try (
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel()

        ) {
            channel.queueDelete(topic);
            log.debug("删除队列成功");
        } catch (Exception e) {
            log.error("执行异常" + e);
            throw new CustomException("删除队列失败");
        }
    }


    /**
     * 异步删除队列
     */
    @Async
    public void deleteQueenAsync(String topic) {
        deleteQueen(topic);
    }
}

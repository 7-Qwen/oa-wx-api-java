package com.wen.oawxapi.mongo.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: 7wen
 * @Date: 2023-07-20 15:10
 * @description: message主体发送信息
 */
@Data
@Document(collection = "message")
public class MessageEntity implements Serializable {
    @Id
    private String _id;

    /**
     * 该注解表示以什么字段为注解
     */
    @Indexed(unique = true)
    private String uuid;


    /**
     * 如果发送者是系统则为0
     */
    @Indexed
    private Integer senderId;

    private String senderName;

    /**
     * 头像固定
     **/
    private String senderPhoto = "https://storage-static-1311477017.cos.ap-beijing.myqcloud.com/oa-wx-web%E9%9D%99%E6%80%81%E8%B5%84%E6%BA%90/photo/%E6%9E%81%E9%99%90%E5%B7%85%E5%B3%B02022-6-28-21-3-5.jpg";

    @Indexed
    private Date sendTime;

    private String msg;

}

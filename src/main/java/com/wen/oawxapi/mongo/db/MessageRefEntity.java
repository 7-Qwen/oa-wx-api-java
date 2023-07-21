package com.wen.oawxapi.mongo.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: 7wen
 * @Date: 2023-07-20 15:18
 * @description:
 */
@Data
@Document(collection = "消息接收集合")
public class MessageRefEntity {
    @Id
    private String _id;
    @Indexed
    private String messageId;
    @Indexed
    private Long receiverId;
    @Indexed
    private Boolean readFlag;
    @Indexed
    private Boolean lastFlag;
}

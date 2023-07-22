import cn.hutool.core.util.IdUtil;
import com.wen.oawxapi.mongo.db.MessageEntity;
import com.wen.oawxapi.mongo.db.MessageRefEntity;
import com.wen.oawxapi.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: 7wen
 * @Date: 2023-07-22 19:14
 * @description:
 */
@SpringBootTest(classes = com.wen.oawxapi.OaWxApiApplication.class)
public class MessageTest {
    @Resource
    private MessageService messageService;

    @Test
    public void test1() {
        for (int i = 0; i < 50; i++) {
            MessageEntity message = new MessageEntity();
            message.setUuid(IdUtil.simpleUUID());
            message.setSenderId(0);
            message.setSenderName("系统消息");
            message.setMsg("这是第" + i + "条测试消息");
            message.setSendTime(new Date());
            String id = messageService.insertMessage(message);
            MessageRefEntity ref = new MessageRefEntity();
            ref.setMessageId(id);
            ref.setReceiverId(450); //注意：这是接收人ID
            ref.setLastFlag(true);
            ref.setReadFlag(false);
            messageService.insertMessageRef(ref);
        }
    }

}

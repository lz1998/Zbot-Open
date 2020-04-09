package xin.lz1998.zbot.plugin.hello;

import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;

/**
 * 不管收到什么私聊消息，都回复hello
 * 用于测试是否正常
 */
@Component
public class HelloPlugin extends CQPlugin {
    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        long userId = event.getUserId();
        cq.sendPrivateMsg(userId, "hello", false);
        return MESSAGE_IGNORE;
    }
}

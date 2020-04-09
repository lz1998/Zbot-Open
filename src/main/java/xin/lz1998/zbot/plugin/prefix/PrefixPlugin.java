package xin.lz1998.zbot.plugin.prefix;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;


/**
 * 这个插件用于预处理消息，如指令前缀
 */
@Component
public class PrefixPlugin extends CQPlugin {
    // 指令前缀 .
    private static final String prefix = ".";

    /**
     * 收到私聊消息时执行
     *
     * @param cq    机器人对象
     * @param event 事件
     * @return 是否继续执行
     */
    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        String msg = event.getMessage();

        if (msg.startsWith(prefix)) {
            // 消息以 prefix 开头，去除prefix，并继续执行下一个插件
            msg = msg.substring(prefix.length());
            event.setMessage(msg);
            return MESSAGE_IGNORE;
        } else {
            // 消息不以 prefix 开头，结束，不执行下一个插件
            return MESSAGE_BLOCK;
        }
    }

    /**
     * 收到群消息时执行
     *
     * @param cq    机器人对象
     * @param event 事件
     * @return 是否继续执行
     */
    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        String msg = event.getMessage();

        if (msg.startsWith(prefix)) {
            // 消息以 prefix 开头，去除prefix，并继续执行下一个插件
            msg = msg.substring(prefix.length());
            event.setMessage(msg);
            return MESSAGE_IGNORE;
        } else {
            // 消息不以 prefix 开头，结束，不执行下一个插件
            return MESSAGE_BLOCK;
        }
    }
}

package xin.lz1998.zbot.plugin.log;

import lombok.extern.slf4j.Slf4j;
import net.lz1998.cq.event.message.CQDiscussMessageEvent;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;


/**
 * 日志功能
 * 输出收到的消息
 */
@Slf4j
@Component
public class LogPlugin extends CQPlugin {

    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        log.info("[PRIVATE] bot: {} userId: {} message: {}", cq.getSelfId(), event.getUserId(), event.getMessage());
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        log.info("[GROUP] bot: {} groupId: {} userId: {} message: {}", cq.getSelfId(), event.getGroupId(), event.getUserId(), event.getMessage());
        return MESSAGE_IGNORE;
    }

    @Override
    public int onDiscussMessage(CoolQ cq, CQDiscussMessageEvent event) {
        log.info("[DISCUSS] bot: {} discussId: {} userId: {} message: {}", cq.getSelfId(), event.getDiscussId(), event.getUserId(), event.getMessage());
        return MESSAGE_IGNORE;
    }
}

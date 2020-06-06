package xin.lz1998.zbot.plugin.repeat;

import lombok.Getter;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.stereotype.Component;
import xin.lz1998.zbot.interfaces.INamedPlugin;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 复读机
 * 人类的本质
 */
@Component
public class RepeatPlugin extends CQPlugin implements INamedPlugin {
    @Getter
    public String pluginName = "复读";

    private static Random random = new Random();

    /**
     * 用于记录每个群上一条消息
     */
    Map<Long, String> lastMsgMap = new ConcurrentHashMap<>();

    /**
     * 用于记录每个群上次复读时间
     */
    Map<Long, Long> lastRepeatTimeMap = new ConcurrentHashMap<>();


    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        String msg = event.getMessage();
        Long groupId = event.getGroupId();

        // 正常指令忽略
        if (msg.startsWith(".")) {
            return MESSAGE_IGNORE;
        }

        // 最短300秒复读一次
        Long lastRepeatTime = lastRepeatTimeMap.getOrDefault(groupId, 0L);
        if (System.currentTimeMillis() - lastRepeatTime < 300 * 1000L) {
            return MESSAGE_IGNORE;
        }

        String lastMsg = lastMsgMap.getOrDefault(groupId, "<null/>");
        lastMsgMap.put(groupId, msg);
        if (lastMsg.equals(msg) && msg.length() < 50) {
            // 10% 概率复读
            if (random.nextInt(100) % 10 == 0) {
                lastRepeatTimeMap.put(groupId, System.currentTimeMillis());
                cq.sendGroupMsg(groupId, msg, false);
                return MESSAGE_IGNORE;
            }
        }
        return MESSAGE_IGNORE;
    }
}

package xin.lz1998.zbot.plugin.scramble;

import lombok.Getter;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xin.lz1998.zbot.interfaces.INamedPlugin;
import xin.lz1998.zbot.utils.HttpUtil;

import java.io.IOException;

@Component
public class ScramblePlugin extends CQPlugin implements INamedPlugin {
    @Getter
    public String pluginName = "打乱";

    private String scrambleUrl = "";
    private static final String LINESEP = "\r\n";


    @Value("${zbot.tnoodle}")
    public void setUrl(String domain) {
        scrambleUrl = "http://" + domain + "/scramble/.txt?=";
    }

    private String getScramble(String type) throws IOException {
        String url = scrambleUrl + type;
        String scramble = HttpUtil.getString(url).trim();

        // 五魔 U后面增加换行
        if ("minx".equals(type)) {
            scramble = scramble.replace("U' ", "U'\n");
            scramble = scramble.replace("U ", "U\n");
        }
        return scramble;
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        String msg = event.getMessage();
        Long groupId = event.getGroupId();
        String retmsg;

        for (TNoodleEnum puzzle : TNoodleEnum.values()) {
            if (msg.equals(puzzle.getInstruction())) {
                try {
                    String scramble = getScramble(puzzle.getShortName());
                    retmsg = puzzle.getShowName() + LINESEP + scramble;
                    cq.sendGroupMsg(groupId, retmsg, false);
                    return MESSAGE_BLOCK;
                } catch (IOException e) {
                    e.printStackTrace();
                    cq.sendGroupMsg(groupId, "获取打乱失败", false);
                    return MESSAGE_BLOCK;
                }
            }
        }

        return MESSAGE_IGNORE;
    }
}

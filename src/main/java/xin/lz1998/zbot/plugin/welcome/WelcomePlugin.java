package xin.lz1998.zbot.plugin.welcome;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.event.message.CQPrivateMessageEvent;
import net.lz1998.cq.event.notice.CQGroupIncreaseNoticeEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.lz1998.zbot.config.Config;
import xin.lz1998.zbot.service.AdminService;
import xin.lz1998.zbot.service.WelcomeService;

/**
 * 入群欢迎功能
 * 设置欢迎
 * 查看欢迎
 * 新人入群时发送欢迎内容
 */
@Component
public class WelcomePlugin extends CQPlugin {
    @Autowired
    WelcomeService welcomeService;

    @Autowired
    AdminService adminService;

    /**
     * 超级管理员设置默认欢迎内容，群号是0
     *
     * @param cq    机器人对象
     * @param event 事件
     * @return 是否继续执行
     */
    @Override
    public int onPrivateMessage(CoolQ cq, CQPrivateMessageEvent event) {
        String msg = event.getMessage();
        long userId = event.getSender().getUserId();
        if (msg.startsWith("设置欢迎")) {
            // 权限判断
            if (!Config.superAdminList.contains(userId)) {
                return MESSAGE_IGNORE;
            }

            // 获取欢迎内容
            msg = msg.substring("设置欢迎".length()).trim();

            // 存入数据库
            welcomeService.setWelcomeMsg(0L, msg, userId);

            // 回复
            cq.sendPrivateMsg(userId, "设置成功", false);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
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
        long userId = event.getUserId();
        long groupId = event.getGroupId();

        // 设置欢迎指令，并且有管理员权限
        if (msg.startsWith("设置欢迎") && adminService.isGroupAdmin(cq, groupId, userId)) {
            // 获取欢迎内容
            msg = msg.substring("设置欢迎".length()).trim();

            // 长度检测
            if (msg.length() > 2000) {
                cq.sendGroupMsg(groupId, "错误：欢迎 最大长度2000", false);
                return MESSAGE_BLOCK;
            }

            // 消息存入数据库
            welcomeService.setWelcomeMsg(groupId, msg, userId);

            // 提示
            cq.sendGroupMsg(groupId, "设置成功", false);
            return MESSAGE_BLOCK;
        }


        // 查看欢迎指令，并且有管理员权限
        if (msg.startsWith("查看欢迎") && adminService.isGroupAdmin(cq, groupId, userId)) {
            // 从数据库读取
            String welcomeMsg = welcomeService.getWelcomeMsg(groupId);

            // 如果非空发送消息
            if (!"<null/>".equalsIgnoreCase(welcomeMsg)) {
                // 变量替换，入群者和审批者
                welcomeMsg = welcomeMsg.replace("{{userId}}", Long.toString(userId));
                welcomeMsg = welcomeMsg.replace("{{operatorId}}", Long.toString(userId));
                cq.sendGroupMsg(groupId, welcomeMsg, false);
                return MESSAGE_BLOCK;
            } else {
                welcomeMsg = "无";
            }

            cq.sendGroupMsg(groupId, welcomeMsg, false);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    /**
     * 群成员增加时执行
     *
     * @param cq    机器人对象
     * @param event 事件
     * @return 是否继续执行下一个功能
     */
    @Override
    public int onGroupIncreaseNotice(CoolQ cq, CQGroupIncreaseNoticeEvent event) {
        long groupId = event.getGroupId();
        long userId = event.getUserId();
        long operatorId = event.getOperatorId();

        // 从数据库读取
        String welcomeMsg = welcomeService.getWelcomeMsg(groupId);

        // 如果非空发送消息
        if (!"<null/>".equalsIgnoreCase(welcomeMsg)) {
            // 变量替换，入群者和审批者
            welcomeMsg = welcomeMsg.replace("{{userId}}", Long.toString(userId));
            welcomeMsg = welcomeMsg.replace("{{operatorId}}", Long.toString(operatorId));
            cq.sendGroupMsg(groupId, welcomeMsg, false);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

}

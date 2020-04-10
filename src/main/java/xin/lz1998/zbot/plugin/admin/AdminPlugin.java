package xin.lz1998.zbot.plugin.admin;

import lombok.Getter;
import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.lz1998.zbot.interfaces.INamedPlugin;
import xin.lz1998.zbot.service.AdminService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AdminPlugin extends CQPlugin implements INamedPlugin {
    @Getter
    public String pluginName = "管理";

    @Autowired
    private AdminService adminService;

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {
        String msg = event.getMessage();
        Long userId = event.getUserId();
        Long groupId = event.getGroupId();
        String retMsg;


        // 查询管理员
        if ("列出管理员".equals(msg)) {
            List<Long> adminList = adminService.getAdminList(groupId);
            retMsg = "管理员列表";
            if (adminList.size() > 0) {
                for (Long adminId : adminList) {
                    retMsg += "\n" + adminId;
                }
            } else {
                retMsg += "\n无";
            }
            cq.sendGroupMsg(groupId, retMsg, false);
            return MESSAGE_BLOCK;
        }

        // 群主添加/删除管理员
        if (msg.startsWith("管理员") && adminService.isGroupOwner(cq, groupId, userId)) {
            msg = msg.substring("管理员".length());

            // 去除at
            msg = atDecode(msg).trim();
            String type = msg.substring(0, 1);
            msg = msg.substring(1).trim();
            Long adminId;
            try{
                adminId= Long.valueOf(msg);
            }catch (Exception e){
                cq.sendGroupMsg(groupId, "格式错误", false);
                return MESSAGE_BLOCK;
            }
            if ("+".equals(type)) {
                adminService.setAdmin(groupId, adminId, true);
                retMsg = "已添加管理员QQ:" + adminId;
                cq.sendGroupMsg(groupId, retMsg, false);
                return MESSAGE_BLOCK;
            } else if ("-".equals(type)) {
                adminService.setAdmin(groupId, adminId, false);
                retMsg = "已删除管理员QQ:" + adminId;
                cq.sendGroupMsg(groupId, retMsg, false);
                return MESSAGE_BLOCK;
            } else {
                cq.sendGroupMsg(groupId, "格式错误", false);
                return MESSAGE_BLOCK;
            }
        }

        // 踢人
        if (msg.toLowerCase().startsWith("t")) {
            // 检测指令发送者权限
            if(!adminService.isGroupAdmin(cq, groupId, userId)){
                return MESSAGE_IGNORE;
            }

            // 自身不是管理员
            if (!adminService.isGroupAdmin(cq, groupId, cq.getSelfId())) {
                retMsg = "我没有权限";
                cq.sendGroupMsg(groupId, retMsg, false);
                return MESSAGE_BLOCK;
            }

            // 是否允许再次申请
            boolean rejectAddRequest=msg.startsWith("T");

            // 去除指令
            msg = msg.substring("t".length());
            msg = atDecode(msg).trim();

            long kickId;
            try {
                kickId = Long.parseLong(msg);
            } catch (Exception e) {
                return MESSAGE_IGNORE;
            }

            // 要踢的人也是管理员
            if (adminService.isGroupAdmin(cq, groupId, kickId)) {
                cq.sendGroupMsg(groupId, "不能踢出管理员", false);
                return MESSAGE_BLOCK;
            }

            // 执行踢人并提示
            cq.setGroupKick(groupId, kickId, rejectAddRequest);
            cq.sendGroupMsg(groupId, "已踢"+kickId, false);
            return MESSAGE_BLOCK;
        }

        // 禁言
        if (msg.toLowerCase().startsWith("ban") || msg.startsWith("禁言")) {
            // 检测指令发送者权限
            if(!adminService.isGroupAdmin(cq, groupId, userId)){
                return MESSAGE_IGNORE;
            }

            // 自身不是管理员
            if (!adminService.isGroupAdmin(cq, groupId, cq.getSelfId())) {
                cq.sendGroupMsg(groupId, "我没有权限", false);
                return MESSAGE_BLOCK;
            }

            msg = msg.toLowerCase();
            msg = msg.replace("ban", "");
            msg = msg.replace("禁言", "");

            // 处理@
            msg = atDecode(msg).trim();

            // 处理不同类型分割符号
            msg = msg.replace(" ", "");
            msg = msg.replace("，", ",");
            msg = msg.replace("/", ",");
            msg = msg.replace("-", ",");

            String[] tmp = msg.split(",");
            // 没写时间
            if (tmp.length < 2) {
                cq.sendGroupMsg(groupId, "格式错误", false);
                return MESSAGE_BLOCK;
            }

            long banId;
            try {
                banId = Long.parseLong(tmp[0]);
            } catch (Exception e) {
                // 不是禁言信息
                return MESSAGE_IGNORE;
            }


            // 要禁言的人也是管理员
            if (adminService.isGroupAdmin(cq, groupId, banId)) {
                retMsg = "不能禁言管理员";
                cq.sendGroupMsg(groupId, retMsg, false);
                return MESSAGE_BLOCK;
            }

            long duration=getBanDuration(tmp[1]);

            // 执行禁言
            if (duration > 0) {
                cq.setGroupBan(groupId, banId, duration);
                retMsg = "已禁言\nQQ:" + banId + "\n时间："+getBanDurationStr(duration);
            } else {
                cq.setGroupBan(groupId, banId, 0L);
                retMsg = "已解除禁言";
            }

            // 提示
            cq.sendGroupMsg(groupId, retMsg, false);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    /**
     * 获取禁言时间
     * @param timeStr 字符串，1d1h1m1s，1天1小时1分1秒
     * @return 禁言时间
     */
    private long getBanDuration(String timeStr){
        // 默认单位 秒
        String durationStr = timeStr.trim() + "s";

        // 正则表达式获取时间 ([0-9]+[dmhs])
        Pattern pattern = Pattern.compile("([0-9]+)([dmhs])");
        Matcher matcher = pattern.matcher(durationStr);
        long duration = 0L;
        while (matcher.find()) {
            int rate = 0;
            switch (matcher.group(2)) {
                case "d": {
                    rate = 24 * 3600;
                    break;
                }
                case "h": {
                    rate = 3600;
                    break;
                }
                case "m": {
                    rate = 60;
                    break;
                }
                case "s": {
                    rate = 1;
                    break;
                }
            }
            duration += Long.parseLong(matcher.group(1)) * rate;
        }

        // 最大30天
        if (duration > 30 * 24 * 3600) {
            duration = 30 * 24 * 3600L;
        }

        // 最小1分钟，小于等于0表示解除
        if (duration < 60 && duration>0) {
            duration = 60L;
        }

        return duration;
    }

    /**
     * 获取禁言时间字符串
     * @param duration 禁言时间 秒
     * @return 禁言时间字符串
     */
    private String getBanDurationStr(long duration){
        // 格式化回复时间
        long day = duration / (24 * 3600);
        duration %= 24 * 3600;
        long hour = duration / 3600;
        duration %= 3600;
        long min = duration / 60;
        duration %= 60;
        long sec = duration;

        String retMsg="";
        if (day > 0) {
            retMsg += day + "天";
        }
        if (hour > 0) {
            retMsg += hour + "时";
        }
        if (min > 0) {
            retMsg += min + "分";
        }
        if (sec > 0) {
            retMsg += sec + "秒";
        }
        return retMsg;
    }

    /**
     * 去除CQ码 at
     * @param msg 消息
     * @return 去除后剩余内容
     */
    private String atDecode(String msg) {
        msg = msg.replace("[CQ:at,qq=", "");
        msg = msg.replace("[cq:at,qq=", "");
        msg = msg.replace("]", "");
        msg = msg.trim();
        return msg;
    }

}

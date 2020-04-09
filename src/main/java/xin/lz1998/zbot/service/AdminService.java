package xin.lz1998.zbot.service;

import net.lz1998.cq.robot.CoolQ;

import java.util.List;

public interface AdminService {
    /**
     * 是否超级管理员
     *
     * @param userId QQ
     * @return 是否超级管理员
     */
    Boolean isSuperAdmin(Long userId);


    /**
     * 是否群主
     *
     * @param cq      机器人对象
     * @param groupId 群号
     * @param userId  QQ
     * @return 是否群主
     */
    Boolean isGroupOwner(CoolQ cq, Long groupId, Long userId);


    /**
     * 是否管理员
     *
     * @param cq      机器人对象
     * @param groupId 群号
     * @param userId  QQ
     * @return 是否管理员
     */
    Boolean isGroupAdmin(CoolQ cq, Long groupId, Long userId);

    /**
     * 设置管理员 新增/删除
     *
     * @param groupId 群号
     * @param userId  QQ
     * @param isAdmin 是否管理员
     */
    void setAdmin(Long groupId, Long userId, Boolean isAdmin);

    /**
     * 获取管理员QQ列表
     *
     * @param groupId 群号
     * @return 管理员QQ列表
     */
    List<Long> getAdminList(Long groupId);
}

package xin.lz1998.zbot.service.impl;

import net.lz1998.cq.retdata.ApiData;
import net.lz1998.cq.retdata.GroupMemberInfoData;
import net.lz1998.cq.robot.CoolQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.lz1998.zbot.config.Config;
import xin.lz1998.zbot.entity.Admin;
import xin.lz1998.zbot.repository.AdminRepository;
import xin.lz1998.zbot.service.AdminService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    /**
     * 是否超级管理员
     *
     * @param userId QQ
     * @return 是否超级管理员
     */
    @Override
    public Boolean isSuperAdmin(Long userId) {
        return Config.superAdminList.contains(userId);
    }

    /**
     * 是否群主或超级管理员
     *
     * @param cq      机器人对象
     * @param groupId 群号
     * @param userId  QQ
     * @return 是否群主
     */
    @Override
    public Boolean isGroupOwner(CoolQ cq, Long groupId, Long userId) {
        ApiData<GroupMemberInfoData> senderInfo = cq.getGroupMemberInfo(groupId, userId, false);
        if (senderInfo == null) {
            return false;
        }

        return Config.superAdminList.contains(userId)
                || senderInfo.getData().getRole().equals("owner");
    }

    /**
     * 是否管理员或群主或超级管理员
     *
     * @param cq      机器人对象
     * @param groupId 群号
     * @param userId  QQ
     * @return 是否管理员
     */
    @Override
    public Boolean isGroupAdmin(CoolQ cq, Long groupId, Long userId) {
        ApiData<GroupMemberInfoData> senderInfo = cq.getGroupMemberInfo(groupId, userId, false);
        Admin admin = adminRepository.findAdminByGroupIdAndUserId(groupId, userId);

        if (senderInfo == null) {
            return false;
        }

        return Config.superAdminList.contains(userId)
                || senderInfo.getData().getRole().equals("owner")
                || senderInfo.getData().getRole().equals("admin")
                || (admin != null && admin.getIsAdmin());
    }


    /**
     * 设置管理员 新增/删除
     *
     * @param groupId 群号
     * @param userId  QQ
     * @param isAdmin 是否管理员
     */
    @Override
    public void setAdmin(Long groupId, Long userId, Boolean isAdmin) {
        Admin admin = Admin.builder()
                .groupId(groupId)
                .userId(userId)
                .isAdmin(isAdmin)
                .build();
        adminRepository.save(admin);
    }

    /**
     * 获取管理员QQ列表
     *
     * @param groupId 群号
     * @return 管理员QQ列表
     */
    @Override
    public List<Long> getAdminList(Long groupId) {
        List<Admin> adminList = adminRepository.findAdminsByGroupIdAndAndIsAdmin(groupId, true);
        return adminList.stream().map(Admin::getUserId).collect(Collectors.toList());
    }


}

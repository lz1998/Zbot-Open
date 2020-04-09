package xin.lz1998.zbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.lz1998.zbot.entity.Welcome;
import xin.lz1998.zbot.repository.WelcomeRepository;
import xin.lz1998.zbot.service.WelcomeService;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    @Autowired
    private WelcomeRepository welcomeRepository;

    /**
     * 保存入群欢迎内容
     *
     * @param groupId    群号
     * @param welcomeMsg 欢迎内容
     * @param adminId    管理员QQ
     * @return 欢迎内容
     */
    @Override
    public String setWelcomeMsg(Long groupId, String welcomeMsg, Long adminId) {
        Welcome welcome = Welcome.builder()
                .groupId(groupId)
                .welcomeMsg(welcomeMsg)
                .adminId(adminId)
                .build();
        return welcomeRepository.save(welcome).getWelcomeMsg();
    }

    /**
     * 获取欢迎内容
     *
     * @param groupId 群号
     * @return 欢迎内容
     */
    @Override
    public String getWelcomeMsg(Long groupId) {
        Welcome welcome = welcomeRepository.findWelcomeByGroupId(groupId);
        if (welcome == null || welcome.getWelcomeMsg().equals("<default/>")) {
            // 如果群内没有设置，那么是默认消息，默认群号是0
            if (!groupId.equals(0L)) {
                return getWelcomeMsg(0L);
            } else {
                return "<null/>";
            }
        }
        return welcome.getWelcomeMsg();
    }
}

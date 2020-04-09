package xin.lz1998.zbot.config;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局配置
 */
@Component
public class Config {
    /**
     * 超级管理员列表
     */
    public static List<Long> superAdminList = new ArrayList<>();

    static {
        superAdminList.add(875543533L);
    }

}

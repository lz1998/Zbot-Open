package xin.lz1998.zbot.interfaces;

/**
 * 实现这个接口的插件有名字
 * 可以使用AOP实现开关功能
 */
public interface INamedPlugin {
    String getPluginName();
}

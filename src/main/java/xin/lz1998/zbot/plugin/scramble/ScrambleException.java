package xin.lz1998.zbot.plugin.scramble;

/**
 * 获取打乱错误
 */
public class ScrambleException extends RuntimeException {
    public ScrambleException() {
        super("get scramble error");
    }

    public ScrambleException(String message) {
        super(message);
    }
}

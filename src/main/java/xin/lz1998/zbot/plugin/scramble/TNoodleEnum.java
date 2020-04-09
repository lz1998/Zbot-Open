package xin.lz1998.zbot.plugin.scramble;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TNoodleEnum {
    PUZZLE_222("2", "222", "2阶"),
    PUZZLE_333("3", "333", "3阶"),
    PUZZLE_444("4", "444", "4阶"),
    PUZZLE_555("5", "555", "5阶"),
    PUZZLE_666("6", "666", "6阶"),
    PUZZLE_777("7", "777", "7阶"),
    PUZZLE_PYRAM("py", "pyram", "pyram"),
    PUZZLE_SKEWB("sk", "skewb", "skewb"),
    PUZZLE_SQ1("sq", "sq1", "sq1"),
    PUZZLE_CLOCK("cl", "clock", "clock"),
    PUZZLE_MINX("mx", "minx", "minx"),
    PUZZLE_333FM("fm", "333fm", "333fm");

    /**
     * 指令
     */
    private String instruction;

    /**
     * tnoodle调用时的参数
     */
    private String shortName;

    /**
     * 回复消息显示内容
     */
    private String showName;
}

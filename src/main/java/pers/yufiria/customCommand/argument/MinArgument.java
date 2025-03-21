package pers.yufiria.customCommand.argument;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinArgument {
    private final int min;
    private final @Nullable String hint;

    public MinArgument(int min, @Nullable String hint) {
        this.min = min;
        this.hint = hint;
    }

    /**
     * 判断参数长度是否不小于要求
     */
    public boolean checkArgumentLength(List<String> arguments) {
        if (min < 0) {
            return true;
        }
        return arguments.size() >= min;
    }

    public int min() {
        return min;
    }

    public @Nullable String hint() {
        return hint;
    }
}

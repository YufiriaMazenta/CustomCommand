package pers.yufiria.customCommand.core.argument;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MaxArgument {

    private final int max;
    private final @Nullable String hint;

    public MaxArgument(int max, @Nullable String hint) {
        this.max = max;
        this.hint = hint;
    }

    /**
     * 判断参数长度是否不大于要求
     */
    public boolean checkArgumentLength(List<String> arguments) {
        if (max < 0) {
            return true;
        }
        return arguments.size() <= max;
    }

    public int max() {
        return max;
    }

    public @Nullable String hint() {
        return hint;
    }

}

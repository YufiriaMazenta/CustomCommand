package pers.yufiria.customCommand.core.tab;

import crypticlib.command.CommandInvoker;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandTabCompleter {

    /**
     * 返回命令参数补全列表
     *
     * @param invoker 命令的执行者
     * @param args    命令的完整参数
     * @return 参数补全列表
     */
    @NotNull List<String> tabComplete(CommandInvoker invoker, List<String> args);

}

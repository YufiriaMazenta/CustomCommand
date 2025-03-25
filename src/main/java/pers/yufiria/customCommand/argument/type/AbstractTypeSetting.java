package pers.yufiria.customCommand.argument.type;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractTypeSetting {

    protected String hint;

    public AbstractTypeSetting(String hint) {
        this.hint = hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Nullable
    public String hint() {
        return hint;
    }

    public abstract boolean checkArgument(String argument);

}

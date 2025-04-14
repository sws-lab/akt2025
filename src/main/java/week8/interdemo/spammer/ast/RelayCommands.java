package week8.interdemo.spammer.ast;

import java.util.Objects;

public abstract class RelayCommands {
    protected final String msg;
    protected final String name;

    protected RelayCommands(String msg, String name) {
        this.msg = msg;
        this.name = name;
    }

    public abstract void send();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelayCommands that = (RelayCommands) o;
        return Objects.equals(msg, that.msg) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msg, name);
    }

}

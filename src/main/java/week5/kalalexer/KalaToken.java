package week5.kalalexer;

import java.util.Objects;

public class KalaToken {

    public enum Type {
        IDENT,
        COMMA,
        LPAREN, RPAREN,
        NULL,
        EOF
    }


    private final Object data;
    private final Type type;

    public KalaToken(Type type) {
        this(type, null);
    }

    public KalaToken(Type type, Object data) {
        assert type != null;

        this.type = type;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "<" + type
                + (data == null ? "" : (":" + data))
                + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KalaToken that)) {
            return false;
        }
        return this.type.equals(that.type) && Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        if (data != null) {
            return type.hashCode() + data.hashCode();
        } else {
            return type.hashCode();
        }
    }
}

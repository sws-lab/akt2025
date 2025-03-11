package week4.regex.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Regulaaravaldise süntakspuu tipude ülemklass. Annab ka homogeense vaade ASTile.
 */

public abstract class RegexNode {
    private final char type;
    private final List<RegexNode> children;

    public RegexNode(char type, RegexNode... children) {
        this.type = type;
        this.children = Arrays.asList(children);
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", type + "(", ")");
        joiner.setEmptyValue(Character.toString(type));

        for (RegexNode child : children) {
            joiner.add(child.toString());
        }

        return joiner.toString();
    }

    public char getType() {
        return type;
    }

    public List<RegexNode> getChildren() {
        return children;
    }

    public RegexNode getChild(int i) {
        return children.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegexNode regexNode = (RegexNode) o;
        return type == regexNode.type &&
                Objects.equals(children, regexNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, children);
    }

    public abstract <R> R accept(RegexVisitor<R> visitor);
}



package week5.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Node {
    private final String label;
    private final List<Node> children;

    public Node(String label) {
        this.label = label;
        this.children = new ArrayList<>();
    }

    public Node(char label) {
        this(Character.toString(label));
    }

    public void add(Node n) {
        children.add(n);
    }

    public String toString() {
        if (children.isEmpty()) return label;
        StringJoiner joiner = new StringJoiner(",", label + "(", ")");
        for (Node child : children) joiner.add(child.toString());
        return joiner.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return Objects.equals(label, node.label) &&
                Objects.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, children);
    }

}


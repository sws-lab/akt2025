package week6.parsers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class Node {
    final String label;
    List<Node> children;

    public Node(String label) {
        this.label = label;
        this.children = new ArrayList<>();
    }

    public Node(String label, Node... children) {
        this(label);
        this.children = new ArrayList<>(Arrays.asList(children));
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

    public void renderPngFile(Path path) throws IOException {
        NodeVisualizer.renderPngFile(this, path);
    }
}


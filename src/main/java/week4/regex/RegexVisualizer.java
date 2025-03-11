package week4.regex;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import week4.regex.ast.RegexNode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class RegexVisualizer {

    public static void main(String[] args) throws IOException {
        RegexNode node = RegexParser.parse("(a|b)*cd");
        renderPngFile(node, Paths.get("graphs", "regex.png"));
    }


    private int nextNodeId = 0;

    private Node makeNode(String label) {
        return node(Integer.toString(nextNodeId++)).with(Label.of(label));
    }

    protected Node visit(RegexNode node) {
        return makeNode(Character.toString(node.getType())).link(
                node.getChildren().stream()
                        .map(this::visit)
                        .collect(Collectors.toList())
        );
    }

    public static void renderPngFile(RegexNode node, Path path) throws IOException {
        Node rootNode = new RegexVisualizer().visit(node);
        Graph graph = graph("regex").directed().with(rootNode).graphAttr().with("ordering", "out");
        //System.out.println(Graphviz.fromGraph(graph).render(Format.DOT).toString());
        Graphviz.fromGraph(graph).scale(3).render(Format.PNG).toFile(path.toFile().getCanonicalFile());
    }
}

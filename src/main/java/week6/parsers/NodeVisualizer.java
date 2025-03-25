package week6.parsers;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class NodeVisualizer {

    private int nextNodeId = 0;

    private Node makeNode(String label) {
        return node(Integer.toString(nextNodeId++)).with(Label.of(label));
    }

    public Node visit(week6.parsers.Node node) {
        return makeNode(node.label).link(
                node.children.stream()
                        .map(this::visit)
                        .collect(Collectors.toList())
        );
    }

    public static void renderPngFile(week6.parsers.Node node, Path path) throws IOException {
        Node rootNode = new NodeVisualizer().visit(node);
        Graph graph = graph("tree").directed().with(rootNode).graphAttr().with("ordering", "out");
        //System.out.println(Graphviz.fromGraph(graph).render(Format.DOT).toString());
        Graphviz.fromGraph(graph).scale(3).render(Format.PNG).toFile(path.toFile().getCanonicalFile());
    }
}

package week3;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.Node;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.model.Factory.*;

public class AbstractAutomatonVisualizer {

    private final AbstractAutomaton automaton;

    public AbstractAutomatonVisualizer(AbstractAutomaton automaton) {
        this.automaton = automaton;
    }

    private Node makeNode(Integer state) {
        return node(Integer.toString(state));
    }

    private Shape getStateShape(Integer state) {
        return automaton.getAcceptingStates().contains(state) ? Shape.DOUBLE_CIRCLE : Shape.CIRCLE;
    }

    private Label getLabelLabel(Character label) {
        if (label != null && label == 'ε') return Label.of("RTFM!");
        return Label.of(Character.toString(label == null ? 'ε' : label));
    }

    private List<Link> getStateLinks(Integer state) {
        return automaton.getOutgoingLabels(state).stream()
                .flatMap(label ->
                    automaton.getDestinations(state, label).stream()
                            .map(toState ->
                                to(makeNode(toState))
                                        .with(getLabelLabel(label))
                            )
                )
                .collect(Collectors.toList());
    }

    private Graph getGraph() {
        List<Node> stateNodes = automaton.getStates().stream()
                .map(state ->
                    makeNode(state)
                            .with(getStateShape(state))
                            .link(getStateLinks(state))
                )
                .collect(Collectors.toList());

        Node nullNode = node("null")
                .with(Label.of(""), Shape.NONE, attr("width", 0), attr("height", 0))
                .link(to(makeNode(automaton.getStartState())));

        return graph("automaton")
                .directed()
                .with(stateNodes)
                .with(nullNode)
                .graphAttr()
                        .with(Rank.dir(RankDir.LEFT_TO_RIGHT));
    }

    public static void renderPngFile(AbstractAutomaton automaton, Path path) throws IOException {
        Graph graph = new AbstractAutomatonVisualizer(automaton).getGraph();
        //System.out.println(Graphviz.fromGraph(graph).render(Format.DOT).toString());
        Graphviz.fromGraph(graph).scale(2).render(Format.PNG).toFile(path.toFile().getCanonicalFile());
    }
}

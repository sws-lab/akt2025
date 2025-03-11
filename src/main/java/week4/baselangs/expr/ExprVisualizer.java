package week4.baselangs.expr;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import week4.baselangs.expr.ast.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static week4.baselangs.expr.ast.ExprNode.*;

public class ExprVisualizer extends ExprVisitor<Node> {

    public static void main(String[] args) throws IOException {
        ExprNode expr = div(add(num(5), add(num(3), neg(num(2)))), num(2));
        renderPngFile(expr, Paths.get("graphs", "expr.png"));
    }


    private int nextNodeId = 0;

    private Node makeNode(String label) {
        return node(Integer.toString(nextNodeId++)).with(Label.of(label));
    }

    @Override
    protected Node visit(ExprNum num) {
        return makeNode(Integer.toString(num.getValue()));
    }

    @Override
    protected Node visit(ExprNeg neg) {
        return makeNode("-").link(
                visit(neg.getExpr())
        );
    }

    @Override
    protected Node visit(ExprAdd add) {
        return makeNode("+").link(
                visit(add.getLeft()),
                visit(add.getRight())
        );
    }

    @Override
    protected Node visit(ExprDiv div) {
        return makeNode("/").link(
                visit(div.getNumerator()),
                visit(div.getDenominator())
        );
    }

    public static void renderPngFile(ExprNode node, Path path) throws IOException {
        Node rootNode = new ExprVisualizer().visit(node);
        Graph graph = graph("expr").directed().with(rootNode).graphAttr().with("ordering", "out");
        //System.out.println(Graphviz.fromGraph(graph).render(Format.DOT).toString());
        Graphviz.fromGraph(graph).scale(3).render(Format.PNG).toFile(path.toFile().getCanonicalFile());
    }
}

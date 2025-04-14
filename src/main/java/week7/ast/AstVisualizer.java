package week7.ast;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import org.apache.commons.text.StringEscapeUtils;
import week7.AktkAst;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.nidi.graphviz.attribute.Records.*;
import static guru.nidi.graphviz.model.Compass.*;
import static guru.nidi.graphviz.model.Factory.*;

public class AstVisualizer extends AstVisitor<Node> {

    public static void main(String[] args) throws IOException {
        String program = Files.readString(Paths.get("inputs", "sample.aktk"));
        AstNode ast = AktkAst.createAst(program);
        renderPngFile(ast, Paths.get("graphs", "aktk.png"));
    }


    private int nextNodeId = 0;

    private Node makeNode(String rec) {
        return node(Integer.toString(nextNodeId++)).with(Records.of(rec));
    }

    private Link withLabel(Node node, String label) {
        return to(node).with(Label.of(label));
    }

    private Node visit(Object object) {
        // also handles non-AstNode elements and nulls
        if (object instanceof AstNode node)
            return super.visit(node);
        else if (object instanceof String s)
            return makeNode(rec("\"" + StringEscapeUtils.escapeJava(s) + "\""));
        else
            return makeNode(rec(Objects.toString(object)));
    }

    private Link visitLabel(Object object, String label) {
        return withLabel(visit(object), label);
    }

    private List<LinkTarget> visitListLabel(List<?> objects, String label) {
        List<LinkTarget> targets = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            Object object = objects.get(i);
            targets.add(visitLabel(object, "%s[%d]".formatted(label, i)));
        }
        return targets;
    }

    private Node makeNodeLink(AstNode node, List<LinkTarget> linkTargets) {
        List<String> linkRecs = new ArrayList<>();
        List<LinkTarget> linkRecLinkTargets = new ArrayList<>();
        for (LinkTarget linkTarget : linkTargets) {
            Link link = linkTarget.linkTo();
            String linkLabel = Objects.requireNonNull(link.attrs().get("label")).toString();
            @SuppressWarnings("UnnecessaryLocalVariable") String linkRecTag = linkLabel;
            linkRecs.add(rec(linkRecTag, linkLabel));
            linkRecLinkTargets.add(between(port(linkRecTag, SOUTH), link.to()));
        }
        return makeNode(turn(rec(node.getSimpleName()), turn(linkRecs.toArray(new String[0])))).link(linkRecLinkTargets);
    }

    private Node makeNodeLink(AstNode node, LinkTarget... linkTargets) {
        return makeNodeLink(node, Arrays.asList(linkTargets));
    }

    @Override
    protected Node visit(Assignment assignment) {
        return makeNodeLink(assignment,
                visitLabel(assignment.getVariableName(), "variableName"),
                visitLabel(assignment.getExpression(), "expression")
        );
    }

    @Override
    protected Node visit(Block block) {
        return makeNodeLink(block,
                visitListLabel(block.getStatements(), "statements")
        );
    }

    @Override
    protected Node visit(ExpressionStatement expressionStatement) {
        return makeNodeLink(expressionStatement,
                visitLabel(expressionStatement.getExpression(), "expression")
        );
    }

    @Override
    protected Node visit(FunctionCall functionCall) {
        List<LinkTarget> targets = new ArrayList<>();
        targets.add(visitLabel(functionCall.getFunctionName(), "functionName"));
        targets.addAll(visitListLabel(functionCall.getArguments(), "arguments"));
        return makeNodeLink(functionCall, targets);
    }

    @Override
    protected Node visit(FunctionDefinition functionDefinition) {
        List<LinkTarget> targets = new ArrayList<>();
        targets.add(visitLabel(functionDefinition.getName(), "name"));
        List<FunctionParameter> parameters = functionDefinition.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            FunctionParameter parameter = parameters.get(i);
            // FunctionParameter can't be visited with AstVisitor
            Node parameterNode = makeNodeLink(parameter,
                    visitLabel(parameter.getVariableName(), "variableName"),
                    visitLabel(parameter.getType(), "type")
            );
            targets.add(withLabel(parameterNode, "parameters[%d]".formatted(i)));
        }
        targets.add(visitLabel(functionDefinition.getReturnType(), "returnType"));
        targets.add(visitLabel(functionDefinition.getBody(), "body"));
        return makeNodeLink(functionDefinition, targets);
    }

    @Override
    protected Node visit(IfStatement ifStatement) {
        return makeNodeLink(ifStatement,
                visitLabel(ifStatement.getCondition(), "condition"),
                visitLabel(ifStatement.getThenBranch(), "thenBranch"),
                visitLabel(ifStatement.getElseBranch(), "elseBranch")
        );
    }

    @Override
    protected Node visit(IntegerLiteral integerLiteral) {
        return makeNodeLink(integerLiteral,
                visitLabel(integerLiteral.getValue(), "value")
        );
    }

    @Override
    protected Node visit(ReturnStatement returnStatement) {
        return makeNodeLink(returnStatement,
                visitLabel(returnStatement.getExpression(), "expression")
        );
    }

    @Override
    protected Node visit(StringLiteral stringLiteral) {
        return makeNodeLink(stringLiteral,
                visitLabel(stringLiteral.getValue(), "value")
        );
    }

    @Override
    protected Node visit(Variable variable) {
        return makeNodeLink(variable,
                visitLabel(variable.getName(), "name")
        );
    }

    @Override
    protected Node visit(VariableDeclaration variableDeclaration) {
        return makeNodeLink(variableDeclaration,
                visitLabel(variableDeclaration.getVariableName(), "variableName"),
                visitLabel(variableDeclaration.getType(), "type"),
                visitLabel(variableDeclaration.getInitializer(), "initializer")
        );
    }

    @Override
    protected Node visit(WhileStatement whileStatement) {
        return makeNodeLink(whileStatement,
                visitLabel(whileStatement.getCondition(), "condition"),
                visitLabel(whileStatement.getBody(), "body")
        );
    }

    public static void renderPngFile(AstNode node, Path path) throws IOException {
        Node rootNode = new AstVisualizer().visit(node);
        Graph graph = graph("aktk").directed().with(rootNode).graphAttr().with("ordering", "out");
        //System.out.println(Graphviz.fromGraph(graph).render(Format.DOT).toString());
        Graphviz.fromGraph(graph).scale(2).render(Format.PNG).toFile(path.toFile().getCanonicalFile());
    }
}

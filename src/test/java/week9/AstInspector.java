package week9;

import week7.ast.*;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * AKTK AST-ist mingite tippude leidmise klass.
 *
 * Kasutatakse testimiseks.
 */
class AstInspector {
    private final AstNode ast;

    AstInspector(AstNode ast) {
        this.ast = ast;
    }

    public Stream<AstNode> streamNodes() {
        return streamNodes(ast);
    }

    private Stream<AstNode> streamNodes(AstNode node) {
        return Stream.concat(
                Stream.of(node),
                node.getChildren().stream()
                        .filter(child -> child instanceof AstNode)
                        .flatMap(child -> streamNodes((AstNode) child))
        );
    }

    public <T extends AstNode> Stream<T> streamNodes(Class<T> clazz) {
        return streamNodes()
                .filter(node -> node.getClass() == clazz)
                .map(clazz::cast);
    }

    private static <T> T find(Stream<T> stream, Predicate<T> predicate, int index) {
        return stream
                .filter(predicate)
                .skip(index)
                .findFirst().orElseThrow(() -> new RuntimeException("Non-existent node"));
    }

    // konkreetsete tiputüüpide abimeetodid...

    public Variable findVariable(String name, int index) {
        return find(
                streamNodes(Variable.class),
                variable -> variable.getName().equals(name),
                index);
    }

    public VariableDeclaration findVariableDeclaration(String name, int index) {
        return find(
                streamNodes(VariableDeclaration.class),
                variable -> variable.getVariableName().equals(name),
                index);
    }

    public FunctionParameter findFunctionParameter(String name, int index) {
        return find(
                streamNodes(FunctionParameter.class),
                parameter -> parameter.getVariableName().equals(name),
                index);
    }

    public Assignment findAssignment(String variableName, int index) {
        return find(
                streamNodes(Assignment.class),
                assignment -> assignment.getVariableName().equals(variableName),
                index);
    }

    public FunctionCall findFunctionCall(String name, int index) {
        return find(
                streamNodes(FunctionCall.class),
                functionCall -> functionCall.getFunctionName().equals(name),
                index);
    }

    public FunctionDefinition findFunctionDefinition(String name, int index) {
        return find(
                streamNodes(FunctionDefinition.class),
                functionDefinition -> functionDefinition.getName().equals(name),
                index);
    }

    public ReturnStatement findReturn(int index) {
        return find(
                streamNodes(ReturnStatement.class),
                returnStatement -> true,
                index);
    }
}

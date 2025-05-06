package week9;

import week7.ast.*;
import week8.Environment;


public class AktkBinding {
    /**
     * Määra iga antud programmis olevale muutujaviitele (week7.ast.Variable)
     * teda siduv element (week7.ast.VariableBinding,
     * st week7.ast.VariableDeclaration või week7.ast.FunctionParameter)
     * Kasuta selleks meetodit week7.ast.Variable#setBinding.
     *
     * Kui muutuja kasutusele ei vasta ühtegi deklaratsiooni ega parameetrit,
     * siis jäta binding määramata.
     */
    public static void bind(AstNode node) {
        new AktkBindingVisitor().visit(node);
    }

    private static class AktkBindingVisitor extends AstVisitor.VoidVisitor {

        // Kuna hoiame samas keskkonnas nii VariableDeclaration kui ka FunctionDefinition objekte,
        // siis kasutame siin üldist AstNode ja cast'ime nii nagu vaja on
        private final Environment<AstNode> env = new Environment<>();

        // Spetsiaalne muutujanimi, mida kasutame funktsiooni definitsiooni hoidmiseks keskkonnas
        private static final String FUNCTION_BINDING_NAME = "__function__";

        @Override
        protected void visitVoid(Assignment assignment) {
            assignment.setBinding((VariableBinding) env.get(assignment.getVariableName()));
            visit(assignment.getExpression());
        }

        @Override
        protected void visitVoid(Block block) {
            env.enterBlock();
            for (Statement statement : block.getStatements()) {
                visit(statement);
            }
            env.exitBlock();
        }

        @Override
        protected void visitVoid(ExpressionStatement expressionStatement) {
            visit(expressionStatement.getExpression());
        }

        @Override
        protected void visitVoid(FunctionCall functionCall) {
            functionCall.setFunctionBinding((FunctionDefinition) env.get(functionCall.getFunctionName()));

            for (Expression expression : functionCall.getArguments()) {
                visit(expression);
            }
        }

        @Override
        protected void visitVoid(FunctionDefinition functionDefinition) {
            env.declareAssign(functionDefinition.getName(), functionDefinition);

            env.enterBlock();
            for (FunctionParameter param : functionDefinition.getParameters()) {
                env.declareAssign(param.getVariableName(), param);
            }
            env.declareAssign(FUNCTION_BINDING_NAME, functionDefinition);
            visit(functionDefinition.getBody());
            env.exitBlock();
        }

        @Override
        protected void visitVoid(IfStatement ifStatement) {
            visit(ifStatement.getCondition());
            visit(ifStatement.getThenBranch());
            visit(ifStatement.getElseBranch());
        }

        @Override
        protected void visitVoid(IntegerLiteral integerLiteral) {

        }

        @Override
        protected void visitVoid(ReturnStatement returnStatement) {
            returnStatement.setFunctionBinding((FunctionDefinition) env.get(FUNCTION_BINDING_NAME));
            visit(returnStatement.getExpression());
        }

        @Override
        protected void visitVoid(StringLiteral stringLiteral) {

        }

        @Override
        protected void visitVoid(Variable variable) {
            variable.setBinding((VariableBinding) env.get(variable.getName()));
        }

        @Override
        protected void visitVoid(VariableDeclaration variableDeclaration) {
            if (variableDeclaration.getInitializer() != null)
                visit(variableDeclaration.getInitializer());
            env.declareAssign(variableDeclaration.getVariableName(), variableDeclaration);
        }

        @Override
        protected void visitVoid(WhileStatement whileStatement) {
            visit(whileStatement.getCondition());
            visit(whileStatement.getBody());
        }
    }
}

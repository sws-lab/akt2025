package week7.ast;

public abstract class AstVisitor<R> {

    protected abstract R visit(Assignment assignment);
    protected abstract R visit(Block block);
    protected abstract R visit(ExpressionStatement expressionStatement);
    protected abstract R visit(FunctionCall functionCall);
    protected abstract R visit(FunctionDefinition functionDefinition);
    protected abstract R visit(IfStatement ifStatement);
    protected abstract R visit(IntegerLiteral integerLiteral);
    protected abstract R visit(ReturnStatement returnStatement);
    protected abstract R visit(StringLiteral stringLiteral);
    protected abstract R visit(Variable variable);
    protected abstract R visit(VariableDeclaration variableDeclaration);
    protected abstract R visit(WhileStatement whileStatement);

    public final R visit(AstNode astNode) {
        return astNode.accept(this);
    }


    /**
     * AstVisitor abiklass juhuks, kui ei soovi midagi tagastada.
     * Üldise AstVisitori korral Java sunnib meid midagi (näiteks null) iga kord tagastama, mis on tüütu.
     */
    public static abstract class VoidVisitor extends AstVisitor<Void> {

        protected abstract void visitVoid(Assignment assignment);
        protected abstract void visitVoid(Block block);
        protected abstract void visitVoid(ExpressionStatement expressionStatement);
        protected abstract void visitVoid(FunctionCall functionCall);
        protected abstract void visitVoid(FunctionDefinition functionDefinition);
        protected abstract void visitVoid(IfStatement ifStatement);
        protected abstract void visitVoid(IntegerLiteral integerLiteral);
        protected abstract void visitVoid(ReturnStatement returnStatement);
        protected abstract void visitVoid(StringLiteral stringLiteral);
        protected abstract void visitVoid(Variable variable);
        protected abstract void visitVoid(VariableDeclaration variableDeclaration);
        protected abstract void visitVoid(WhileStatement whileStatement);

        @Override
        protected final Void visit(Assignment assignment) {
            visitVoid(assignment);
            return null;
        }

        @Override
        protected final Void visit(Block block) {
            visitVoid(block);
            return null;
        }

        @Override
        protected final Void visit(ExpressionStatement expressionStatement) {
            visitVoid(expressionStatement);
            return null;
        }

        @Override
        protected final Void visit(FunctionCall functionCall) {
            visitVoid(functionCall);
            return null;
        }

        @Override
        protected final Void visit(FunctionDefinition functionDefinition) {
            visitVoid(functionDefinition);
            return null;
        }

        @Override
        protected final Void visit(IfStatement ifStatement) {
            visitVoid(ifStatement);
            return null;
        }

        @Override
        protected final Void visit(IntegerLiteral integerLiteral) {
            visitVoid(integerLiteral);
            return null;
        }

        @Override
        protected final Void visit(ReturnStatement returnStatement) {
            visitVoid(returnStatement);
            return null;
        }

        @Override
        protected final Void visit(StringLiteral stringLiteral) {
            visitVoid(stringLiteral);
            return null;
        }

        @Override
        protected final Void visit(Variable variable) {
            visitVoid(variable);
            return null;
        }

        @Override
        protected final Void visit(VariableDeclaration variableDeclaration) {
            visitVoid(variableDeclaration);
            return null;
        }

        @Override
        protected final Void visit(WhileStatement whileStatement) {
            visitVoid(whileStatement);
            return null;
        }
    }
}

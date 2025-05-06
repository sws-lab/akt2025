package week7;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;
import week7.AktkParser.*;
import week7.ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class AktkAst {

    // Ise testimiseks soovitame kasutada selline meetod: inputs/sample.aktk failis sisend muuta.
    // Kui testid sinna kopeerida, siis äkki võtab IDE escape sümbolid ära ja on selgem,
    // milline see kood tegelikult välja näeb.
    public static void main(String[] args) throws IOException {
        String program = Files.readString(Paths.get("inputs", "sample.aktk"));
        AstNode ast = createAst(program);
        System.out.println(ast);
    }

    // Automaattestide jaoks vajalik meetod.
    public static AstNode createAst(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.program();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida:
    private static AstNode parseTreeToAst(ParseTree tree) {
        AktkAstStatementVisitor statementVisitor = new AktkAstStatementVisitor();
        return statementVisitor.visit(tree);
    }

    // Eraldi Expression ja Statement visitoride tegemine võimaldab kasutada eri
    // tagastustüüpe, et ei peaks avaldiste AST-i loomisel pidevalt AstNode'sid
    // Expression'iteks cast'ima

    private static class AktkAstExpressionVisitor extends AktkBaseVisitor<Expression> {
        @Override
        public Expression visitIntegerLiteral(IntegerLiteralContext ctx) {
            int value = Integer.parseInt(ctx.Integer().getText());
            return new IntegerLiteral(value);
        }

        @Override
        public Expression visitStringLiteral(StringLiteralContext ctx) {
            String text = ctx.String().getText();
            // Sõneliteraalil on ümber jutumärgid, mis ei kuulu sõne sisu hulka
            text = text.substring(1, text.length() - 1);
            return new StringLiteral(text);
        }

        @Override
        public Expression visitVariable(VariableContext ctx) {
            return new Variable(ctx.Identifier().getText());
        }

        @Override
        public Expression visitParenthesis(ParenthesisContext ctx) {
            // Sulgudes olev avaldis siin vaja ainult edasi anda,
            // vaikeimplementatsioon ei tööta, sest ümber on lisaks '(' ja ')'
            return visit(ctx.expression());
        }

        @Override
        public Expression visitFunctionCall(FunctionCallContext ctx) {
            String functionName = ctx.Identifier().getText();

            List<Expression> arguments = new ArrayList<>();
            for (ExpressionContext argumentCtx : ctx.expression()) {
                arguments.add(visit(argumentCtx));
            }

            return new FunctionCall(functionName, arguments);
        }

        @Override
        public Expression visitUnaryMinus(UnaryMinusContext ctx) {
            Expression argument = visit(ctx.factorExpression());
            // Operaatorit esitame funktsioonikutsega, vt FunctionCall klassi
            return new FunctionCall("-", argument);
        }

        @Override
        public Expression visitBinaryTerm(BinaryTermContext ctx) {
            return binaryOperation(ctx);
        }

        @Override
        public Expression visitBinarySum(BinarySumContext ctx) {
            return binaryOperation(ctx);
        }

        @Override
        public Expression visitBinaryCompare(BinaryCompareContext ctx) {
            return binaryOperation(ctx);
        }

        private Expression binaryOperation(ParserRuleContext ctx) {
            // Kolme eri taseme binaarsed operatsioonid saab käsitleda ühtmoodi,
            // aga neile ühise üldise Context tüüpi argumendi tõttu peame kasutama getChild
            Expression leftArgument = visit(ctx.getChild(0));
            String operator = ctx.getChild(1).getText();
            Expression rightArgument = visit(ctx.getChild(2));
            // Operaatorit esitame funktsioonikutsega, vt FunctionCall klassi
            return new FunctionCall(operator, leftArgument, rightArgument);
        }
    }

    private static class AktkAstStatementVisitor extends AktkBaseVisitor<Statement> {
        private final AktkAstExpressionVisitor expressionVisitor = new AktkAstExpressionVisitor();

        @Override
        public Statement visitExpression(ExpressionContext ctx) {
            // Kui lause koosneb avaldisest, siis selleks, et temast saaks ikkagi lause,
            // tuleb ta avaldise visitoriga töödelda ja pakendada ExpressionStatement'i sisse
            Expression expression = expressionVisitor.visit(ctx);
            return new ExpressionStatement(expression);
        }

        @Override
        public Statement visitFunctionDefinition(FunctionDefinitionContext ctx) {
            String name = ctx.FunctionName.getText();

            List<FunctionParameter> params = new ArrayList<>();
            for (int i = 0; i < ctx.ParameterName.size(); i++) {
                String parameterName = ctx.ParameterName.get(i).getText();
                String parameterType = ctx.ParameterType.get(i).getText();
                params.add(new FunctionParameter(parameterName, parameterType));
            }

            String returnType = null;
            if (ctx.ReturnType != null) // tagastustüübi puudumisel viskaks getText erindi
                returnType = ctx.ReturnType.getText();

            Block body = (Block) visit(ctx.blockStatement());

            return new FunctionDefinition(name, params, returnType, body);
        }

        @Override
        public Statement visitReturnStatement(ReturnStatementContext ctx) {
            Expression expression = expressionVisitor.visit(ctx.expression());
            return new ReturnStatement(expression);
        }

        @Override
        public Statement visitVariableDeclaration(VariableDeclarationContext ctx) {
            String variableName = ctx.VariableName.getText();

            String type = null;
            if (ctx.VariableType != null) // tüübi puudumisel viskaks getText erindi
                type = ctx.VariableType.getText();

            Expression initializer = null;
            if (ctx.expression() != null) // algväärtusavaldise puudumisel viskaks visit erindi
                initializer = expressionVisitor.visit(ctx.expression());

            return new VariableDeclaration(variableName, type, initializer);
        }

        @Override
        public Statement visitAssignStatement(AssignStatementContext ctx) {
            String variableName = ctx.Identifier().getText();
            Expression expression = expressionVisitor.visit(ctx.expression());
            return new Assignment(variableName, expression);
        }

        /**
         * Ümbritseb lause plokiga, kui see pole juba plokk.
         */
        private static Block ensureBlock(Statement statement) {
            if (statement instanceof Block block)
                return block;
            else
                return new Block(List.of(statement));
        }

        @Override
        public Statement visitWhileStatement(WhileStatementContext ctx) {
            Expression condition = expressionVisitor.visit(ctx.expression());
            Block body = ensureBlock(visit(ctx.statement()));
            return new WhileStatement(condition, body);
        }

        @Override
        public Statement visitIfStatement(IfStatementContext ctx) {
            Expression condition = expressionVisitor.visit(ctx.expression());
            Block thenBranch = ensureBlock(visit(ctx.thenStatement));
            Block elseBranch = ensureBlock(visit(ctx.elseStatement));
            return new IfStatement(condition, thenBranch, elseBranch);
        }

        @Override
        public Statement visitBlockStatement(BlockStatementContext ctx) {
            // visitStatements juba loob Block-i, siin vaja ainult edasi anda,
            // vaikeimplementatsioon ei tööta, sest blockStatement-is on lisaks '{' ja '}'
            return visit(ctx.statements());
        }

        @Override
        public Statement visitStatements(StatementsContext ctx) {
            List<Statement> statements = new ArrayList<>();
            for (StatementContext statementCtx : ctx.statement()) {
                statements.add(visit(statementCtx));
            }
            return new Block(statements);
        }

        @Override
        public Statement visitProgram(ProgramContext ctx) {
            return visit(ctx.statements());
        }
    }
}

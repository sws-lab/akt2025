package toylangs.vhile;

import toylangs.vhile.ast.VhileExpr;
import toylangs.vhile.ast.VhileStmt;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import utils.ExceptionErrorListener;

import java.util.stream.Collectors;

import static toylangs.vhile.ast.VhileNode.*;

public class VhileAst {

    public static VhileStmt makeVhileAst(String input) {
        VhileLexer lexer = new VhileLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        VhileParser parser = new VhileParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static VhileStmt parseTreeToAst(ParseTree tree) {
        VhileVisitor<VhileExpr> exprVisitor = new VhileBaseVisitor<>() {
            @Override
            public VhileExpr visitVar(VhileParser.VarContext ctx) {
                return var(ctx.Var().getText());
            }

            @Override
            public VhileExpr visitNum(VhileParser.NumContext ctx) {
                return num(Integer.parseInt(ctx.Num().getText()));
            }

            @Override
            public VhileExpr visitBinaryCompare(VhileParser.BinaryCompareContext ctx) {
                VhileExpr left = visit(ctx.left);
                VhileExpr right = visit(ctx.right);
                return switch (ctx.op.getText()) {
                    case "==" -> eq(left, right);
                    case "!=" -> neq(left, right);
                    default -> throw new UnsupportedOperationException("unknown op");
                };
            }

            @Override
            public VhileExpr visitBinaryExpr(VhileParser.BinaryExprContext ctx) {
                VhileExpr left = visit(ctx.left);
                VhileExpr right = visit(ctx.right);
                return switch (ctx.op.getText()) {
                    case "+" -> add(left, right);
                    case "*" -> mul(left, right);
                    default -> throw new UnsupportedOperationException("unknown op");
                };
            }

            @Override
            public VhileExpr visitParen(VhileParser.ParenContext ctx) {
                return visit(ctx.compare());
            }
        };

        VhileVisitor<VhileStmt> stmtVisitor = new VhileBaseVisitor<>() {
            @Override
            public VhileStmt visitInit(VhileParser.InitContext ctx) {
                return visit(ctx.stmt());
            }

            @Override
            public VhileStmt visitAssign(VhileParser.AssignContext ctx) {
                return assign(ctx.Var().getText(), exprVisitor.visit(ctx.compare()));
            }

            @Override
            public VhileStmt visitBlock(VhileParser.BlockContext ctx) {
                return block(ctx.stmt().stream().map(this::visit).collect(Collectors.toList()));
            }

            @Override
            public VhileStmt visitWhile(VhileParser.WhileContext ctx) {
                return loop(exprVisitor.visit(ctx.compare()), visit(ctx.stmt()));
            }

            @Override
            public VhileStmt visitEscape(VhileParser.EscapeContext ctx) {
                TerminalNode num = ctx.Num();
                int level = num != null ? Integer.parseInt(num.getText()) : 1;
                return escape(level);
            }
        };

        return stmtVisitor.visit(tree);
    }
}

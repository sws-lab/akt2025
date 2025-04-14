package toylangs.safdi;

import toylangs.safdi.ast.SafdiNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static toylangs.safdi.ast.SafdiNode.*;

public class SafdiAst {

    public static SafdiNode makeSafdiAst(String input) {
        SafdiLexer lexer = new SafdiLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        SafdiParser parser = new SafdiParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static SafdiNode parseTreeToAst(ParseTree tree) {
        return new SafdiBaseVisitor<SafdiNode>() {
            @Override
            public SafdiNode visitInit(SafdiParser.InitContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public SafdiNode visitAdd(SafdiParser.AddContext ctx) {
                return add(visit(ctx.left), visit(ctx.right));
            }

            @Override
            public SafdiNode visitMul(SafdiParser.MulContext ctx) {
                return mul(visit(ctx.left), visit(ctx.right));
            }

            @Override
            public SafdiNode visitDiv(SafdiParser.DivContext ctx) {
                return div(visit(ctx.left), visit(ctx.right),
                        ctx.recover != null ? visit(ctx.recover) : null);
            }

            @Override
            public SafdiNode visitNeg(SafdiParser.NegContext ctx) {
                return neg(visit(ctx.factor()));
            }

            @Override
            public SafdiNode visitNum(SafdiParser.NumContext ctx) {
                return num(Integer.parseInt(ctx.getText()));
            }

            @Override
            public SafdiNode visitVar(SafdiParser.VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public SafdiNode visitParen(SafdiParser.ParenContext ctx) {
                return visit(ctx.expr());
            }
        }.visit(tree);
    }
}

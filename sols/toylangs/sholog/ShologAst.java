package toylangs.sholog;

import toylangs.sholog.ast.ShologNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static toylangs.sholog.ast.ShologNode.*;

public class ShologAst {

    public static ShologNode makeShologAst(String input) {
        ShologLexer lexer = new ShologLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ShologParser parser = new ShologParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static ShologNode parseTreeToAst(ParseTree tree) {
        return new ShologBaseVisitor<ShologNode>() {
            @Override
            public ShologNode visitInit(ShologParser.InitContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public ShologNode visitLit(ShologParser.LitContext ctx) {
                return lit(ctx.getText().equals("T"));
            }

            @Override
            public ShologNode visitVar(ShologParser.VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public ShologNode visitErr(ShologParser.ErrContext ctx) {
                int code = Integer.parseInt(ctx.getText().substring(1)); // eemalda E
                return error(code);
            }

            @Override
            public ShologNode visitParen(ShologParser.ParenContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public ShologNode visitNot(ShologParser.NotContext ctx) {
                return xor(visit(ctx.expr()), lit(true)); // ~x = x + T
            }

            @Override
            public ShologNode visitOp(ShologParser.OpContext ctx) {
                ShologNode left = visit(ctx.expr(0));
                ShologNode right = visit(ctx.expr(1));
                return binary(ctx.op.getText(), left, right);
            }
        }.visit(tree);
    }
}

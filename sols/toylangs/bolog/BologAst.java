package toylangs.bolog;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.bolog.ast.BologNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static toylangs.bolog.ast.BologNode.*;
import static toylangs.bolog.BologParser.*;

public class BologAst {


    public static Set<BologNode> makeBologAst(String sisend) {
        BologLexer lexer = new BologLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        BologParser parser = new BologParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static Set<BologNode> parseTreetoAst(ParseTree tree) {
        Set<BologNode> nodes = new HashSet<>();
        new BologBaseVisitor<BologNode>() {

            @Override
            public BologNode visitProg(ProgContext ctx) {
                for (ExprContext exprContext : ctx.expr()) {
                    nodes.add(visit(exprContext));
                }
                return null;
            }

            @Override
            public BologNode visitTvLit(TvLitContext ctx) {
                return tv(ctx.getText().equals("JAH"));
            }

            @Override
            public BologNode visitVar(VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public BologNode visitBinary(BinaryContext ctx) {
                BologNode left = visit(ctx.expr(0));
                BologNode right = visit(ctx.expr(1));
                switch (ctx.op.getText()) {
                    case "&&": return and(left, right);
                    case "||": return or(left, right);
                    case "<>": return xor(left, right);
                }
                throw new IllegalArgumentException();
            }

            @Override
            public BologNode visitImp(ImpContext ctx) {
                List<BologNode> assumptions = ctx.ass.stream().map(this::visit).collect(Collectors.toList());
                return imp(visit(ctx.con), assumptions);
            }

            @Override
            public BologNode visitUnary(UnaryContext ctx) {
                return not(visit(ctx.expr()));
            }

            @Override
            public BologNode visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }
        }.visit(tree);

        return nodes;
    }


    public static void main(String[] args) throws IOException {
        String input = "X kui Y\nZ && Y";
        Set<BologNode> bologNodes = makeBologAst(input);
        int idx = 1;
        for (BologNode bologNode : bologNodes) {
            bologNode.renderPngFile(Paths.get("graphs", "bolog" + idx++ + ".png"));
            System.out.println(bologNode);
        }
    }



}

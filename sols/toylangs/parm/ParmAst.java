package toylangs.parm;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.parm.ast.ParmNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

import static toylangs.parm.ast.ParmNode.*;
import static toylangs.parm.ParmParser.*;

public class ParmAst {


    public static ParmNode makeParmAst(String sisend) {
        ParmLexer lexer = new ParmLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ParmParser parser = new ParmParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static ParmNode parseTreetoAst(ParseTree tree) {
        return new ParmBaseVisitor<ParmNode>() {

            @Override
            public ParmNode visitInit(InitContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public ParmNode visitLit(LitContext ctx) {
                return lit(Integer.parseInt(ctx.getText()));
            }

            @Override
            public ParmNode visitVar(VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public ParmNode visitBinOp(BinOpContext ctx) {
                ParmNode left = visit(ctx.expr(0));
                ParmNode right = visit(ctx.expr(1));
                switch (ctx.op.getText()) {
                    case "+":
                        return plus(left, right);
                    case "|":
                        return par(left, right);
                    case ";":
                        return seq(left, right);
                }
                throw new IllegalArgumentException();
            }

            @Override
            public ParmNode visitAssign(AssignContext ctx) {
                return up(ctx.Var().getText(), visit(ctx.expr()));
            }

            @Override
            public ParmNode visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }
        }.visit(tree);
    }


    public static void main(String[] args) throws IOException {
        String input = "Kala <- 10; Koer <- Kass <- 5; Kala + Koer";
        ParmNode ast = makeParmAst(input);
        ast.renderPngFile(Paths.get("graphs", "parm.png"));
        System.out.println(ast);
    }

}

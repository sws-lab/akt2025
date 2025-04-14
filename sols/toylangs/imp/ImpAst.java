package toylangs.imp;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.imp.ast.ImpAssign;
import toylangs.imp.ast.ImpNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static toylangs.imp.ast.ImpNode.*;

public class ImpAst {

    public static ImpNode makeImpAst(String input) {
        ImpLexer lexer = new ImpLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ImpParser parser = new ImpParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    private static ImpNode parseTreeToAst(ParseTree tree) {
        return new ImpBaseVisitor<ImpNode>() {
            @Override
            public ImpNode visitInit(ImpParser.InitContext ctx) {
                return visit(ctx.prog());
            }

            @Override
            public ImpNode visitProg(ImpParser.ProgContext ctx) {
                List<ImpAssign> assigns = new ArrayList<>();
                for (ImpParser.AssignContext assignCtx : ctx.assign()) {
                    assigns.add(visitAssign(assignCtx));
                }
                return prog(visit(ctx.expr()), assigns);
            }

            @Override
            public ImpAssign visitAssign(ImpParser.AssignContext ctx) {
                char name = ctx.Ident().getText().charAt(0);
                return assign(name, visit(ctx.expr()));
            }

            @Override
            public ImpNode visitNeg(ImpParser.NegContext ctx) {
                return neg(visit(ctx.expr()));
            }

            @Override
            public ImpNode visitDiv(ImpParser.DivContext ctx) {
                return div(visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public ImpNode visitAdd(ImpParser.AddContext ctx) {
                return add(visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public ImpNode visitVar(ImpParser.VarContext ctx) {
                char name = ctx.Ident().getText().charAt(0);
                return var(name);
            }

            @Override
            public ImpNode visitNum(ImpParser.NumContext ctx) {
                return num(Integer.parseInt(ctx.Int().getText()));
            }

            @Override
            public ImpNode visitParen(ImpParser.ParenContext ctx) {
                return visit(ctx.expr());
            }
        }.visit(tree);
    }

    public static void main(String[] args) throws IOException {
        ImpNode ast = makeImpAst("x = 5, x + 1");
        ast.renderPngFile(Paths.get("graphs", "imp.png"));
        System.out.println(ast);
    }
}

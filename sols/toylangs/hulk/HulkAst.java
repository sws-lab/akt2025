package toylangs.hulk;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.hulk.ast.HulkCond;
import toylangs.hulk.ast.HulkNode;
import toylangs.hulk.ast.HulkStmt;
import toylangs.hulk.ast.expressions.HulkExpr;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static toylangs.hulk.ast.HulkNode.*;
import static toylangs.hulk.HulkParser.*;

public class HulkAst {

    public static HulkNode makeHulkAst(String input) {
        HulkLexer lexer = new HulkLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        HulkParser parser = new HulkParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
//        System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static HulkNode parseTreeToAst(ParseTree tree) {
        HulkVisitor<HulkExpr> exprVisitor = new HulkBaseVisitor<>() {
            // Siin on väike kavalus, et elemendite list on ise ka juba literaal.
            // See võimaldab meil A <- x väga mugavalt käsitleda nagu A := A + {x}
            @Override
            public HulkExpr visitElements(ElementsContext ctx) {
                Set<Character> elements = new HashSet<>();
                ctx.Element().forEach(x -> elements.add(x.getText().charAt(0)));
                return lit(elements);
            }

            @Override
            public HulkExpr visitLit(LitContext ctx) {
                if (ctx.elements() != null) return visit(ctx.elements());
                else return lit();
            }

            @Override
            public HulkExpr visitVar(VarContext ctx) {
                return var(ctx.getText().charAt(0));
            }

            @Override
            public HulkExpr visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public HulkExpr visitBinOp(BinOpContext ctx) {
                HulkExpr left = visit(ctx.expr(0));
                HulkExpr right = visit(ctx.expr(1));
                Character op = ctx.op.getText().charAt(0);
                return binop(op, left, right);
            }
        };

        HulkVisitor<HulkCond> condVisitor = new HulkBaseVisitor<>() {
            @Override
            public HulkCond visitSubset(SubsetContext ctx) {
                HulkExpr subExpr = exprVisitor.visit(ctx.expr(0));
                HulkExpr superExpr = exprVisitor.visit(ctx.expr(1));
                return cond(subExpr, superExpr);
            }

            @Override
            public HulkCond visitIncl(InclContext ctx) {
                HulkExpr subExpr = lit(ctx.Element().getText().charAt(0));  // moodustan yhe-el hulga
                HulkExpr superExpr = exprVisitor.visit(ctx.expr());
                return cond(subExpr, superExpr);
            }
        };

        HulkVisitor<HulkNode> visitor = new HulkBaseVisitor<>() {
            @Override
            public HulkNode visitInit(InitContext ctx) {
                return visit(ctx.program());
            }

            @Override
            public HulkNode visitProgram(ProgramContext ctx) {
                List<HulkStmt> stmts = new ArrayList<>();
                ctx.stmt().forEach(x -> stmts.add(visitStmt(x)));
                return prog(stmts);
            }

            @Override
            public HulkStmt visitStmt(StmtContext ctx) {
                Character name = ctx.Set().getText().charAt(0);
                HulkExpr expr = exprVisitor.visit(ctx.getChild(2)); // TODO: 20.05.21 ilma getChild-ita, tüüpidega keeruline...
                HulkCond cond = null;
                if (ctx.cond() != null) cond = condVisitor.visit(ctx.cond());

                if (ctx.op.getText().equals("<-")) expr = binop('+', var(name), expr);
                else if (ctx.op.getText().equals("->")) expr = binop('-', var(name), expr);

                return stmt(name, expr, cond);
            }

        };

        return visitor.visit(tree);
    }

    public static void main(String[] args) throws IOException {
        HulkNode ln = makeHulkAst("B := {x, y, z}\nA := A + B | {x} subset B");
        System.out.println(ln.toString());
        ln.renderPngFile(Paths.get("graphs", "hulk.png"));
        System.out.println("\nPROGRAMM ISE:");
        System.out.println(ln.prettyPrint());
    }
}

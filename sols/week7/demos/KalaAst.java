package week7.demos;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;
import week6.kalaparser.KalaNode;
import week7.demos.KalaParser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KalaAst {

    public static KalaNode makeKalaAst(String sisend) {
        KalaLexer lexer = new KalaLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        KalaParser parser = new KalaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida
    private static KalaNode parseTreeToAst(ParseTree tree) {
        KalaBaseVisitor<KalaNode> visitor = new KalaBaseVisitor<>() {
            @Override
            public KalaNode visitInit(InitContext ctx) {
                return visit(ctx.list());
            }

            @Override
            public KalaNode visitList(ListContext ctx) {
                if (ctx.elements() != null) return visit(ctx.elements());
                else return KalaNode.mkList();
            }

            @Override
            public KalaNode visitElements(ElementsContext ctx) {
                List<KalaNode> list = new ArrayList<>();
                for (ElementContext element : ctx.element()) list.add(visit(element));
                // Ehk: List<KalaNode> = ctx.element().stream().map(this::visit).collect(Collectors.toList());
                return KalaNode.mkList(list);
            }

            @Override
            public KalaNode visitVariable(VariableContext ctx) {
                return KalaNode.mkIdent(ctx.getText());
            }

            @Override
            public KalaNode visitNull(NullContext ctx) {
                return KalaNode.mkNull();
            }

            @Override
            public KalaNode visitListElement(ListElementContext ctx) {
                return visit(ctx.list());
            }
        };
        return visitor.visit(tree);
    }

    public static void main(String[] args) {
        KalaNode kalaAst = makeKalaAst("(kala, (x,y , null, (), (kala,()) ))");
        System.out.println(kalaAst);  // (kala, (x, y, NULL, (), (kala, ())))

        Map<String, Integer> env = new HashMap<>();
        env.put("kala", 1);
        env.put("x", 2);
        env.put("y", 3);

        System.out.println(kalaAst.sum(env));  // 7
    }
}

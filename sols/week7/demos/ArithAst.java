package week7.demos;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import week6.parsers.Node;

import java.util.HashMap;
import java.util.Map;

import static week7.demos.ArithParser.*;

public class ArithAst {

    private static ParseTree createParseTree(String program, boolean debug) {
        ArithLexer lexer = new ArithLexer(CharStreams.fromString(program));
        ArithParser parser = new ArithParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.expr();
        if (debug) System.out.println(tree.toStringTree(parser));
        return tree;
    }

    private static int eval(ParseTree tree, Map<String, Integer> env) {
        ArithVisitor<Integer> visitor = new ArithBaseVisitor<>() {

            @Override
            public Integer visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public Integer visitVariable(VariableContext ctx) {
                return env.get(ctx.getText());
            }

            @Override
            public Integer visitLiteral(LiteralContext ctx) {
                return Integer.parseInt(ctx.getText());
            }


            // Defineerime abifunktsiooni, et sama struktuuriga reegleid koos käsitleda:
            //   expr -> expr '+' term    #BinaryExpr
            //   expr -> expr '-' term    #BinaryExpr
            //   term -> term '*' factor  #BinaryTerm
            //   term -> term '/' factor  #BinaryTerm
            private Integer handleBinop(ParseTree ctx) {

                // Küsime homogeense liidese kaudu tipude alluvad
                ParseTree leftChild  = ctx.getChild(0);
                String operator      = ctx.getChild(1).getText();
                ParseTree rightChild = ctx.getChild(2);

                // Visit saab eri tüüpi alluvatega hakkama:
                int leftValue  = visit(leftChild);
                int rightValue = visit(rightChild);

                return switch (operator) {
                    case "+" -> leftValue + rightValue;
                    case "-" -> leftValue - rightValue;
                    case "*" -> leftValue * rightValue;
                    case "/" -> leftValue / rightValue;
                    default -> throw new RuntimeException("Tundmatu operaator");
                };
            }

            @Override
            public Integer visitBinaryExpr(BinaryExprContext ctx) {
                return handleBinop(ctx);
            }

            @Override
            public Integer visitBinaryTerm(BinaryTermContext ctx) {
                return handleBinop(ctx);
            }

            // Aga kus on järgmised reeglid?
            //    expr -> term     #SimpleExpr
            //    term -> factor   #SimpleTerm
            // BaseVisitor hoolitseb nende eest. Kui mõni reegel on defineerimata,
            // siis külastatakse alluvad järjest ja tagastatakse viimase tulemus.
            // (Seda käitumist saab aggregateResult ja defaultResult kaudu ka muuta.)

        };
        return visitor.visit(tree);
    }

    private static Node parseTreeToAst(ParseTree tree) {
        ArithVisitor<Node> visitor = new ArithBaseVisitor<>() {

            @Override
            public Node visitTerminal(TerminalNode node) {
                return new Node(node.getText());
            }

            @Override
            public Node visitBinaryExpr(BinaryExprContext ctx) {
                return new Node(ctx.op.getText(), visit(ctx.expr()), visit(ctx.term()));
            }

            @Override
            public Node visitBinaryTerm(BinaryTermContext ctx) {
                return new Node(ctx.op.getText(), visit(ctx.term()), visit(ctx.factor()));
            }

            @Override
            public Node visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }
        };
        return visitor.visit(tree);
    }

    // testide jaoks...
    public static int eval(String expr) {
        return eval(createParseTree(expr, false), new HashMap<>());
    }

    public static Node makeAst(String expr) {
        return parseTreeToAst(createParseTree(expr, false));
    }

    // Ise katsetamiseks:
    public static void main(String[] args) {
        ParseTree parseTree = createParseTree("20 - x * 2 - 1", true);
        HashMap<String, Integer> env = new HashMap<>(); env.put("x", 3);
        System.out.println(eval(parseTree, env));       // 13
        System.out.println(parseTreeToAst(parseTree));  // -(-(20,*(x,2)),1)
    }

}

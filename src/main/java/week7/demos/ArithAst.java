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
        throw new UnsupportedOperationException();
    }

    private static Node parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
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

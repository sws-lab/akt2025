package week7.demos;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;
import week6.kalaparser.KalaNode;
import week7.demos.KalaParser.*;

import java.util.HashMap;
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
        throw new UnsupportedOperationException();
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

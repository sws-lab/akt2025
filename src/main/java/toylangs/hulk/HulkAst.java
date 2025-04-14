package toylangs.hulk;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.hulk.ast.HulkNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

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
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws IOException {
        HulkNode ln = makeHulkAst("B := {x, y, z}\nA := A + B | {x} subset B");
        System.out.println(ln.toString());
        ln.renderPngFile(Paths.get("graphs", "hulk.png"));
        System.out.println("\nPROGRAMM ISE:");
        System.out.println(ln.prettyPrint());
    }
}

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
        throw new UnsupportedOperationException();
    }
}

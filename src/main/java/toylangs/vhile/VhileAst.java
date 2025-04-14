package toylangs.vhile;

import toylangs.vhile.ast.VhileStmt;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static toylangs.vhile.ast.VhileNode.*;

public class VhileAst {

    public static VhileStmt makeVhileAst(String input) {
        VhileLexer lexer = new VhileLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        VhileParser parser = new VhileParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static VhileStmt parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}

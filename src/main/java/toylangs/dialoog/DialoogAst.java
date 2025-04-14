package toylangs.dialoog;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.dialoog.ast.DialoogNode;
import utils.ExceptionErrorListener;

import static toylangs.dialoog.ast.DialoogNode.*;

public class DialoogAst {

    public static DialoogNode makeDialoogAst(String sisend) {
        DialoogLexer lexer = new DialoogLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        DialoogParser parser = new DialoogParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static DialoogNode parseTreetoAst(ParseTree tree) {
        return null;
    }

    public static void main(String[] args) {
        String sisend =
                """
                        x on bool! y on int!
                        Arvuta:\s
                          2 *\s
                          Kas 5 + 5 on 10?\s
                            Jah: Kas 5 + 5 on 10?\s
                                   Jah: 35 * Oota Oota 2 + 2 Valmis - 3 Valmis
                                   Ei: 100\s
                                 Selge\s
                            Ei: 100\s
                          Selge\s
                        + 30 + Kas jah?
                                    Jah: 10
                                    Ei: 300
                               Selge""";

        System.out.println(makeDialoogAst(sisend));
    }

}



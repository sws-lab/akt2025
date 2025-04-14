package toylangs.pullet;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.pullet.ast.PulletNode;
import utils.ExceptionErrorListener;

import static toylangs.pullet.ast.PulletNode.*;
import static toylangs.pullet.PulletParser.*;

public class PulletAst {

    /**
     * Vabrikumeetod avaldise abstraktse süntaksipuu loomiseks sõnest.
     * Jooksutab esmalt ANTLR-i ja kutsub allolev abimeetod, mida tuleks implementeerida.
     * NB! Ära unusta ANTLR-i koodi uuesti genereerida peale grammatika muutmist.
     */
    public static PulletNode makePulletAst(String input) {
        PulletLexer lexer = new PulletLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        PulletParser parser = new PulletParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree konkreetnePuu = parser.init();
        //System.out.println(konkreetnePuu.toStringTree(parser));
        return parseTreeToAst(konkreetnePuu);
    }


    private static PulletNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}

package toylangs.modul;

import toylangs.modul.ast.*;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import utils.ExceptionErrorListener;

import static toylangs.modul.ModulParser.*;
import static toylangs.modul.ast.ModulNode.*;

public class ModulAst {

    public static ModulProg makeModulAst(String input) {
        ModulLexer lexer = new ModulLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ModulParser parser = new ModulParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        return parseTreeToAst(parser.init());
    }

    private static ModulProg parseTreeToAst(InitContext tree) {
        // ParseTree asemel on siin täpsem tüüp: kõik töötab samamoodi! (võid isegi ignoreerida)
        // Saad aga mugvamalt teha tree.prog (sõltuvalt sinu reegli nimest) ja
        // need välised 1-2 juhtu ilma visitorita teha, et visitor saaks tagastada ModulExpr.
        throw new UnsupportedOperationException();
    }
}

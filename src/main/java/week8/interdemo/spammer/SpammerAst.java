package week8.interdemo.spammer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import week8.interdemo.spammer.SpammerParser.EmailContext;
import week8.interdemo.spammer.SpammerParser.InitContext;
import week8.interdemo.spammer.SpammerParser.LineContext;
import week8.interdemo.spammer.SpammerParser.PhoneContext;
import week8.interdemo.spammer.ast.MailCmd;
import week8.interdemo.spammer.ast.RelayCommands;
import week8.interdemo.spammer.ast.SmsCmd;

import java.util.List;
import java.util.Random;

public abstract class SpammerAst {

    public static final String exampleInput = "Vesal Vojdani: vesal.vojdani@spammail.com\nVarmo Vene: +371 666 6666";
    public static final String MSG = "Buy some Vitamin D!";

    public abstract int eval(Random rnd);

    public static ParseTree parse(String sisend) {
        SpammerLexer lexer = new SpammerLexer(CharStreams.fromString(sisend));
        SpammerParser parser = new SpammerParser(new CommonTokenStream(lexer));
        return parser.init();
    }

    // Me võime kohe spämmi välja saata, aga kuidas peaks sellist asja testima?
    private static void sendSpam(String sisend) {
        ParseTree tree = parse(sisend);
        SpammerBaseListener listener = new SpammerBaseListener() {
            @Override
            public void exitEmail(EmailContext ctx) {
                Relay.sendMail(ctx.EMAIL().getText(), ctx.NAME().getText(), MSG);
            }

            @Override
            public void exitPhone(PhoneContext ctx) {
                String numStr = ctx.NUMBER().getText();
                long number = Long.parseLong(numStr.replaceAll("[+ -]", ""));
                Relay.sendSMS(number, ctx.NAME().getText(), MSG);
            }
        };
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
    }

    // Genereerime selle asemel käskude jada (lihtne AST!), mida saab pärast interpreteerida.
    // Aga lisaks saab ka väga hästi testida, kas genereeritud käsud on õiged!
    public static List<RelayCommands> createAst(String sisend) {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("*** DIRECT ***");
        sendSpam(exampleInput);
        System.out.println("*** INTERPRETER ***");
        Relay.interpret(createAst(exampleInput));
    }

}

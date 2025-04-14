package week8.interdemo.spammer;

import org.junit.Test;
import week8.interdemo.spammer.ast.MailCmd;
import week8.interdemo.spammer.ast.RelayCommands;
import week8.interdemo.spammer.ast.SmsCmd;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static week8.interdemo.spammer.SpammerAst.*;

public class SpammerAstTest {

    @Test
    public void testAst() {
        List<RelayCommands> ast = createAst(exampleInput);
        RelayCommands cmd0 = new MailCmd("vesal.vojdani@spammail.com", "Vesal Vojdani", MSG);
        RelayCommands cmd1 = new SmsCmd(3716666666L, "Varmo Vene", MSG);
        assertEquals(cmd0, ast.get(0));
        assertEquals(cmd1, ast.get(1));
    }
}

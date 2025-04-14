package week8.interdemo.spammer;

import week8.interdemo.spammer.ast.RelayCommands;

import java.util.List;

public class Relay {

    public static void sendMail(String address, String name, String msg) {
        System.out.println("Sending email to " + address);
        System.out.println("Dear " + name + ",");
        System.out.println(msg);
        System.out.println("END OF MESSAGE");
    }

    public static void sendSMS(long number, String name, String msg) {
        System.out.println("Sending SMS to " + number);
        System.out.println(name + ", " + msg);
        System.out.println("END OF MESSAGE");
    }

    public static void interpret(List<RelayCommands> commands) {
        for (RelayCommands cmd : commands) cmd.send();
    }
}

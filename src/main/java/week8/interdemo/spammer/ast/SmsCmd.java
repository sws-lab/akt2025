package week8.interdemo.spammer.ast;

import week8.interdemo.spammer.Relay;

import java.util.Objects;

public class SmsCmd extends RelayCommands {

    private final long number;

    public SmsCmd(long number, String name, String msg) {
        super(msg, name);
        this.number = number;
    }

    @Override
    public void send() {
        Relay.sendSMS(number, name, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmsCmd smsCmd = (SmsCmd) o;
        return number == smsCmd.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number);
    }

    @Override
    public String toString() {
        return "SmsCmd{" +
                "number=" + number +
                ", msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

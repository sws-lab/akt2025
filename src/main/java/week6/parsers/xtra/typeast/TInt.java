package week6.parsers.xtra.typeast;

public class TInt extends Type {

    public TInt() {
        super("TInt");
    }

    @Override
    protected String toSyntax(String rest) {
        return "int " + rest;
    }

    @Override
    protected String toEnglish(boolean singular) {
        if (singular) return "an integer";
        else return "integers";
    }
}

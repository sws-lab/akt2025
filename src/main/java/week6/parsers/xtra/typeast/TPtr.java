package week6.parsers.xtra.typeast;

public class TPtr extends Type {
    private final Type target;

    public TPtr(Type target) {
        super("TPtr");
        this.add(target);
        this.target = target;
    }

    @Override
    protected String toSyntax(String rest) {
        return target.toSyntax("*(" + rest + ")");
    }

    @Override
    protected String toEnglish(boolean singular) {
        if (singular)
            return "a pointer to " + target.toEnglish(true);
        else
            return "pointers to " + target.toEnglish(false);
    }
}

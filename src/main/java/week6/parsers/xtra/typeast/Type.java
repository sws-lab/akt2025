package week6.parsers.xtra.typeast;

import week6.parsers.Node;

public abstract class Type extends Node {

    public Type(String label) {
        super(label);
    }

    protected abstract String toSyntax(String rest);

    protected abstract String toEnglish(boolean singular);

    public String toEnglish() {
        return toEnglish(true);
    }

    public String toSyntax() {
        return toSyntax("");
    }
}

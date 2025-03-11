package week4.baselangs.bool.ast;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public abstract class BoolNode {
    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static BoolNode var(char b) {
        return new BoolVar(b);
    }

    public static BoolNode imp(BoolNode e1, BoolNode e2) {
        return new BoolImp(e1, e2);
    }

    public static BoolNode or(BoolNode e1, BoolNode e2) {
        return new BoolOr(e1, e2);
    }

    public static BoolNode not(BoolNode e) {
        return new BoolNot(e);
    }

    private final List<BoolNode> children;

    public BoolNode(BoolNode... children) {
        this.children = Arrays.asList(children);
    }

    public List<BoolNode> getChildren() {
        return children;
    }

    public BoolNode getChild(int i) {
        return children.get(i);
    }


    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(BoolVisitor<T> visitor);

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", getClass().getSimpleName().toLowerCase() + "(", ")");
        for (BoolNode child : children) joiner.add(child.toString());
        return joiner.toString();
    }

    public static void main(String[] args) {
        System.out.println(not(or(var('A'), var('B'))));
    }

}

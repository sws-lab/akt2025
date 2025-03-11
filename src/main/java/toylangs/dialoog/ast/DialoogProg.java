package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class DialoogProg extends DialoogNode {

    private final List<DialoogDecl> decls;
    private final DialoogNode expression;

    public DialoogProg(List<DialoogDecl> decls, DialoogNode expression) {
        this.decls = decls;
        this.expression = expression;
    }

    public DialoogNode getExpression() {
        return expression;
    }

    public List<DialoogDecl> getDecls() {
            return decls;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(listNode(decls, "decls"), expression);
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}

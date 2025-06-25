package eksam3.ast;

import java.util.List;

public class SimpProg extends SimpNode {
    private final List<SimpNode> expressions;

    public SimpProg(List<SimpNode> expressions) {
        this.expressions = expressions;
    }

    public List<SimpNode> getExpressions() {
        return expressions;
    }

    @Override
    protected List<SimpNode> getAbstractNodeList() {
        return expressions;
    }

    @Override
    public <T> T accept(SimpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

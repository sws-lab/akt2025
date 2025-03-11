package toylangs.sholog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ShologError extends ShologNode {
    private final int code;

    public ShologError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(code));
    }

    @Override
    public <T> T accept(ShologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

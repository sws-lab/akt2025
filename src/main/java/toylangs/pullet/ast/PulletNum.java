package toylangs.pullet.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

/**
 * Täisarv.
 */
public class PulletNum extends PulletNode {
    private final int num;

    public PulletNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(num));
    }

    @Override
    public <T> T accept(PulletAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

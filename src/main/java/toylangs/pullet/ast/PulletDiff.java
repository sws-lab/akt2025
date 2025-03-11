package toylangs.pullet.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

/**
 * Lahutamine.
 */
public class PulletDiff extends PulletNode {
    private final PulletNode left;
    private final PulletNode right;

    public PulletDiff(PulletNode left, PulletNode right) {
        this.left = left;
        this.right = right;
    }


    public PulletNode getLeft() {
        return left;
    }

    public PulletNode getRight() {
        return right;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right);
    }

    @Override
    public <T> T accept(PulletAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

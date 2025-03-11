package toylangs.pullet.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

/**
 * Vaba muutuja, mis saab võtta täisarvulisi väärtuseid.
 */
public class PulletVar extends PulletNode {
    private final String name;

    public PulletVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(name));
    }

    @Override
    public <T> T accept(PulletAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

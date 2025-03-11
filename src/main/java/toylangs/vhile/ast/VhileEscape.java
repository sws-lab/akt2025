package toylangs.vhile.ast;

import com.google.common.base.Preconditions;
import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class VhileEscape extends VhileStmt {
    private final int level;

    public VhileEscape(int level) {
        Preconditions.checkArgument(level > 0, "level is not positive: %s", level);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(level));
    }

    @Override
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

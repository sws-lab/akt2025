package toylangs.modul.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ModulVar extends ModulExpr {
    private final String name;

    public ModulVar(String name) {
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
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ImpDiv extends ImpNode {
    private final ImpNode numerator;
    private final ImpNode denominator;

    public ImpDiv(ImpNode numerator, ImpNode denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public ImpNode getNumerator() {
        return numerator;
    }

    public ImpNode getDenominator() {
        return denominator;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(numerator, denominator);
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

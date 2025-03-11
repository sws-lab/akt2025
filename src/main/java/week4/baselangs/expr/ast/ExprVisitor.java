package week4.baselangs.expr.ast;

public abstract class ExprVisitor<T> {

    protected abstract T visit(ExprNum num);
    protected abstract T visit(ExprNeg neg);
    protected abstract T visit(ExprDiv div);
    protected abstract T visit(ExprAdd add);

    public T visit(ExprNode node) {
        return node.accept(this);
    }


    public static class BaseVisitor<T> extends ExprVisitor<T> {

        @Override
        protected T visit(ExprNum num) {
            return null;
        }

        @Override
        protected T visit(ExprNeg neg) {
            return visit(neg.getExpr());
        }

        @Override
        protected T visit(ExprDiv div) {
            return aggregateResult(visit(div.getNumerator()), visit(div.getDenominator()));
        }

        @Override
        protected T visit(ExprAdd add) {
            return aggregateResult(visit(add.getLeft()), visit(add.getRight()));
        }

        protected T aggregateResult(T aggregate, T nextResult) {
            return nextResult;
        }
    }
}

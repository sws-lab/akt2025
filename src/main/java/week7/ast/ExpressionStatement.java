package week7.ast;

import java.util.Collections;
import java.util.List;

/**
 * Avaldislause.
 *
 * Kasutatakse eeldatavasti väärtustamisel kõrvalefektiga avaldiste jaoks, mille väärtus pole oluline.
 * Näiteks 'print("tere")'.
 */
public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public List<Object> getChildren() {
        return Collections.singletonList(expression);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

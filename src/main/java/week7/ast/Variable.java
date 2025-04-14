package week7.ast;

import java.util.Collections;
import java.util.List;

/**
 * Muutujaviide.
 *
 * Kasutatakse ainult avaldistes, aga mitte omistuslausete vasakutes pooltes.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Sidumine viidatava muutujaga.
     * Vajalik alles 9. kodutöös!
     */
    private VariableBinding binding = null;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableBinding getBinding() {
        return binding;
    }

    public void setBinding(VariableBinding binding) {
        this.binding = binding;
    }

    @Override
    public List<Object> getChildren() {
        return Collections.singletonList(name);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

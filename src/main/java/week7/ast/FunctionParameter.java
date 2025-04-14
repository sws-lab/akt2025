package week7.ast;

import java.util.Arrays;
import java.util.List;

/**
 * Funktsiooni parameeter funktiooni definitsioonis.
 *
 * @see FunctionDefinition
 */
public class FunctionParameter extends AstNode implements VariableBinding {
    private final String parameterName;
    private final String type;

    public FunctionParameter(String parameterName, String type) {
        this.parameterName = parameterName;
        this.type = type;
    }

    @Override
    public String getVariableName() {
        return parameterName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<Object> getChildren() {
        return Arrays.asList(parameterName, type);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        throw new UnsupportedOperationException("AstVisitor ei võimalda FunctionParameter-it käsitleda: ära kutsu FunctionParameter-i peal visit, vaid kasuta seda otse");
    }
}

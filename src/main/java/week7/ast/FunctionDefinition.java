package week7.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Funktsiooni definitsioon.
 */
public class FunctionDefinition extends Statement {

    private final String name;
    private final List<FunctionParameter> params;
    /**
     * Tagastustüüp. Puudumise korral {@code null}.
     */
    private final String returnType;
    private final Block body;

    public FunctionDefinition(String name, List<FunctionParameter> params, String returnType, Block body) {
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public FunctionDefinition(String name, List<FunctionParameter> params, Block body) {
        this(name, params, null, body);
    }

    public String getName() {
        return name;
    }

    public List<FunctionParameter> getParameters() {
        return Collections.unmodifiableList(params);
    }

    public String getReturnType() {
        return returnType;
    }

    public Block getBody() {
        return body;
    }

    @Override
    public List<Object> getChildren() {
        ArrayList<Object> children = new ArrayList<>();
        children.add(name);
        children.addAll(params);
        children.add(returnType);
        children.add(body);
        return children;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

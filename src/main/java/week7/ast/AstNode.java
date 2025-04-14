package week7.ast;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * AKTK abstraktse süntaksipuu tippude ülemklass.
 */
public abstract class AstNode {

    /**
     * Tagastab kõik tipu alamad.
     * Lisaks teistele AstNode'idele võib sisaldada ka muud tüüpi väärtusi ja {@code null}-e.
     */
    public abstract List<Object> getChildren();

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", getSimpleName() + "(", ")");
        for (Object child : getChildren()) {
            String childString;
            if (child instanceof String s)
                childString = "\"" + StringEscapeUtils.escapeJava(s) + "\"";
            else
                childString = Objects.toString(child); // child may be null
            joiner.add(childString);
        }
        return joiner.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        AstNode that = (AstNode) obj;
        return Objects.equals(this.getChildren(), that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSimpleName(), getChildren());
    }

    String getSimpleName() {
        return getClass().getSimpleName();
    }

    public abstract <R> R accept(AstVisitor<R> visitor);
}

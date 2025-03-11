package toylangs;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class AbstractNode {

    /**
     * Homogeenne esitsus alluvatest, et arvutada equals, hashmap ja toString.
     */
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return emptyList();
    }

    /**
     * Kasutame equals ja hashmap jaoks. Vaikimisi klassi suffix.
     * Tuleb eelkõige lehtedes üle defineerida vastava väärtusega.
     */
    protected Object getNodeInfo() {
        String className = this.getClass().getSimpleName();
        return className.replaceFirst("[A-Z][a-z]*", "").toLowerCase();
    }

    /**
     * Kasutame toString meetodi genereerimiseks.
     * Enamasti langeb kokku klassi info-objektiga,
     * aga mõnikord vajame jutumärke ümber.
     */
    protected String getNodeLabel() {
        return getNodeInfo().toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        AbstractNode that = (AbstractNode) obj;
        return Objects.equals(this.getNodeInfo(), that.getNodeInfo()) &&
                Objects.equals(this.getAbstractNodeList(), that.getAbstractNodeList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeInfo(), getAbstractNodeList());
    }

    // Alamklassid pigem buildString üle defineerida.
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        buildString(sb);
        return sb.toString();
    }

    // Override with true to output empty vararg constructors.
    protected boolean canHaveEmptyChildList() {
        return false;
    }

    protected void buildString(StringBuilder sb) {
        sb.append(getNodeLabel());
        Iterator<? extends AbstractNode> childIterator = getAbstractNodeList().iterator();
        if (childIterator.hasNext()) {
            sb.append("(");
            childIterator.next().buildString(sb);
            while (childIterator.hasNext()) {
                sb.append(", ");
                AbstractNode next = childIterator.next();
                if (next == null) sb.append("null");
                else next.buildString(sb);
            }
            sb.append(")");
        } else if (canHaveEmptyChildList()) sb.append("()");
    }

    public final void renderPngFile(Path path) throws IOException {
        AbstractNodeVisualizer.renderPngFile(this, path);
    }

    protected static AbstractNode dataNode(Object data, String quote) {
        return new AbstractNode() {
            @Override
            protected Object getNodeInfo() {
                return data;
            }

            @Override
            protected String getNodeLabel() {
                return quote + data + quote;
            }
        };
    }

    protected static AbstractNode dataNode(Character data) {
        return dataNode(data, "'");
    }

    protected static AbstractNode dataNode(String data) {
        return dataNode(data, "\"");
    }

    protected static AbstractNode dataNode(AbstractNode value) {
        throw new IllegalArgumentException("This is almost certainly a bug!");
    }

    protected static AbstractNode dataNode(Object data) {
        return dataNode(data, "");
    }

    protected static AbstractNode listNode(List<? extends AbstractNode> list, String prefix) {
        return new AbstractNode() {
            @Override
            protected List<? extends AbstractNode> getAbstractNodeList() {
                return list;
            }

            @Override
            protected Object getNodeInfo() {
                return prefix;
            }

            @Override
            protected boolean canHaveEmptyChildList() {
                return true;
            }
        };
    }

    protected static List<AbstractNode> concat(
            List<? extends AbstractNode> list1, List<? extends AbstractNode> list2) {
        return Lists.newArrayList(Iterables.concat(list1, list2));
    }

    protected static List<AbstractNode> cons(
            AbstractNode node, List<? extends AbstractNode> list) {
        return concat(singletonList(node), list);
    }

    protected static List<AbstractNode> snoc(
            List<? extends AbstractNode> list, AbstractNode node) {
        return concat(list, singletonList(node));
    }
}

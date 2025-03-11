package toylangs.pullet.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

/**
 * PulletBinding seob muutujatele väärtuse, mis kehtib avaldise kehas.
 * Muutuja sidumiseks tuleb anda sellele väärtuseks samuti mingi avaldis.
 * <p>
 * NB! Juba seotud muutuja uuesti sidumine kehtib ainult let-sidumise keha piires.
 */
public class PulletBinding extends PulletNode {
    private final String name;
    private final PulletNode value;
    private final PulletNode body;

    public PulletBinding(String name, PulletNode value, PulletNode body) {
        this.name = name;
        this.value = value;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public PulletNode getValue() {
        return value;
    }

    public PulletNode getBody() {
        return body;
    }

    @Override
    protected Object getNodeInfo() {
        return "let";
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(name), value, body);
    }

    @Override
    public <T> T accept(PulletAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

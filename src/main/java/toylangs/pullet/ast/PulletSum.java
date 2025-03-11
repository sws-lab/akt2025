package toylangs.pullet.ast;


import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

/**
 * Avaldis, mis seisneb mingi teise avaldise mingite väärtuste omavahelises liitmises.
 * PulletSum seob enda kehas ÜHE muutuja, mille väärtus on igal korral erinev.
 * Tsüklimuutuja algväärtuseks on avaldise lo väärtus, ning seda hakatakse suurendama
 * ühe kaupa, kuni tema väärtus on väiksem kui avaldise hi väärtus.
 * <p>
 * NB! Avaldise hi väärtus on välja arvatud ega saa kunagi tsüklimuutuja väärtuseks.
 */
public class PulletSum extends PulletNode {
    private final String name;
    private final PulletNode lo;
    private final PulletNode hi;
    private final PulletNode body;

    public PulletSum(String name, PulletNode lo, PulletNode hi, PulletNode body) {
        this.name = name;
        this.lo = lo;
        this.hi = hi;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public PulletNode getLo() {
        return lo;
    }

    public PulletNode getHi() {
        return hi;
    }

    public PulletNode getBody() {
        return body;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(name), lo, hi, body);
    }

    @Override
    public <T> T accept(PulletAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

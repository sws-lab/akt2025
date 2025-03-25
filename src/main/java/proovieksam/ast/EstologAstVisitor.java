package proovieksam.ast;

import proovieksam.ast.aatomid.EstologLiteraal;
import proovieksam.ast.aatomid.EstologMuutuja;
import proovieksam.ast.operaatorid.EstologJa;
import proovieksam.ast.operaatorid.EstologVoi;
import proovieksam.ast.operaatorid.EstologVordus;

public abstract class EstologAstVisitor<T> {

    public abstract T visit(EstologLiteraal literaal);
    public abstract T visit(EstologMuutuja muutuja);
    public abstract T visit(EstologJa ja);
    public abstract T visit(EstologVoi voi);
    public abstract T visit(EstologVordus vordus);
    public abstract T visit(EstologKui kui);

    public abstract T visit(EstologDef def);
    public abstract T visit(EstologProg prog);

    public T visit(EstologNode node) {
        return node.accept(this);
    }
}

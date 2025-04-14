package toylangs.dialoog.ast;

import cma.instruction.CMaBasicInstruction;
import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DialoogBinary extends DialoogNode {

    public enum BinOp {
        DialoogEq("on", Object::equals),

        DialoogAnd("&", (x,y) -> (boolean)x && (boolean)y),
        DialoogOr("|", (x,y) -> (boolean)x || (boolean)y),

        DialoogAdd("+", (x,y) -> (int)x + (int)y),
        DialoogSub("-", (x,y) -> (int)x - (int)y),
        DialoogMul("*", (x,y) -> (int)x * (int)y),
        DialoogDiv("/", (x,y) -> (int)x / (int)y);

        private final String symb;
        private final BinaryOperator<Object> javaOp;

        BinOp(String symb, BinaryOperator<Object> javaOp) {
            this.symb = symb;
            this.javaOp = javaOp;
        }

        public String getSymb() {
            return symb;
        }

        public BinaryOperator<Object> toJava() {
            return javaOp;
        }

        public CMaBasicInstruction.Code toCMa() {
            return CMaBasicInstruction.Code.valueOf(toString().substring(7).toUpperCase());
        }

        private final static Map<String, BinOp> symbolMap =
                Arrays.stream(BinOp.values()).collect(toMap(BinOp::getSymb, identity()));

        public static BinOp fromSymb(String symb) {
            return symbolMap.get(symb);
        }

    }

    private final BinOp op;
    private final DialoogNode leftExpr;
    private final DialoogNode rightExpr;

    public DialoogBinary(BinOp op, DialoogNode leftExpr, DialoogNode rightExpr) {
        this.op = op;
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public DialoogBinary(String symb, DialoogNode leftExpr, DialoogNode rightExpr) {
        this(BinOp.fromSymb(symb), leftExpr, rightExpr);
    }

    public BinOp getOp() {
        return op;
    }

    public DialoogNode getLeftExpr() {
        return leftExpr;
    }

    public DialoogNode getRightExpr() {
        return rightExpr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(leftExpr, rightExpr);
    }

    @Override
    protected Object getNodeInfo() {
        return op.toString().substring(7).toLowerCase();
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

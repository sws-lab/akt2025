package cma.instruction;

public abstract class CMaInstruction<Code> {

    protected final Code code;

    protected CMaInstruction(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public abstract void accept(CMaInstructionVisitor visitor);

    @Override
    public abstract String toString();
}

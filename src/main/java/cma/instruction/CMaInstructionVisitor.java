package cma.instruction;

public abstract class CMaInstructionVisitor {

    protected abstract void visit(CMaBasicInstruction basicInstruction);
    protected abstract void visit(CMaIntInstruction intInstruction);
    protected abstract void visit(CMaLabelInstruction labelInstruction);

    public void visit(CMaInstruction<?> instruction) {
        instruction.accept(this);
    }
}

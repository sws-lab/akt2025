package cma.instruction;

public class CMaIntInstruction extends CMaInstruction<CMaIntInstruction.Code> {

    public enum Code {
        //@formatter:off
        /** lisa stackile konstant */
        LOADC,

        /** loe väärtus indeksilt */
        LOADA,

        /** salvesta väärtus indeksile */
        STOREA,
        //@formatter:on
    }

    private final int arg;

    public CMaIntInstruction(Code code, int arg) {
        super(code);
        this.arg = arg;
    }

    public int getArg() {
        return arg;
    }

    @Override
    public void accept(CMaInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return code.name() + " " + arg;
    }
}

package cma.instruction;

import cma.CMaLabel;

public class CMaLabelInstruction extends CMaInstruction<CMaLabelInstruction.Code> {

    public enum Code {
        //@formatter:off
        /** hüppa labelile */
        JUMP,

        /** hüppa labelile kui stackipealne väärtus on 0 */
        JUMPZ,
        //@formatter:on
    }

    private final CMaLabel label;

    public CMaLabelInstruction(Code code, CMaLabel label) {
        super(code);
        this.label = label;
    }

    public CMaLabel getLabel() {
        return label;
    }

    @Override
    public void accept(CMaInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return code.name() + " " + label;
    }
}

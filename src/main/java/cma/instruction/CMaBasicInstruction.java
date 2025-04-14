package cma.instruction;

public class CMaBasicInstruction extends CMaInstruction<CMaBasicInstruction.Code> {

    public enum Code {
        //@formatter:off
        /** binaarne + */ ADD,
        /** binaarne - */ SUB,
        /** binaarne * */ MUL,
        /** binaarne / */ DIV,
        /** binaarne % */ MOD,
        /** unaarne -  */ NEG,

        /** binaarne & */ AND,
        /** binaarne | */ OR,
        /** binaarne ^ */ XOR,
        /** unaarne !  */ NOT,

        /** binaarne == */ EQ,
        /** binaarne != */ NEQ,
        /** binaarne <  */ LE,
        /** binaarne <= */ LEQ,
        /** binaarne >  */ GE, GR,
        /** binaarne >= */ GEQ,

        /** eemalda stackipealne väärtus */
        POP,

        /** duubelda stackipealne väärtus */
        DUP,

        /** lae väärtus stackipealselt indeksilt */
        LOAD,

        /** salvesta väärtus stackipealsele indeksile */
        STORE,

        /** seiska programm */
        HALT,
        //@formatter:on
    }

    public CMaBasicInstruction(Code code) {
        super(code);
    }

    @Override
    public void accept(CMaInstructionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return code.name();
    }
}

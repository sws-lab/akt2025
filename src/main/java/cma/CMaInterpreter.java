package cma;

import cma.instruction.*;

import static cma.instruction.CMaBasicInstruction.Code.LOAD;
import static cma.instruction.CMaBasicInstruction.Code.STORE;
import static cma.instruction.CMaIntInstruction.Code.LOADC;

public class CMaInterpreter {

    private final CMaProgram program;
    private int pc = 0;
    private final CMaStack stack;
    private final CMaInstructionVisitor instructionExecuteVisitor = new InstructionExecuteVisitor();

    private CMaInterpreter(CMaProgram program, CMaStack initialStack) {
        this.program = program;
        this.stack = new CMaStack(initialStack);
    }

    public static CMaStack run(CMaProgram program) {
        return run(program, new CMaStack());
    }

    public static CMaStack run(CMaProgram program, CMaStack initialStack) {
        CMaInterpreter interpreter = new CMaInterpreter(program, initialStack);
        return interpreter.execute();
    }

    private CMaStack execute() {
        while (0 <= pc && pc < program.getInstructions().size()) {
            CMaInstruction<?> instruction = program.getInstructions().get(pc);
            pc++;
            execute(instruction);
        }
        return stack;
    }

    private void execute(CMaInstruction<?> instruction) {
        instructionExecuteVisitor.visit(instruction);
    }

    private class InstructionExecuteVisitor extends CMaInstructionVisitor {
        @Override
        protected void visit(CMaBasicInstruction basicInstruction) {
            int arg, lhs, rhs;
            switch (basicInstruction.getCode()) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case MOD:
                case AND:
                case OR:
                case XOR:
                case EQ:
                case NEQ:
                case LE:
                case LEQ:
                case GE:
                case GR:
                case GEQ:
                    rhs = stack.pop();
                    lhs = stack.pop();
                    switch (basicInstruction.getCode()) {
                        case ADD -> stack.push(lhs + rhs);
                        case SUB -> stack.push(lhs - rhs);
                        case MUL -> stack.push(lhs * rhs);
                        case DIV -> stack.push(lhs / rhs);
                        case MOD -> stack.push(lhs % rhs);
                        case AND -> stack.push(lhs & rhs);
                        case OR -> stack.push(lhs | rhs);
                        case XOR -> stack.push(lhs ^ rhs);
                        case EQ -> stack.push(CMaUtils.bool2int(lhs == rhs));
                        case NEQ -> stack.push(CMaUtils.bool2int(lhs != rhs));
                        case LE -> stack.push(CMaUtils.bool2int(lhs < rhs));
                        case LEQ -> stack.push(CMaUtils.bool2int(lhs <= rhs));
                        case GE, GR -> stack.push(CMaUtils.bool2int(lhs > rhs));
                        case GEQ -> stack.push(CMaUtils.bool2int(lhs >= rhs));
                    }
                    break;
                case NEG:
                    arg = stack.pop();
                    stack.push(-arg);
                    break;
                case NOT:
                    arg = stack.pop();
                    stack.push(CMaUtils.bool2int(!CMaUtils.int2bool(arg)));
                    break;
                case POP:
                    stack.pop();
                    break;
                case DUP:
                    stack.push(stack.peek());
                    break;
                case LOAD:
                    arg = stack.pop();
                    stack.push(stack.get(arg));
                    break;
                case STORE:
                    arg = stack.pop();
                    stack.set(arg, stack.peek());
                    break;
                case HALT:
                    pc = -1; // out of range pc halts
                    break;
            }

        }

        @Override
        protected void visit(CMaIntInstruction intInstruction) {
            switch (intInstruction.getCode()) {
                case LOADC:
                    stack.push(intInstruction.getArg());
                    break;
                case LOADA:
                    visit(new CMaIntInstruction(LOADC, intInstruction.getArg()));
                    visit(new CMaBasicInstruction(LOAD));
                    break;
                case STOREA:
                    visit(new CMaIntInstruction(LOADC, intInstruction.getArg()));
                    visit(new CMaBasicInstruction(STORE));
                    break;
            }
        }

        @Override
        protected void visit(CMaLabelInstruction labelInstruction) {
            switch (labelInstruction.getCode()) {
                case JUMP:
                    pc = getLabelTarget(labelInstruction.getLabel());
                    break;
                case JUMPZ:
                    if (!CMaUtils.int2bool(stack.pop()))
                        pc = getLabelTarget(labelInstruction.getLabel());
                    break;
            }
        }

        private int getLabelTarget(CMaLabel label) {
            Integer target = program.getLabels().get(label);
            if (target != null)
                return target;
            else
                throw new CMaException("label '%s' not placed".formatted(label));
        }
    }
}

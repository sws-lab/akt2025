package cma;

import cma.instruction.CMaInstruction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CMaProgram {

    private final List<CMaInstruction<?>> instructions;
    private final Map<CMaLabel, Integer> labels;

    public CMaProgram(List<CMaInstruction<?>> instructions, Map<CMaLabel, Integer> labels) {
        this.instructions = instructions;
        this.labels = labels;
    }

    public List<CMaInstruction<?>> getInstructions() {
        return instructions;
    }

    public Map<CMaLabel, Integer> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        Set<Map.Entry<CMaLabel, Integer>> remainingLabelEntries = new HashMap<>(labels).entrySet();
        StringJoiner joiner = new StringJoiner("\n");

        for (int i = 0; i < instructions.size(); i++) {
            CMaInstruction<?> instruction = instructions.get(i);
            StringBuilder builder = new StringBuilder();
            for (Iterator<Map.Entry<CMaLabel, Integer>> iterator = remainingLabelEntries.iterator(); iterator.hasNext(); ) {
                Map.Entry<CMaLabel, Integer> labelEntry = iterator.next();
                if (labelEntry.getValue() == i) {
                    builder.append(labelEntry.getKey()).append(": ");
                    iterator.remove();
                }
            }
            builder.append(instruction.toString());
            joiner.add(builder.toString());
        }

        if (!remainingLabelEntries.isEmpty()) {
            // print remaining (out of bounds) labels at end, Vam can handle
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<CMaLabel, Integer> labelEntry : remainingLabelEntries) {
                builder.append(labelEntry.getKey()).append(": ");
            }
            joiner.add(builder.toString());
        }

        return joiner.toString();
    }

    public CMaProgram append(CMaProgram other) {
        List<CMaInstruction<?>> instructions = new ArrayList<>();
        instructions.addAll(this.instructions);
        instructions.addAll(other.instructions);

        Map<CMaLabel, Integer> labels = new HashMap<>();
        labels.putAll(this.labels);
        labels.putAll(other.labels.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() + this.instructions.size())));

        return new CMaProgram(instructions, labels);
    }

    public String toString(CMaStack initialStack) {
        return initialStack.toLoadProgram().append(this).toString();
    }

    public void toFile(Path path, CMaStack initialStack) throws IOException {
        Files.createDirectories(path.toAbsolutePath().getParent());
        Files.writeString(path, toString(initialStack));
    }
}

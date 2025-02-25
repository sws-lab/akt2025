package week3;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.StatePair;
import dk.brics.automaton.Transition;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.AlphanumComparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class JFlapEquivalenceTester {

    private static final Path EXPECTED_ROOT = Paths.get("src", "test", "jflap");
    private static final Path ACTUAL_ROOT = Paths.get("src", "main", "jflap");

    private static final Pattern TEST_PATTERN = Pattern.compile("^test(.*)\\.jff$");
    private static final Comparator<Path> COMPARATOR = Comparator.comparing(Path::toString, new AlphanumComparator());

    @Parameters(name="{0}")
    public static Iterable<Path> data() throws IOException {
        return Files.walk(EXPECTED_ROOT)
                .filter(Files::isRegularFile)
                .filter(path -> TEST_PATTERN.matcher(path.getFileName().toString()).matches())
                .map(EXPECTED_ROOT::relativize)
                .sorted(COMPARATOR)
                .collect(Collectors.toList());
    }

    private final Path expectedRelativePath;

    public JFlapEquivalenceTester(Path expectedRelativePath) {
        this.expectedRelativePath = expectedRelativePath;
    }

    @Test
    public void testEquivalent() {
        Path expectedPath = EXPECTED_ROOT.resolve(expectedRelativePath);
        String expectedFilename = expectedPath.getFileName().toString();
        String actualFilename = TEST_PATTERN.matcher(expectedFilename).replaceFirst("yl$1.jff");
        Path actualRelativePath = expectedRelativePath.resolveSibling(actualFilename);
        Path actualPath = ACTUAL_ROOT.resolve(actualRelativePath);
        assertTrue("lahendus puudub", Files.isRegularFile(actualPath));


        Automaton expectedAutomaton = loadJFlapAutomaton(expectedPath);
        Automaton actualAutomaton = loadJFlapAutomaton(actualPath);

        Automaton expectedMinusActual = expectedAutomaton.minus(actualAutomaton);
        if (!expectedMinusActual.isEmpty()) {
            fail("ei aktsepteeri '%s', aga peaks".formatted(expectedMinusActual.getShortestExample(true)));
        }

        Automaton actualMinusExpected = actualAutomaton.minus(expectedAutomaton);
        if (!actualMinusExpected.isEmpty()) {
            fail("aktsepteerib '%s', aga ei tohiks".formatted(actualMinusExpected.getShortestExample(true)));
        }

        assertEquals(expectedAutomaton, actualAutomaton); // sanity check, should be equal
    }

    private static Automaton loadJFlapAutomaton(Path path) {
        Map<Integer, State> states = new HashMap<>();
        State initialState = null;
        Set<StatePair> epsilons = new HashSet<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(path.toFile());

            NodeList stateNodes = doc.getElementsByTagName("state");
            for (int i=0; i < stateNodes.getLength(); i++) {
                Element stateNode = (Element)stateNodes.item(i);
                int stateId = Integer.parseInt(stateNode.getAttribute("id"));

                State state = new State();
                state.setAccept(stateNode.getElementsByTagName("final").getLength() == 1);

                if (stateNode.getElementsByTagName("initial").getLength() == 1) {
                    initialState = state;
                }

                states.put(stateId, state);
            }

            NodeList transitionNodes = doc.getElementsByTagName("transition");
            for (int i=0; i < transitionNodes.getLength(); i++) {
                Element transitionNode = (Element)transitionNodes.item(i);

                int fromId = Integer.parseInt(getXmlChildContent(transitionNode, "from"));
                int toId = Integer.parseInt(getXmlChildContent(transitionNode, "to"));
                State fromState = states.get(fromId);
                State toState = states.get(toId);

                String label = getXmlChildContent(transitionNode, "read");

                if (label.isEmpty()) {
                    epsilons.add(new StatePair(fromState, toState));
                }
                else if (label.length() == 1) {
                    fromState.addTransition(new Transition(label.charAt(0), toState));
                }
                else {
                    throw new IllegalArgumentException("Multichar transition label");
                }
            }

            Automaton automaton = new Automaton();
            Objects.requireNonNull(initialState, "No initial state");
            automaton.setInitialState(initialState);
            automaton.addEpsilons(epsilons);
            automaton.restoreInvariant();
            automaton.setDeterministic(false);
            return automaton;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getXmlChildContent(Element element, String tagName) {
        NodeList children = element.getElementsByTagName(tagName);
        if (children.getLength() != 1) {
            throw new IllegalArgumentException("Expected single child with given name");
        }

        return children.item(0).getTextContent();
    }
}

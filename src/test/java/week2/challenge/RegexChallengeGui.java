package week2.challenge;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RegexChallengeGui implements ItemListener {
    private JPanel cards; //a panel that uses CardLayout
    private static JTextField initialField; // needed to request focus for first window


    private static class TextNote extends JTextArea {
        private TextNote(String text) {
            super(text);
            setBorder(new EmptyBorder(5,5,5,5));
            setBackground(null);
            setEditable(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setFocusable(false);
        }
    }
    private static final TextNote output = new TextNote("Sisesta regex ja vajuta enterit!");

    private void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        JComboBox<String> cb = new JComboBox<>(RegexChallengeTask.getNames());
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(new JLabel("Tase: "));
        comboBoxPane.add(cb);
        cards = new JPanel(new CardLayout());

        for (int i = 0; i < RegexChallengeTask.TASKS.length; i++) {
            RegexChallengeTask regexChallengeTask = RegexChallengeTask.TASKS[i];
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            TextNote text = new TextNote(regexChallengeTask.getDescription());
            JTextField input = new JTextField();
            if (i == 0) initialField = input;

            //text.setHorizontalAlignment(SwingConstants.CENTER);
            card.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    input.requestFocusInWindow();
                }
            });
            input.addActionListener(e -> {
                String text1 = input.getText();
                output.setText(check(regexChallengeTask.getAutomaton(), text1));
            });
            card.add(text);
            card.add(input);
            cards.add(card, regexChallengeTask.getName());
        }

        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
        pane.add(output, BorderLayout.PAGE_END);
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
        output.setText("");
     }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AKT Regex Challenge");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 200));

        RegexChallengeGui demo = new RegexChallengeGui();
        demo.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        initialField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(RegexChallengeGui::createAndShowGUI);
    }

    public static String check(Automaton a, String s) {
        try {
            Automaton q = (new RegExp(s, RegExp.NONE)).toAutomaton();
            String s1 = BasicOperations.minus(a, q).getShortestExample(true);
            if (s1 != null) return ("Sinu regex ei tunne ära sõna \"" + eps(s1) + "\".");
            else {
                String s2 = BasicOperations.minus(q, a).getShortestExample(true);
                if (s2 != null) return ("Sinu regex sobitub ka sõnaga \"" + eps(s2) + "\".");
                else return ("Palju õnne! Õige regex!");
            }
        } catch (IllegalArgumentException e) {
            return "Viga (" + e.getMessage() + ")";
        }
    }

    private static String eps(String s) {
        if (s.isEmpty())
            return "ε";
        else
            return s;
    }
}


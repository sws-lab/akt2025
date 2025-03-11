package week4;

import week3.FiniteAutomaton;
import week4.regex.RegexParser;
import week4.regex.ast.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            System.err.println(
                    """
                            Programm vajab vähemalt ühte argumenti: regulaaravaldist.
                            Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).
                            Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.
                            Rohkem argumente programm ei aktsepteeri.
                            """
            );
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        FiniteAutomaton automaton = determinize(regexToFiniteAutomaton(regex));

        InputStream inputStream;
        if (args.length == 2) {
            inputStream = Files.newInputStream(Paths.get(args[1]));
        } else {
            inputStream = System.in;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // kuva ekraanile need read, mis vastavad antud regulaaravaldisele/automaadile
            String line;
            while ((line = reader.readLine()) != null) {
                if (automaton.accepts(line)) {
                    System.out.println(line);
                }
            }
        }
    }

    /*
     * See meetod peab loenguslaididel toodud konstruktsiooni põhjal koostama ja tagastama
     * etteantud regulaaravaldisele vastava mittedetermineeritud lõpliku automaadi.
     * Selle meetodi korrektne implementeerimine on antud ülesande juures kõige tähtsam.
     *
     * (Sa võid selle meetodi implementeerimiseks kasutada abimeetodeid ja ka abiklasse,
     * aga ära muuda meetodi signatuuri, sest automaattestid eeldavad just sellise signatuuri
     * olemasolu.)
     *
     * (Selle ülesande juures pole põhjust kasutada vahetulemuste salvestamiseks klassivälju,
     * aga kui sa seda siiski teed, siis kontrolli, et see meetod töötab korrektselt ka siis,
     * kui teda kutsutakse välja mitu korda järjest.)
     */
    public static FiniteAutomaton regexToFiniteAutomaton(RegexNode regex) {
        throw new UnsupportedOperationException();
    }

    /**
     * See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     * automaat tuleb determineerida.
     * Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton determinize(FiniteAutomaton nfa) {
        return nfa;
    }
}

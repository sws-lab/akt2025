package week3.mealy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Siin on vaja defineerida järgmised masinad. Kõige paremini saab nendest aru, kui hoolikalt
 * teste läbi vaadata. Seekord on kõik testid avalikud. Me võime aga kosmeetilisi muudatusi nendele teha,
 * seega ära päris ainult nende pealt progremmeeri.
 */
public class MachineDefs {


    /**
     * Tulemusena tahaks saada masin, mis realiseerib meie HtmlStrip ülesanne.
     * Siin on alustatud, aga väike viga on sees... Lisaks võiks masina täiendada, et
     * mõlemat tüüpi jutumärkidega arvestaks.
     */
    public static MealyMachine getHtmlMachine() {
        List<TableEntry> table = Arrays.asList(
                // Viga tuleks ära parandada:
                new TableEntry(0, '<', 1, "x"),
                new TableEntry(0, '_', 0, "_"),

                new TableEntry(1, '>', 0, ""),
                new TableEntry(1, '\'', 2, ""),
                new TableEntry(1, '_', 1, ""),

                new TableEntry(2, '\'', 1, ""),
                new TableEntry(2, '_', 2, "")

                // Lisada ka üleminekud tavaliste jutumärkide jaoks.
                // NB! Üleval on üleminek (1, '_', 1, ""), mis sobitub kõikide tähtedega.

        );
        return new MealyMachine(table);
    }


    /**
     * Masin, mis asendab iga teine 'x' tähega 'o', näiteks 'xxxx' → 'xoxo'.
     */
    public static MealyMachine getXoxoMachine() {
        // Ei tohiks olla liiga keeruline masin...
        throw new UnsupportedOperationException();
    }

    /**
     * Siin on vaja plus-avaldist juppideks jagada, toppides etteantud eraldaja juppide vahele.
     * Kui eraldaja on näiteks '|', siis sisendi "10+kala++5" korral tahaks saada "10|+|kala|+|+|5".
     * Ma ei taha saada "10|+|kala|+||+|5"
     */
    public static MealyMachine getTokenizer(char delim) {
        // Samuti kahe seisundiga masin: vaata courses'ist 3. Olekumasinad -> PlusMiinus
        // Sealset viimast seisundit ei ole vaja ja väljundid on siin muidugi lihtsamad.
        throw new UnsupportedOperationException();
    }

    /**
     * Sisendiks on sulgavaldis, kus on peidus üks dollarmärk. Tuleb tagastada dollarmärgi sügavus ehk
     * kui palju rohkem on dollarmärgini jõudes sulge avatud, kui seda on kinni pandud. Kuna lõpliku arv
     * seisunditega on seda võimatu lahendada, võib eeldada, et maksimaalne sügavus on 10.
     */
    public static MealyMachine getDepthMachine() {
        List<TableEntry> table = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // Lisada iga seisundi jaoks üleminekud siia.
            throw new UnsupportedOperationException();
        }
        return new MealyMachine(table);
    }

    /**
     * See on see FormatMachine ülesanne, aga lõpuks saab seda normaalselt kirja panna.  :)
     * See ei ole liiga tüütu, kui kasutada kirjavahemärkide lisamiseks abifunktsioon,
     * eeldades muidugi, et automaat on kõigepealt optimeeritud.
     */
    public static MealyMachine getFormatter() {
        // Lõpuks saab ülesanne korralikult lahendatud.
        throw new UnsupportedOperationException();
    }


    private static void addPuncts(List<TableEntry> table, int pre) {
        List<Character> puncts = Arrays.asList(',',';',':', '!','?','.', ')');
        for (char c : puncts) table.add(new TableEntry(pre, c, 1, "_"));
        table.add(new TableEntry(pre, '\n', 0, "\n"));
    }

    /**
     * Aitab naljast, nüüd lõpuks saab natuke mõelda... Sisenditeks on binaararvud, mis lõppevad dollariga.
     * Tuleks öelda, mis on antud binaararvu väärtus kümnendsüsteemis, aga kuna meie automaadid on lõplikud,
     * siis saame ainult loendada teatud arvuni. Kui piir on ületatud, siis tuleb uuesti nullist lugema hakkata.
     * Teiste sõnadaga, me fikseerime arvu n ja masin arvutab, mis on binaararvu jääk jagamisel n-iga.
     */
    public static MealyMachine getBinaryMachine(int n) {
        // See on tegelikult lihtne, kui Algebra loengutest on meeles, kuidas jäägiklassid käituvad...
        throw new UnsupportedOperationException();
    }
}

package week2;

import java.util.Map;

public class TextAnalyzer {

    // Sõned, mis lõppevad tähtedega "ed" või mõni täht veel.
    // (Ülesanne lehel on pikemalt seletatud!)
    public static final String RE1 = null;

    // Paaritu pikkusega sõned.
    public static final String RE2 = null;

    // Sõned, mille esimene ja viimane täht on sama!
    public static final String RE3 = null;

    // Sõned, mis ülesanne nimede tingimustele vastavad.
    public static final String NAME = null;

    // Sõned, mis ülesanne numbri tingimustele vastavad.
    public static final String NUMBER = null;

    public TextAnalyzer(String text) {
        // Kas siin peaks ka midagi tegema?
    }

    public Map<String, String> getPhoneNumbers() {
        throw new UnsupportedOperationException();
    }

    public String anonymize() {
        throw new UnsupportedOperationException();
    }


    public static void main(String[] args) {

        String input =
                """
                        Mina olen Kalle Kulbok ja mu telefoninumber on 5556 4272.
                        Mina olen Peeter Peet ja mu telefoninumber on 5234 567.
                        Mari Maasikas siin, mu number on 6723 3434. Tere, olen Jaan Jubin numbriga 45631643.""";

        TextAnalyzer ta = new TextAnalyzer(input);
        Map<String, String> phoneBook = ta.getPhoneNumbers();
        System.out.println(phoneBook.get("Peeter Peet")); // peab väljastama 5234567
        System.out.println(phoneBook.get("Jaan Jubin"));  // peab väljastama 45631643

        System.out.println(ta.anonymize());

        /* peab väljastama:
            Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
            Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
            <nimi> siin, mu number on <telefoninumber>. Tere, olen <nimi> numbriga <telefoninumber>.
        */
    }
}

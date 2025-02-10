package week1.soojendus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Exercise1 {

    // Lugeda failist kõik read ja liita neid kokku.
    // Faili nimi antakse käsurea argumendina.
    // NB! Seda tuleks from scratch lahendada. UPLOAD ONLY SOL!
    public static void main(String[] args) throws IOException {

        // Kole ja vanamoodne lähenemine (File, Scanner)
        /*File file = new File(args[0]);
        try (Scanner scanner = new Scanner(file, "UTF-8")) {

            int sum = 0;
            while (scanner.hasNextInt()) {
                sum += scanner.nextInt();
            }

            System.out.println(sum);
        }*/

        Path path = Paths.get(args[0]);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            int sum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                sum += Integer.parseInt(line);
            }

            System.out.println(sum);
        }
    }
}

package week2.regexapi;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Exercise1Test {

    @Test
    public void test01Lyhike() {
        checkAddress("Minu aadress: Cumberlandi maakond, Sydney, Wallaby tee 42, akvaarium 1",
                "Cumberlandi maakond, Sydney, Wallaby tee 42, akvaarium 1");
        checkAddress("Kauge sugulane elab hoopis aadressil Pärnu maakond, Pärnu linn, Mingi tänav 777",
                "Pärnu maakond, Pärnu linn, Mingi tänav 777");
    }

    @Test
    public void test02PikemTekst() {
        String test = """
                Dolor necessitatibus est harum atque ratione. Officiis ut dolorem dolores ab repudiandae provident dolor. Quia quam et sunt voluptas repellendus. Assumenda numquam maiores distinctio doloremque quia aperiam. Laborum magni Tartu maakond, Tartu, Liivi 2, kabinet 111 expedita ipsum omnis rerum cupiditate excepturi et. Excepturi officiis et doloribus dignissimos culpa magnam id assumenda.
                Possimus unde eum ut magnam non expedita. Nihil soluta nemo sint qui et quia quidem blanditiis. Culpa quia minima consectetur delectus tenetur nemo vitae.
                Iusto ut sit nihil maxime est architecto pariatur. Voluptas nostrum Jõgeva maakond, Tallinna linnanimi, Mingi tänav 777, dignissimos magni molestiae soluta in voluptates culpa. Sed excepturi ipsum minima quia cum unde. Quisquam explicabo magnam deserunt. Omnis atque aut omnis.
                Praesentium laudantium sequi quia labore reprehenderit libero ullam. Nesciunt alias sunt expedita omnis molestiae sed. Quidem dolorum qui illum quam iure veritatis fugiat facere. Repellendus officiis similique et. Ducimus enim laborum odio qui et eveniet.
                Ducimus et aut consequatur tenetur qui. Ea quia est corporis. Velit ipsum dolore dolore accusantium expedita debitis deleniti. Nemo nesciunt Cumberlandi maakond, Sydney, Wallaby tee 42, akvaarium 1 nulla magni consectetur perspiciatis numquam odio eius.""";

        List<String> expected = Arrays.asList(
                "Tartu maakond, Tartu, Liivi 2, kabinet 111",
                "Jõgeva maakond, Tallinna linnanimi, Mingi tänav 777",
                "Cumberlandi maakond, Sydney, Wallaby tee 42, akvaarium 1"
        );

        List<String> addresses = Exercise1.extractAddresses(test);
        assertEquals(expected, addresses);
    }

    @Test
    public void test03False() {
        checkAddress("See siis on valesti HARJU maakond, TALLINN, Risti tänav 22");
        checkAddress("See siis on samuti valesti Harju maakond, Tallinn 50, Risti tänav 22");
        checkAddress("Endiselt valesti Harju maakond, Tallinn, Risti tänav, kassipojad koju!");
    }

    @Test
    public void test04Erinevad() {
        checkAddress("Kuskil majandab Jaan, kes elab Pärnu MAAKOND, Juhu küla, Meeri tänav 15. Talle läheb külla Maie, kes sai sihtmärgiks hoopis" +
                        "Pärnu Maakond, Juhu küla, Meeri tänav 15. Kõige viimaseks tuleb aadressilt Tartu maakond, Tartu linn, Lai 22, korter 5 keegi Kaupo.",
                "Tartu maakond, Tartu linn, Lai 22, korter 5");
    }

    @Test
    public void test05Erinevad2() {
        checkAddress("Siit peaks aadressi kätte saama. Minu maakond, Jaanilinn, Sõle 55, 11 korter on halvas olukorras. " +
                        "Siit ei tohiks: Viljandi maakond, Viljandi, 45 suur koht." +
                        "Samas siit jällegi võiks Lääne maakond, Haapsalu linn, Pikk 111, tuba 45, pole kunagi käinud." +
                        "Reavahetused on pahad: Tartu maakond, Tartu linn,\nLai 22, korter 5." +
                        "Ning vale formatting on ka paha Tartu Maakond, Tartu Linn, Lai 15",
                "Minu maakond, Jaanilinn, Sõle 55, 11",
                "Lääne maakond, Haapsalu linn, Pikk 111, tuba 45");
    }

    private void checkAddress(String text, String... expectedAddresses) {
        List<String> expected = Arrays.asList(expectedAddresses);
        List<String> addresses = Exercise1.extractAddresses(text);
        assertEquals(expected, addresses);
    }
}

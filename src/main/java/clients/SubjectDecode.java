package clients;

import java.util.Scanner;

import mua.message.header.Intestazione;
import mua.message.header.Oggetto;

/** SubjectDecode */
public class SubjectDecode {

    /**
     * Tests subject value decoding
     *
     * <p>
     * Reads a line from stdin containing the encoding of the value of a subject
     * header and emits
     * its decoded version in the stdout.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Intestazione i;
        try (Scanner s = new Scanner(System.in)) {
            i = Oggetto.parse(s.nextLine());
        }
        System.out.println(i.valore());
    }
}

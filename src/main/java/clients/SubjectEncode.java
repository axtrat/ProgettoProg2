package clients;

import java.util.Scanner;

import mua.Intestazione;
import mua.Oggetto;

/** SubjectEncode */
public class SubjectEncode {

    /**
     * Tests subject value encoding
     *
     * <p>
     * Reads a line from stdin containing the value of a subject header and emits
     * its encoded
     * version in the stdout.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Intestazione i;
        try (Scanner s = new Scanner(System.in)) {
            i = new Oggetto(s.nextLine());
        }
        System.out.println(i);
    }

}

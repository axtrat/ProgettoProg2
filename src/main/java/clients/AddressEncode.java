package clients;

import java.util.Scanner;

import mua.Indirizzo;
import mua.Intestazione;
import mua.Mittente;

/** AddressEncode */
public class AddressEncode {

    /**
     * Tests address encoding
     *
     * <p>
     * Reads three lines from stdin corresponding to the (possibly empty)
     * <em>display name</em>,
     * <em>local</em>, and <em>domain</em> parts of the address and emits a line in
     * the stout
     * containing the encoding of the email address.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Intestazione i;
        try (Scanner s = new Scanner(System.in)) {
            String nome = s.nextLine();
            String locale = s.nextLine();
            String dominio = s.nextLine();
            i = new Mittente(new Indirizzo(nome, locale, dominio));
        }
        System.out.println(i);
    }

}

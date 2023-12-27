package clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mua.Destinatario;
import mua.Indirizzo;
import mua.Intestazione;

/** RecipientsEncode */
public class RecipientsEncode {

    /**
     * Tests recipients encoding
     *
     * <p>
     * Reads a series of lines from stidn, each containing a comma separated list of
     * three strings
     * corresponding to the (possibly empty) <em>display name</em>, <em>local</em>,
     * and
     * <em>domain</em> parts of an address and emits a line in stdout containing the
     * encoding of the
     * recipients header obtained using such addresses.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Intestazione i;
        List<Indirizzo> indirizzi;
        try (Scanner s = new Scanner(System.in)) {
            indirizzi = new ArrayList<>();
            while (s.hasNextLine()) {
                String[] parts = s.nextLine().split(", ");
                indirizzi.add(new Indirizzo(parts[0], parts[1], parts[2]));
            }
        }
        i = new Destinatario(indirizzi);
        System.out.println(i.tipo() + ": " + i);
    }

}

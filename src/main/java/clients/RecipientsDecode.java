package clients;

import java.util.Scanner;

import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;

/** RecipientsDecode */
public class RecipientsDecode {

    /**
     * Tests recipients decoding
     *
     * <p>
     * Reads a line from stdin containing the encoding of the recipients header and
     * for every
     * address in the header emits a line in stdout containing a comma separated
     * list of three strings
     * corresponding to the (possibly empty) <em>display name</em>, <em>local</em>,
     * and
     * <em>domain</em> parts of the address.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Intestazione i;
        try (Scanner s = new Scanner(System.in)) {
            String destinatari = s.nextLine().split(": ")[1];
            i = Destinatario.parse(destinatari);
        }
        Destinatario d = (Destinatario) i;
        for (Indirizzo indirizzo : d.valore()) {
            System.out.print(indirizzo.nome() + ", ");
            System.out.print(indirizzo.locale() + ", ");
            System.out.println(indirizzo.dominio());
        }
    }
}

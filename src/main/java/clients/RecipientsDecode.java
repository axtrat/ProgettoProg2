package clients;

import java.util.Scanner;

import mua.message.header.Address;
import mua.message.header.Recipient;
import utils.ASCIICharSequence;

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
        Recipient recipient;
        try (Scanner s = new Scanner(System.in)) {
            String destinatari = s.nextLine().split(": ")[1];
            recipient = Recipient.parse(ASCIICharSequence.of(destinatari));
        }
        for (Address address : recipient.value()) {
            System.out.print(address.nome() + ", ");
            System.out.print(address.locale() + ", ");
            System.out.println(address.dominio());
        }
    }
}

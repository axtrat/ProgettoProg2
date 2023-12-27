package clients;

import mua.Indirizzo;

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
        try (java.util.Scanner s = new java.util.Scanner(System.in)) {
            String nome = s.nextLine();
            String locale = s.nextLine();
            String dominio = s.nextLine();
            mua.Indirizzo address = new Indirizzo(nome, locale, dominio);
            System.out.println(address);
        }
    }

}

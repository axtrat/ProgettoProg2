package clients;

import java.util.Scanner;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import mua.message.header.Mittente;

/** AddressDecode */
public class AddressDecode {

  /**
   * Tests address decoding
   *
   * <p>Reads a line from stdin containing the encoding of an email address and emits three lines in
   * the stout corresponding to the (possibly empty) <em>display name</em>, <em>local</em>, and
   * <em>domain</em> parts of the address.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Intestazione i;
    try (Scanner s = new Scanner(System.in)) {
      i = new Mittente(Indirizzo.parse(s.nextLine()));
    }
    Indirizzo address = (Indirizzo) i.valore();
    System.out.println(address.nome());
    System.out.println(address.locale());
    System.out.println(address.dominio());
  }
}

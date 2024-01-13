package clients;

import java.util.Scanner;
import mua.message.header.Address;
import mua.message.header.Header;
import mua.message.header.Sender;

/** AddressEncode */
public class AddressEncode {

  /**
   * Tests address encoding
   *
   * <p>Reads three lines from stdin corresponding to the (possibly empty) <em>display name</em>,
   * <em>local</em>, and <em>domain</em> parts of the address and emits a line in the stout
   * containing the encoding of the email address.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Header i;
    try (Scanner s = new Scanner(System.in)) {
      String nome = s.nextLine();
      String locale = s.nextLine();
      String dominio = s.nextLine();
      i = new Sender(new Address(nome, locale, dominio));
    }
    System.out.println(i);
  }
}

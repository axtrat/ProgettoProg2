package clients;

import java.util.Scanner;
import mua.message.header.Address;
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
    Sender s;
    try (Scanner sc = new Scanner(System.in)) {
      String nome = sc.nextLine();
      String locale = sc.nextLine();
      String dominio = sc.nextLine();
      s = new Sender(new Address(nome, locale, dominio));
    }
    System.out.println(s);
  }
}

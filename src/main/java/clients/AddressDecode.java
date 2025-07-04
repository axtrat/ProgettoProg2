package clients;

import java.util.Scanner;

import mua.message.header.Address;
import mua.message.header.Sender;
import utils.ASCIICharSequence;

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
    Sender s;
    try (Scanner sc = new Scanner(System.in)) {
      s = new Sender(Address.parse(ASCIICharSequence.of(sc.nextLine())));
    }
    Address address = s.value();
    System.out.println(address.nome());
    System.out.println(address.locale());
    System.out.println(address.dominio());
  }
}

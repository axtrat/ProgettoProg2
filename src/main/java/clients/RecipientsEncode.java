package clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mua.message.header.Address;
import mua.message.header.Recipient;
import mua.message.header.Header;

/** RecipientsEncode */
public class RecipientsEncode {

  /**
   * Tests recipients encoding
   *
   * <p>Reads a series of lines from stidn, each containing a comma separated list of three strings
   * corresponding to the (possibly empty) <em>display name</em>, <em>local</em>, and
   * <em>domain</em> parts of an address and emits a line in stdout containing the encoding of the
   * recipients header obtained using such addresses.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Header i;
    List<Address> indirizzi;
    try (Scanner s = new Scanner(System.in)) {
      indirizzi = new ArrayList<>();
      while (s.hasNextLine()) {
        String[] parts = s.nextLine().split(", ");
        indirizzi.add(new Address(parts[0], parts[1], parts[2]));
      }
    }
    i = new Recipient(indirizzi);
    System.out.println(i.type() + ": " + i);
  }
}

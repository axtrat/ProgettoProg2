package clients;

import java.util.Scanner;
import mua.message.header.Header;
import mua.message.header.Subject;
import utils.ASCIICharSequence;

/** SubjectDecode */
public class SubjectDecode {

  /**
   * Tests subject value decoding
   *
   * <p>Reads a line from stdin containing the encoding of the value of a subject header and emits
   * its decoded version in the stdout.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Header i;
    try (Scanner s = new Scanner(System.in)) {
      i = Subject.parse(ASCIICharSequence.of(s.nextLine()));
    }
    System.out.println(i.value());
  }
}

package clients;

import java.util.Scanner;

import mua.message.header.Header;
import mua.message.header.Subject;

/** SubjectEncode */
public class SubjectEncode {

  /**
   * Tests subject value encoding
   *
   * <p>Reads a line from stdin containing the value of a subject header and emits its encoded
   * version in the stdout.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Header i;
    try (Scanner s = new Scanner(System.in)) {
      i = new Subject(s.nextLine());
    }
    System.out.println(i);
  }
}

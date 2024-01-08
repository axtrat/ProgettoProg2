package clients;

import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import utils.ASCIICharSequence;
import utils.EntryEncoding;
import utils.Fragment;

/** MessageDecode */
public class MessageDecode {

  /**
   * Tests message decoding
   *
   * <p>Reads a message from stdin and emits its fragments on the stdout.
   *
   * <p>Every fragment should be emitted as, for example:
   *
   * <pre>{@code
   * Fragment
   *   Raw headers:
   *     Raw type = from, value = Luca Prigioniero <prigioniero@di.unimi.it>
   *     Raw type = to, value = "Massimo prof. Santini" <santini@di.unimi.it>, info@unimi.it
   *     Raw type = subject, value = Oggetto semplice
   *     Raw type = date, value = Wed, 6 Dec 2023 12:30:20 +0100
   *     Raw type = mime-version, value = 1.0
   *     Raw type = content-type, value = multipart/alternative; boundary=frontier
   *   Raw body:
   *     This is a message with multiple parts in MIME format.
   * }</pre>
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    StringJoiner sj = new StringJoiner("\n");
    try (Scanner sc = new Scanner(System.in)) {
      while (sc.hasNextLine()) sj.add(sc.nextLine());
    }

    List<Fragment> fragments = EntryEncoding.decode(ASCIICharSequence.of(sj.toString()));
    for (Fragment fragment : fragments) {
      System.out.println("Fragment\n\tRaw headers:");
      for (List<ASCIICharSequence> rawHeader : fragment.rawHeaders())
        System.out.println("\t\tRaw type = " + rawHeader.get(0) + ", value = " + rawHeader.get(1));
      System.out.println("\t\tRaw body: \n\t\t" + fragment.rawBody() + "\n");
    }
  }
}

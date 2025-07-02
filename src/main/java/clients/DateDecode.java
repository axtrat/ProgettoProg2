package clients;

import java.time.ZonedDateTime;
import java.util.Scanner;
import mua.message.header.Date;
import mua.message.header.Header;
import utils.ASCIICharSequence;

/** DateDecode */
public class DateDecode {

  /**
   * Tests date decoding
   *
   * <p>Reads a line from stdin containing the encoding of a date and emits the corresponding day of
   * week in the stout.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    Header i;
    try (Scanner sc = new Scanner(System.in)) {
      i = Date.parse(ASCIICharSequence.of(sc.nextLine()));
    }
    ZonedDateTime date = (ZonedDateTime) i.value();
    System.out.println(date.getDayOfWeek());
  }
}

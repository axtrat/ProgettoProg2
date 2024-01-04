package clients;

import java.time.ZonedDateTime;
import java.util.Scanner;
import mua.message.header.Data;
import mua.message.header.Intestazione;
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
    Intestazione i;
    try (Scanner sc = new Scanner(System.in)) {
      i = Data.parse(ASCIICharSequence.of(sc.nextLine()));
    }
    ZonedDateTime date = (ZonedDateTime) i.valore();
    System.out.println(date.getDayOfWeek());
  }
}

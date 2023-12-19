package clients;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/** MessageEncode */
public class MessageEncode {

  public static final ZonedDateTime DATE =
      ZonedDateTime.of(2023, 12, 6, 12, 30, 20, 200, ZoneId.of("Europe/Rome"));

  /**
   * Tests message encoding
   *
   * <p>Reads a message from stdin and emits its encoding on the stdout.
   *
   * <p>The stdin contains:
   *
   * <ul>
   *   <li>the sender address (three lines, see {@link AddressDecode}),
   *   <li>two recipient addresses (three lines each, as above),
   *   <li>the subject (one line),
   *   <li>the text part (one line, possibly empty),
   *   <li>the HTML part (one line, possibly empty).
   * </ul>
   *
   * To such information, the program adds the date corresponding to {@link #DATE}.
   *
   * @param args not used
   */
  // public static void main(String[] args) {}

}

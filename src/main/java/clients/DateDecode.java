package clients;

import java.time.ZonedDateTime;
import java.util.Scanner;

import mua.Date;
import mua.Intestazione;
import utils.ASCIICharSequence;
import utils.DateEncoding;

/** DateDecode */
public class DateDecode {

    /**
     * Tests date decoding
     *
     * <p>
     * Reads a line from stdin containing the encoding of a date and emits the
     * corresponding day of
     * week in the stout.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            Intestazione i = new Date(DateEncoding.decode(ASCIICharSequence.of(s.nextLine())));
            ZonedDateTime date = (ZonedDateTime) i.valore();
            System.out.println(date.getDayOfWeek());
        }
    }

}

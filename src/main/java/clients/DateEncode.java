package clients;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Scanner;

import mua.message.header.Data;
import mua.message.header.Intestazione;
import utils.DateEncoding;

/** DateEncode */
public class DateEncode {

    /**
     * Tests date encoding
     *
     * <p>
     * Reads three integers from stdin corresponding to an year, month and day and
     * emits a line in
     * the stout containing the encoding of the date corresponding to the exact
     * midnight of such date
     * (in the "Europe/Rome" timezone).
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        try (Scanner s = new java.util.Scanner(System.in)) {
            int year = s.nextInt();
            int month = s.nextInt();
            int day = s.nextInt();
            ZonedDateTime date = ZonedDateTime.of(LocalDate.of(year, month, day), LocalTime.MIDNIGHT, DateEncoding.EUROPE_ROME);
            Intestazione i = new Data(date);
            System.out.println(i);
        }
    }

}

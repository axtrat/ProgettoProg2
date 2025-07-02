package mua.message.header;

import java.time.ZonedDateTime;
import java.util.Objects;

import utils.ASCIICharSequence;
import utils.DateEncoding;

/**
 * Date è una classe immutabile che rappresenta un'intestazione di tipo Date.
 *
 * <p>
 * Un'istanza di Date contiene la data e l'ora del messaggio in corrispondenza della time-zone.
 */
public class Date implements Header {

    /** Date e ora */
    private final ZonedDateTime zonedDateTime;

    /*
     * RI:  zonedDateTime != null
     *
     * AF:  AF(zonedDateTime) = Date: zonedDateTime // zonedDateTime codificato secondo lo standard RFC
     */

    /**
     * Costruisce un'intestazione di tipo Date.
     *
     * @param zonedDateTime l'ora del messaggio.
     * @throws NullPointerException se {@code zonedDateTime} è {@code null}.
     */
    public Date(final ZonedDateTime zonedDateTime) {
        this.zonedDateTime = Objects.requireNonNull(zonedDateTime);
    }

    /**
     * Crea l'intestazione Date a partire da una sequenza che lo rappresenta secondo lo standard RFC
     * @param sequence la sequenza ASCII
     * @return Un'istanza di Date
     * @throws NullPointerException se {@code sequence} è null
     */
    public static Date parse(final ASCIICharSequence sequence) {
        return new Date(DateEncoding.decode(Objects.requireNonNull(sequence)));
    }

    @Override
    public String type() {
        return "Date";
    }

    @Override
    public ZonedDateTime value() {
        return zonedDateTime;
    }

    @Override
    public String toString() {
        return DateEncoding.encode(zonedDateTime).toString();
    }
}

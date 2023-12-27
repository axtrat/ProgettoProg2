package mua;

import java.time.ZonedDateTime;
import java.util.Objects;

import utils.DateEncoding;

/** Date è una classe immutabile che rappresenta un'intestazione di tipo Date.
*/
public class Date implements Intestazione {
    private ZonedDateTime zonedDateTime;

    /** Costruisce un'intestazione di tipo Date.
     * @param zonedDateTime il valore dell'intestazione.
     * @throws NullPointerException se zonedDateTime è null.
     */
    public Date(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = Objects.requireNonNull(zonedDateTime);
    }

    @Override
    public String tipo() {
        return "Date";
    }

    @Override
    public ZonedDateTime valore() {
        return zonedDateTime;
    }

    @Override
    public String toString() {
        return DateEncoding.encode(zonedDateTime).toString();
    }
}

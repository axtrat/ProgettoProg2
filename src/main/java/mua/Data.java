package mua;

import java.time.ZonedDateTime;
import java.util.Objects;

import utils.DateEncoding;

/** Data è una classe immutabile che rappresenta un intestazione di tipo Date.
 *  Un'intestazione di tipo Data contiene la data e l'ora del messaggio.
*/
public class Data implements Intestazione {
    private ZonedDateTime zonedDateTime;

    /** Costruisce un'intestazione di tipo Data.
     * @param zonedDateTime l'ora del messaggio.
     * @throws NullPointerException se zonedDateTime è null.
     */
    public Data(ZonedDateTime zonedDateTime) {
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

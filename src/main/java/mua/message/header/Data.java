package mua.message.header;

import java.time.ZonedDateTime;
import java.util.Objects;
import utils.ASCIICharSequence;
import utils.DateEncoding;

/**
 * Data è una classe immutabile che rappresenta un'intestazione di tipo Date.
 *
 * <p>Un'istanza di Data contiene la data e l'ora del messaggio.
 */
public class Data implements Intestazione {
  private final ZonedDateTime zonedDateTime;

  /**
   * Costruisce un'intestazione di tipo Data.
   *
   * @param zonedDateTime l'ora del messaggio.
   * @throws NullPointerException se {@code zonedDateTime} è null.
   */
  public Data(final ZonedDateTime zonedDateTime) {
    this.zonedDateTime = Objects.requireNonNull(zonedDateTime);
  }

  /**
   * Crea l'intestazione Data a partire da una sequenza
   *
   * @param sequence sequenza che contiene la Data
   * @return Un'istanza di Data
   * @throws NullPointerException se {@code sequence} è null
   */
  public static Data parse(final ASCIICharSequence sequence) {
    return new Data(DateEncoding.decode(Objects.requireNonNull(sequence)));
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

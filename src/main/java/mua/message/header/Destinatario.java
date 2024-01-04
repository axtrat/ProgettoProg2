package mua.message.header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo Destinatario.
 *
 * <p>Un'istanza di Destinatario contiene i destinatari del messaggio.
 */
public class Destinatario implements Intestazione {
  private final List<Indirizzo> indirizzi = new ArrayList<Indirizzo>();

  /**
   * Crea l'intestazione dei destinatari a partire da una collezione di indirizzi
   *
   * @param indirizzi iterable di indirizzi
   * @throws NullPointerException se indirizzi è null
   */
  public Destinatario(final Iterable<Indirizzo> indirizzi) {
    Objects.requireNonNull(indirizzi);
    indirizzi.forEach(this.indirizzi::add);
  }

  /**
   * Crea l'intestazione dei destinatari a partire da una sequenza
   *
   * @param sequence sequenza che contiene gli indirizzi separati da virgola
   * @return Un'istanza di Destinatari
   * @throws NullPointerException se {@code sequence} è null
   * @throws IllegalArgumentException se {@code sequence} contiene indirizzi validi
   */
  public static Destinatario parse(final ASCIICharSequence sequence) {
    Objects.requireNonNull(sequence);
    final List<Indirizzo> indirizzi = new ArrayList<Indirizzo>();
    for (final List<String> indirizzo : AddressEncoding.decode(sequence))
      indirizzi.add(new Indirizzo(indirizzo.get(0), indirizzo.get(1), indirizzo.get(2)));
    return new Destinatario(indirizzi);
  }

  @Override
  public String tipo() {
    return "To";
  }

  @Override
  public List<Indirizzo> valore() {
    return Collections.unmodifiableList(indirizzi);
  }

  @Override
  public String toString() {
    final StringJoiner sj = new StringJoiner(", ");
    for (final Indirizzo indirizzo : indirizzi) sj.add(indirizzo.toString());
    return sj.toString();
  }
}

package mua.message.header;

import java.util.Objects;
import utils.ASCIICharSequence;
import utils.Base64Encoding;

/**
 * Oggetto è una classe immutabile che rappresenta un'intestazione di tipo Oggetto.
 *
 * <p>Un'istanza di Oggetto contiene l'oggetto del messaggio.
 */
public class Oggetto implements Intestazione {
  private final String oggetto;

  /**
   * Costruisce un intestazione di tipo Oggetto.
   *
   * @param oggetto l'oggetto del messaggio.
   * @throws NullPointerException se oggetto è null.
   */
  public Oggetto(final String oggetto) {
    this.oggetto = Objects.requireNonNull(oggetto);
  }

  /**
   * Crea un sequence a partire da una sequenza possibilmente codificata in base64.
   *
   * @param sequence la sequenza.
   * @return l'intestazione di tipo Oggetto.
   * @throws NullPointerException se sequence è null.
   */
  public static Oggetto parse(final ASCIICharSequence sequence) {
    Objects.requireNonNull(sequence);
    String decoded = Base64Encoding.decodeWord(sequence);
    if (Objects.isNull(decoded)) return new Oggetto(sequence.toString());
    return new Oggetto(decoded);
  }

  @Override
  public String tipo() {
    return "Subject";
  }

  @Override
  public Object valore() {
    return oggetto;
  }

  @Override
  public String toString() {
    if (ASCIICharSequence.isAscii(oggetto)) return oggetto;
    return Base64Encoding.encodeWord(oggetto);
  }
}

package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo Mittente.
 *
 * <p>Un'istanza di Mittente contiene l'indirizzo del mittente del messaggio.
 */
public class Mittente implements Intestazione {

  private final Indirizzo indirizzo;

  /**
   * Crea un'intestazione di tipo Mittente a partire da un indirizzo.
   *
   * @param indirizzo l'indirizzo del mittente del messaggio.
   * @throws NullPointerException se {@code indirizzo} è {@code null}.
   */
  public Mittente(final Indirizzo indirizzo) {
    this.indirizzo = Objects.requireNonNull(indirizzo);
  }

  /**
   * Crea un'intestazione di tipo Mittente a partire da una sequenza.
   *
   * @param sequence la sequenza da cui creare l'intestazione.
   * @return un'intestazione di tipo Mittente.
   * @throws NullPointerException se {@code sequence} è {@code null}.
   */
  public static Mittente parse(final ASCIICharSequence sequence) {
    return new Mittente(Indirizzo.parse(sequence));
  }

  @Override
  public String tipo() {
    return "From";
  }

  @Override
  public Indirizzo valore() {
    return indirizzo;
  }

  @Override
  public String toString() {
    return indirizzo.toString();
  }
}

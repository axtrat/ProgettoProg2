package mua.message.header;

/**
 * Un'Intestazione è un'interfaccia che definisce le caratteristiche delle intestazioni di un
 * messaggio
 *
 * <p>Un'Intestazione è composta da un tipo e da un valore
 */
public interface Intestazione {
  /**
   * Restituisce il tipo dell'intestazione
   *
   * @return il tipo dell'intestazione
   */
  String tipo();

  /**
   * Restituisce il valore dell'intestazione
   *
   * @return il valore dell'intestazione
   */
  Object valore();
}

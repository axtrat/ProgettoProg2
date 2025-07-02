package mua.message.header;

/**
 * Un'Header è un'interfaccia che aggrega le caratteristiche principali
 * delle intestazioni di un messaggio email
 * <p>
 * Un'Intestazione è composta da un tipo e da un valore
 * <p>
 * Le implementazioni di questa classe devo sovrascrivere il toString in modo tale che
 * restituisca il valore codificato in caratteri ASCII
 */
public interface Header {
    /**
     * Restituisce il tipo dell'intestazione
     * <p> il tipo deve essere conforme lo standard RFC</p>
     *
     * @return il tipo dell'intestazione
     */
    String type();

    /**
     * Restituisce il valore dell'intestazione
     *
     * @return il valore dell'intestazione
     */
    Object value();
}

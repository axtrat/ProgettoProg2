package mua.message.header;

import java.util.Objects;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo Mittente.
 * <p>
 * Un'instanza di Mittente contiene l'indirizzo del mittente del messaggio.
 */
public class Mittente implements Intestazione {

    private final Indirizzo indirizzo;

    /**
     * Crea un'intestazione di tipo Mittente a partire da un'indirizzo.
     * 
     * @param indirizzo l'indirizzo del mittente del messaggio.
     * @throws NullPointerException se indirizzo è null.
     */
    public Mittente(Indirizzo indirizzo) {
        this.indirizzo = Objects.requireNonNull(indirizzo);
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

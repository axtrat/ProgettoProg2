package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo From.
 * <p>
 * Un'istanza di Sender contiene l'indirizzo del mittente del messaggio.
 */
public class Sender implements Header {
    /** L'indirizzo del Sender. */
    private final Address address;

    /*
     * RI:  address != null
     *
     * AF: AF(address) = From: address
     */

    /**
     * Crea un'intestazione di tipo Sender a partire da un indirizzo.
     *
     * @param address l'indirizzo del mittente del messaggio.
     * @throws NullPointerException se {@code address} è {@code null}.
     */
    public Sender(final Address address) {
        this.address = Objects.requireNonNull(address);
    }

    /**
     * Crea un'intestazione di tipo Sender a partire da una sequenza.
     * <p>
     * La sequenza è valida se è composta da un indirizzo valido
     *
     * @param sequence la sequenza da cui creare l'intestazione.
     * @return un'intestazione di tipo Sender.
     * @throws NullPointerException se {@code sequence} è {@code null}.
     */
    public static Sender parse(final ASCIICharSequence sequence) {
        return new Sender(Address.parse(Objects.requireNonNull(sequence)));
    }

    @Override
    public String type() {
        return "From";
    }

    @Override
    public Address value() {
        return address;
    }

    @Override
    public String toString() {
        return address.toString();
    }
}

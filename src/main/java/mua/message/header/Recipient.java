package mua.message.header;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo To.
 * <p>
 * Un'istanza di Recipient contiene i destinatari del messaggio.
 */
public class Recipient implements Header {

    /** Collezione degli indirizzi dei destinatari >= 1*/
    private final Collection<Address> addresses = new LinkedHashSet<>();

    /*
     * RI:  addresses != null, non contiene null e deve avere almeno un indirizzo
     *      addresses non contiene indirizzi duplicati
     *
     * AF:  AF(addresses) = { indirizzo in addresses | indirizzo è un indirizzo di un destinatario }
     */

    /**
     * Crea l'intestazione dei destinatari a partire da una collezione di indirizzi
     *
     * @param addresses iterable di indirizzi
     * @throws NullPointerException se {@code addresses} è {@code null}
     * @throws IllegalArgumentException se {@code addresses} è vuoto
     */
    public Recipient(final Iterable<Address> addresses) {
        Objects.requireNonNull(addresses);
        addresses.forEach(this.addresses::add);
        if (this.addresses.isEmpty())
            throw new IllegalArgumentException("Deve esserci almeno un destinatario");
    }

    /**
     * Crea l'intestazione dei destinatari a partire da una sequenza
     *
     * @param sequence sequenza che contiene gli indirizzi separati da virgola
     * @return Un'istanza di Destinatari
     * @throws NullPointerException     se {@code sequence} è null
     * @throws IllegalArgumentException se {@code sequence} contiene indirizzi
     *                                  non validi
     */
    public static Recipient parse(final ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence);
        final List<Address> indirizzi = new ArrayList<Address>();
        for (final List<String> indirizzo : AddressEncoding.decode(sequence))
            indirizzi.add(new Address(indirizzo.get(0), indirizzo.get(1), indirizzo.get(2)));
        return new Recipient(indirizzi);
    }

    @Override
    public String type() {
        return "To";
    }

    @Override
    public List<Address> value() {
        return List.copyOf(addresses);
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ");
        for (final Address address : addresses)
            sj.add(address.toString());
        return sj.toString();
    }
}

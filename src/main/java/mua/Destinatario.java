package mua;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutabile che rappresenta un intestazione di tipo Destinatario.
 * <p>
 * Un Destinatario contiente i destinatari del messaggio.
 */
public class Destinatario implements Intestazione {
    private List<Indirizzo> indirizzi = new ArrayList<Indirizzo>();

    /**
     * Crea l'intestazione dei destinatari a partire da una collezione di indirizzi
     * @param indirizzi iterable di indirizzi
     * @throws NullPointerException se indirizzi è null
     */
    public Destinatario(Iterable<Indirizzo> indirizzi) {
        Objects.requireNonNull(indirizzi);
        indirizzi.forEach((indirizzo) -> this.indirizzi.add(indirizzo));
    }

    /**
     * Crea l'intestazione dei destinatari a partire da una stringa
     * @param input stringa che contiene gli indirizzi separati da virgola
     * @return Un'istanza di Destinatari
     * @throws NullPointerException se input è null
     * @throws IllegalArgumentException se input contiene indirizzi validi
     */
    public static Destinatario parse(String input) {
        Objects.requireNonNull(input);
        List<Indirizzo> indirizzi = new ArrayList<Indirizzo>();
        for (List<String> indirizzo : AddressEncoding.decode(ASCIICharSequence.of(input)))
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
        StringJoiner sj = new StringJoiner(", ");
        for (Indirizzo indirizzo : indirizzi)
            sj.add(indirizzo.toString());
        return sj.toString();
    }
}

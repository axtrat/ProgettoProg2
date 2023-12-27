package mua;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import utils.ASCIICharSequence;
import utils.AddressEncoding;

public class Destinatari implements Intestazione {
    private List<Indirizzo> indirizzi;

    private Destinatari(List<Indirizzo> indirizzi) {
        this.indirizzi = indirizzi;
    }

    /**
     * Crea l'intestazione dei destinatari a partire da una stringa
     * @param input stringa che contiene gli indirizzi separati da virgola
     * @return Un'istanza di Destinatari
     * @throws NullPointerException se input è null
     * @throws IllegalArgumentException se input contiene indirizzi validi
     */
    public static Destinatari parse(String input) {
        Objects.requireNonNull(input);
        List<Indirizzo> indirizzi = new ArrayList<Indirizzo>();
        for (List<String> indirizzo : AddressEncoding.decode(ASCIICharSequence.of(input)))
            indirizzi.add(new Indirizzo(indirizzo.get(0), indirizzo.get(1), indirizzo.get(2)));
        return new Destinatari(indirizzi);
    }


    @Override
    public String tipo() {
        return "To";
    }

    @Override
    public List<Indirizzo> valore() {
        return Collections.unmodifiableList(indirizzi);
    }

}

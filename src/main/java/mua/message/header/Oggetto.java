package mua.message.header;

import utils.ASCIICharSequence;
import utils.Base64Encoding;

import java.util.Objects;

/**
 * Oggetto è una classe immutabile che rappresenta un'intestazione di tipo Oggetto.
 * <p>
 * Un'istanza di Oggetto contiene l'oggetto del messaggio.
 */
public class Oggetto implements Intestazione {
    private final String oggetto;

    /**
     * Costruisce un intestazione di tipo Oggetto.
     * @param oggetto l'oggetto del messaggio.
     * @throws NullPointerException se oggetto è null.
     */
    public Oggetto(final String oggetto) {
        this.oggetto = Objects.requireNonNull(oggetto);
    }

    /**
     * Crea un oggetto a partire da una stringa possibilmente codificata in base64.
     * @param oggetto la stringa.
     * @return l'intestazione di tipo Oggetto.
     * @throws NullPointerException se oggetto è null.
     */
    public static Oggetto parse(final String oggetto) {
        Objects.requireNonNull(oggetto);
        String decoded = Base64Encoding.decodeWord(ASCIICharSequence.of(oggetto));
        if (Objects.isNull(decoded)) return new Oggetto(oggetto);
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

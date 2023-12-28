package mua.message.header;

import utils.ASCIICharSequence;
import utils.Base64Encoding;

import java.util.Objects;

/**
 * Oggetto è una classe immutabile che rappresenta un'intestazione di tipo Oggetto.
 * <p>
 * Un'instanza di Oggetto contiene l'oggetto del messaggio.
 */
public class Oggetto implements Intestazione {
    private final String oggetto;

    /**
     * Costruisce un intestazione di tipo Oggetto.
     * @param oggetto l'oggetto del messaggio.
     * @throws NullPointerException se oggetto è null.
     */
    public Oggetto(String oggetto) {
        this.oggetto = Objects.requireNonNull(oggetto);
    }

    /**
     * Crea un oggetto a partire da una stringa possibilimente codificata in base64.
     * @param oggetto la stringa.
     * @return l'intestazione di tipo Oggetto.
     * @throws NullPointerException se oggetto è null.
     */
    public static Oggetto parse(String oggetto) {
        Objects.requireNonNull(oggetto);
        if (oggetto.startsWith("=?utf-8?B?") && oggetto.endsWith("?="))
            return new Oggetto(Base64Encoding.decodeWord(ASCIICharSequence.of(oggetto)));
        return new Oggetto(oggetto);
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

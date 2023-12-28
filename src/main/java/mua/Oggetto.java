package mua;

import utils.ASCIICharSequence;
import utils.Base64Encoding;

import java.util.Objects;

public class Oggetto implements Intestazione {
    private final String oggetto;

    /**
     * Costruisce un'intestazione di tipo Oggetto.
     * @param oggetto l'oggetto del messaggio.
     * @throws NullPointerException se oggetto è null.
     */
    public Oggetto(String oggetto) {
        this.oggetto = Objects.requireNonNull(oggetto);
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

    public static Intestazione parse(String oggetto) {
        if (oggetto.startsWith("=?utf-8?B?") && oggetto.endsWith("?="))
            return new Oggetto(Base64Encoding.decodeWord(ASCIICharSequence.of(oggetto)));
        return new Oggetto(oggetto);
    }
}

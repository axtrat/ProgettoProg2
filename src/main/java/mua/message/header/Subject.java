package mua.message.header;

import java.util.Objects;
import utils.ASCIICharSequence;
import utils.Base64Encoding;

/**
 * Subject è una classe immutabile che rappresenta un'intestazione di tipo Subject.
 * <p>
 * Un'istanza di Subject contiene l'oggetto del messaggio.
 */
public class Subject implements Header {
    /** Oggetto del messaggio */
    private final String subject;

    /*
     * RI:  subject != null
     *
     * AF:  AF(subject) = Subject: subject // dove subject è l'oggetto del messaggio
     */

    /**
     * Costruisce un intestazione di tipo Subject.
     *
     * @param subject l'oggetto del messaggio.
     * @throws NullPointerException se {@code subject} è {@code null}.
     */
    public Subject(final String subject) {
        this.subject = Objects.requireNonNull(subject);
    }

    /**
     * Crea un sequence a partire da una sequenza possibilmente codificata in
     * base64.
     *
     * @param sequence la sequenza.
     * @return l'intestazione di tipo Subject.
     * @throws NullPointerException se {@code sequence} è {@code null}.
     */
    public static Subject parse(final ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence);
        String decoded = Base64Encoding.decodeWord(sequence);
        if (Objects.isNull(decoded))
            return new Subject(sequence.toString());
        return new Subject(decoded);
    }

    @Override
    public String type() {
        return "Subject";
    }

    @Override
    public Object value() {
        return subject;
    }

    @Override
    public String toString() {
        if (ASCIICharSequence.isAscii(subject))
            return subject;
        return Base64Encoding.encodeWord(subject);
    }
}

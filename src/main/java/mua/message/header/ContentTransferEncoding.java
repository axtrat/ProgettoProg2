package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * ContentTransferEncoding è una classe immutabile che rappresenta un'intestazione di tipo Content-Transfer-Encoding
 * <p>
 * Un'istanza di ContentTransferEncoding contiene il tipo di codifica del messaggio.
 */
public class ContentTransferEncoding implements Intestazione {

    /** Tipo di codifica */
    private final String encoding;

    /**
     * Costruisce un'intestazione di tipo ContentTransferEncoding.
     *
     * @param encoding il tipo di codifica del messaggio.
     * @throws NullPointerException se {@code encoding} è null.
     * @throws IllegalArgumentException se {@code encoding} è vuoto.
     */
    public ContentTransferEncoding(final String encoding) {
        this.encoding = Objects.requireNonNull(encoding, "L'encoding non può essere null");
        if (encoding.isEmpty())
            throw new IllegalArgumentException("L'encoding non può essere vuoto");
    }

    /**
     * Crea l'intestazione ContentTransferEncoding a partire da una sequenza ascii
     * @param sequence sequenza che contiene l'encoding
     * @return Un'istanza di ContentTransferEncoding
     */
    public static ContentTransferEncoding parse (final ASCIICharSequence sequence) {
        return new ContentTransferEncoding(Objects.requireNonNull(sequence).toString());
    }

    @Override
    public String tipo() {
        return "Content-Transfer-Encoding";
    }

    @Override
    public String valore() {
        return encoding;
    }

    @Override
    public String toString() {
        return encoding;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ContentTransferEncoding that)
            return  Objects.equals(encoding, that.encoding);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(encoding);
    }
}

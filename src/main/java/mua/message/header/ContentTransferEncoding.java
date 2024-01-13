package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * ContentTransferEncoding è una classe immutabile che rappresenta un'intestazione di tipo Content-Transfer-Encoding
 * <p>
 * Un'istanza di ContentTransferEncoding contiene le informazioni sul tipo di codifica del messaggio.
 */
public class ContentTransferEncoding implements Header {
    /** Tipo di codifica */
    private final ASCIICharSequence encoding;

    /*
     * RI:  encoding != null, non vuoto
     * AF:
     */


    /**
     * Costruisce un'intestazione di tipo ContentTransferEncoding.
     *
     * @param encoding il tipo di codifica del messaggio.
     */
    private ContentTransferEncoding(final ASCIICharSequence encoding) {
        this.encoding = Objects.requireNonNull(encoding, "encoding non può essere null");
        if (encoding.isEmpty())
            throw new IllegalArgumentException("encoding non può essere vuoto");
    }

    /**
     * Crea l'intestazione ContentTransferEncoding a partire da una sequenza ascii
     * @param sequence sequenza che contiene l'encoding
     * @return Un'istanza di ContentTransferEncoding
     * @throws NullPointerException se {@code sequence} è null
     * @throws IllegalArgumentException se {@code sequence} è vuota
     */
    public static ContentTransferEncoding parse (final ASCIICharSequence sequence) {
        return new ContentTransferEncoding(Objects.requireNonNull(sequence, "encoding non può essere null"));
    }

    @Override
    public String type() {
        return "Content-Transfer-Encoding";
    }

    @Override
    public ASCIICharSequence value() {
        return encoding;
    }

    @Override
    public String toString() {
        return encoding.toString();
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

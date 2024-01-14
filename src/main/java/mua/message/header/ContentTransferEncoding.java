package mua.message.header;

import java.util.Objects;

import utils.ASCIICharSequence;

/**
 * ContentTransferEncoding è una classe immutabile che rappresenta un'intestazione di tipo Content-Transfer-Encoding
 * <p>
 * Un'istanza di ContentTransferEncoding contiene le informazioni sul tipo di codifica del messaggio.
 */
public record ContentTransferEncoding(String encoding) implements Header {

    /*
     * RI:  encoding != null, non vuoto, solo caratteri ASCII
     * 
     * AF:  AF(encoding) = Content-Transfer-Encoding: encoding // specifica il tipo di codifica del messaggio
     */


    /**
     * Costruisce un'intestazione di tipo ContentTransferEncoding.
     *
     * @param encoding il tipo di codifica del messaggio.
     * @throws NullPointerException se {@code encoding} è null.
     * @throws IllegalArgumentException se encoding è vuoto.
     * @throws IllegalArgumentException se encoding contiene caratteri non ASCII.
     */
    public ContentTransferEncoding {
        encoding = Objects.requireNonNull(encoding, "encoding non può essere null");
        if (encoding.isEmpty())
            throw new IllegalArgumentException("encoding non può essere vuoto");
        if (!ASCIICharSequence.isAscii(encoding))
            throw new IllegalArgumentException("encoding può contenere solo caratteri ASCII");
    }

    /**
     * Crea l'intestazione ContentTransferEncoding a partire da una sequenza che lo rappresenta secondo lo standard RFC
     * @param sequence la sequenza ASCII
     * @return Un'istanza di ContentTransferEncoding
     * @throws NullPointerException se {@code sequence} è null
     * @throws IllegalArgumentException se la decodifica fallisce
     */
    public static ContentTransferEncoding parse (final ASCIICharSequence sequence) {
        return new ContentTransferEncoding(Objects.requireNonNull(sequence, "la sequenza non può essere null").toString());
    }

    @Override
    public String type() {
        return "Content-Transfer-Encoding";
    }

    @Override
    public String value() {
        return encoding;
    }

    @Override
    public String toString() {
        return value();
    }
}

package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo MIME-Version.
 * <p>
 * Un'istanza di Mime contiene la versione del MIME.
 */
public class Mime implements Header {
    /** Versione MIME 1.0 */
    public static final Mime MIME_1_0 = new Mime(1, 0);
    
    /** Versione MIME */
    private final int majorVersion, minorVersion;

    /*
     * RI:  majorVersion, minorVersion sono composti da 1 cifra
     * 
     * AF:  AF(majorVersion, minorVersion) = MIME-Version: majorVersion.minorVersion //rappresenta la versione del MIME
     */

    /**
     * Costruisce un'intestazione di tipo MIME-Version.
     *
     * @param majorVersion versione major
     * @param minorVersion versione minor
     * @throws NullPointerException se version è null.
     * @throws IllegalArgumentException majorVersion o minorVersion non sono composti da più di una cifra
     */
    private Mime(int majorVersion, int minorVersion) {
        if (majorVersion < 0 || majorVersion > 9)
            throw new IllegalArgumentException("La versione major deve essere compresa tra 0 e 9");
        if (minorVersion < 0 || minorVersion > 9)
            throw new IllegalArgumentException("La versione minor deve essere compresa tra 0 e 9");
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    /**
     * Crea l'intestazione MIME-Version a partire da una sequenza
     * <p>
     * La sequenza deve essere composta da due numeri da una cifra separati da un punto
     *
     * @param sequence sequenza che contiene la versione
     * @return Un'istanza di MIME-Version
     * @throws NullPointerException se {@code sequence} è null
     */
    public static Mime parse(ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence, "La sequenza non può essere null");
        String[] versions = sequence.toString().split("\\.");
        if (versions.length != 2)
            throw new IllegalArgumentException("La sequenza deve essere composta da due numeri separati da un punto");
        return new Mime(Integer.parseInt(versions[0]), Integer.parseInt(versions[1]));
    }

    @Override
    public String type() {
        return "MIME-Version";
    }

    @Override
    public String value() {
        return majorVersion + "." + minorVersion;
    }

    @Override
    public String toString() {
        return majorVersion + "." + minorVersion;
    }
}

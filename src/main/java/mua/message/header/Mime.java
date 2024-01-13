package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

/**
 * Classe immutabile che rappresenta un'intestazione di tipo MIME-Version.
 * <p>
 * Un'istanza di Mime contiene la versione del MIME.
 */
public class Mime implements Header {
    
    /** Versione MIME */
    private final String version;

    /**
     * Costruisce un'intestazione di tipo MIME-Version.
     *
     * @param version la versione del MIME.
     * @throws NullPointerException se version è null.
     */
    public Mime(String version) {
        this.version = Objects.requireNonNull(version);
    }

    /**
     * Crea l'intestazione MIME-Version a partire da una sequenza
     *
     * @param sequence sequenza che contiene la versione
     * @return Un'istanza di MIME-Version
     * @throws NullPointerException se {@code sequence} è null
     */
    public static Mime parse(ASCIICharSequence sequence) {
        return new Mime(Objects.requireNonNull(sequence).toString());
    }

    @Override
    public String type() {
        return "MIME-Version";
    }

    @Override
    public String value() {
        return version;
    }

    @Override
    public String toString() {
        return version;
    }
}

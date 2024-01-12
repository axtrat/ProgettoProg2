package mua.message;

import mua.message.header.ContentTransferEncoding;
import mua.message.header.Intestazione;
import utils.Base64Encoding;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Classe immutabile che rappresenta la parte di un messaggio
 * <p>
 * Una parte è composta da una o più intestazioni e da un corpo non vuoto
 */
public class Parte implements Iterable<Intestazione> {
    /** Intestazioni della parte >= 1*/
    private final List<Intestazione> intestazioni;
    /** Corpo della parte */
    private final String corpo;

    /*
     * RI:  intestazioni, corpo != null, intestazioni e corpo non vuoti
     *      intestazioni deve contenere almeno un elemento e non può contenere null
     * 
     * AF:
     */

    /**
     * Costruisce un'istanza di Parte a partire dalle sue {@code intestazioni} e dal suo {@code corpo}
     * @param intestazioni intestazioni della parte
     * @param corpo corpo della parte
     * @throws NullPointerException se {@code intestazioni} è null, o contiene null
     * @throws NullPointerException se {@code corpo} è null
     * @throws IllegalArgumentException se {@code intestazioni.size() < 1} o {@code corpo} è vuoto
     */
    public Parte(final List<Intestazione> intestazioni, final String corpo) {
        this.intestazioni = List.copyOf(intestazioni);
        this.corpo = Objects.requireNonNull(corpo, "Il corpo non può essere null");
        
        if (intestazioni.isEmpty())
            throw new IllegalArgumentException("La parte deve avere almeno un'intestazione");
        if (corpo.isEmpty())
            throw new IllegalArgumentException("Il corpo non può essere vuoto");
    }

    /**
     * Restituisce il corpo della parte non codificato
     *
     * @return il corpo della parte
     */
    public String corpo() {
        return corpo;
    }

    @Override
    public Iterator<Intestazione> iterator() {
        return intestazioni.iterator();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n");
        String coded = corpo;
        for (Intestazione intestazione : intestazioni) {
            sj.add(intestazione.tipo() + ": " + intestazione);
            if (intestazione instanceof ContentTransferEncoding)
                coded = Base64Encoding.encode(corpo);
        }

        sj.add("\n");
        sj.add(coded);

        return sj.toString();
    }
}

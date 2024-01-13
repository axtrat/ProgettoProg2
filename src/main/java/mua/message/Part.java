package mua.message;

import mua.message.header.ContentTransferEncoding;
import mua.message.header.Header;
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
public class Part implements Iterable<Header> {
    /** Intestazioni della parte >= 1*/
    private final List<Header> headers;
    /** Corpo della parte */
    private final String body;

    /*
     * RI:  headers, body != null e non vuoti
     *      headers deve contenere almeno un elemento e non può contenere null
     * 
     * AF:
     */

    /**
     * Costruisce un'istanza di Part a partire dalle sue intestazioni e dal suo corpo
     * @param headers intestazioni della parte
     * @param body corpo/contenuto della parte
     * @throws NullPointerException se {@code headers} è null, o contiene null
     * @throws NullPointerException se {@code body} è null
     * @throws IllegalArgumentException se {@code headers.size() < 1} o {@code body} è vuoto
     */
    public Part(final List<Header> headers, final String body) {
        this.headers = List.copyOf(headers);
        this.body = Objects.requireNonNull(body, "Il body non può essere null");
        
        if (headers.isEmpty())
            throw new IllegalArgumentException("La parte deve avere almeno un'intestazione");
        if (body.isEmpty())
            throw new IllegalArgumentException("Il body non può essere vuoto");
    }

    /**
     * Restituisce il corpo della parte non codificato
     *
     * @return il corpo della parte
     */
    public String body() {
        return body;
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.iterator();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n");
        String coded = body;
        for (Header header : headers) {
            sj.add(header.type() + ": " + header);
            if (header instanceof ContentTransferEncoding)
                coded = Base64Encoding.encode(body);
        }

        sj.add("\n");
        sj.add(coded);

        return sj.toString();
    }
}

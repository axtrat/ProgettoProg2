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
 */
public class Parte implements Iterable<Intestazione> {
    private final List<Intestazione> intestazioni;
    private final String corpo;

    public Parte(final List<Intestazione> intestazione, final String corpo) {
        this.intestazioni = List.copyOf(intestazione);
        this.corpo = Objects.requireNonNull(corpo);
    }

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

package mua.message;

import mua.message.header.*;
import utils.ASCIICharSequence;
import utils.Base64Encoding;
import utils.EntryEncoding;
import utils.Fragment;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Classe immutabile che rappresenta un messaggio email
 * <p>
 * Un messaggio di posta
 */
public class Messaggio implements Iterable<Parte>, Comparable<Messaggio> {
    private final List<Intestazione> intestazioni = new ArrayList<>();
    private final List<Parte> parti;

    public Messaggio(final List<Parte> parti) {
        this.parti = List.copyOf(parti);

        parti.get(0).forEach(this.intestazioni::add);
    }

    public static Messaggio parse(final ASCIICharSequence sequence) {
        final HeaderParser parser = new HeaderParser();
        final List<Parte> parti = new ArrayList<>();
        for (Fragment fragment : EntryEncoding.decode(sequence)) {
            List<Intestazione> intestazioni = new ArrayList<>();
            String corpo = fragment.rawBody().toString();
            for (List<ASCIICharSequence> rawHeader : fragment.rawHeaders()) {
                Intestazione i = parser.parse(rawHeader.get(0), rawHeader.get(1));
                if (Objects.nonNull(i)) intestazioni.add(i);
            }
            if (intestazioni.contains(new ContentTransferEncoding("base64")))
                corpo = Base64Encoding.decode(ASCIICharSequence.of(corpo));
            parti.add(new Parte(intestazioni, corpo));
        }
        return new Messaggio(parti);
    }

    private ZonedDateTime getData() {
        return (ZonedDateTime) intestazioni.get(3).valore();
    }

    @Override
    public int compareTo(final Messaggio o) {
        int res = this.getData().compareTo(o.getData());
        if (res != 0) return res;

        for (int i = 0; i < intestazioni.size(); i++) {
            res = this.intestazioni.get(i).toString().compareTo(o.intestazioni.get(i).toString());
            if (res != 0) return res;
        }
        return 0;
    }

    @Override
    public Iterator<Parte> iterator() {
        return List.copyOf(parti).iterator();
    }

    public Iterator<Intestazione> intestazioni() {
        return intestazioni.iterator();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n--frontier\n");
        for (final Parte parte : parti) sj.add(parte.toString());
        return sj+((parti.size() > 1) ? "\n--frontier--": "");
    }
}

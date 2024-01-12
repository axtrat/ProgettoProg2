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
 * Un messaggio è costituito da una o più parti;
 * ciascuna parte comprende alcune intestazioni (come ad esempio l'oggetto, il mittente, i destinatari…) e un corpo che è il suo vero e proprio contenuto.
 */
public class Messaggio implements Iterable<Parte>, Comparable<Messaggio> {
    /** Lista di intestazioni (copiata dalla prima parte) >= 4 */
    private final List<Intestazione> intestazioni = new ArrayList<>();
    /** Lista di parti >= 1 */
    private final List<Parte> parti;

    /*
     * RI:  intestazioni != null && intestazioni.size() >= 4 && 
     *      parti != null && parti.size() >= 1
     * 
     * AF:
     */

    /**
     * Costruisce un'istanza di Messaggio a partire dalle sue {@code parti}
     * @param parti parti del messaggio
     * @throws NullPointerException se {@code parti} è null
     * @throws IllegalArgumentException se {@code parti.isEmpty()} o {@code intestazioni.size() < 4}
     */
    public Messaggio(final List<Parte> parti) {
        this.parti = List.copyOf(parti);
        parti.get(0).forEach(this.intestazioni::add);
        if (intestazioni.size() < 4)
            throw new IllegalArgumentException("Il messaggio deve avere almeno 4 intestazioni");
        if (parti.isEmpty())
            throw new IllegalArgumentException("Il messaggio deve avere almeno una parte");
    }

    /**
     * Costruisce un'istanza di Messaggio a partire dalla sua {@code sequence}
     * <p>
     * La sequenza deve essere codificata secondo lo standard RFC
     * @param sequence sequenza di caratteri ASCII che rappresenta il messaggio codificato
     * @return il messaggio decodificato
     * @throws NullPointerException se {@code sequence} è null
     * @throws IllegalArgumentException se {@code sequence} non è codificata secondo lo standard RFC
     */
    public static Messaggio parse(final ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence, "La sequenza non può essere null");
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

    /**
     * Restituisce la data del messaggio
     * @return la data del messaggio
     */
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

    /**
     * Restituisce le intestazioni principali del messaggio
     * @return le intestazioni principali del messaggio
     */
    public Iterator<Intestazione> intestazioni() {
        return intestazioni.iterator();
    }

    @Override
    public Iterator<Parte> iterator() {
        return List.copyOf(parti).iterator();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n--frontier\n");
        for (final Parte parte : parti) sj.add(parte.toString());
        return sj+((parti.size() > 1) ? "\n--frontier--": "");
    }
}

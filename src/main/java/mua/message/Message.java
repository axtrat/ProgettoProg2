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
 * Ciascuna parte comprende alcune intestazioni (come ad esempio l'oggetto, il mittente, i destinatari…) e un corpo che è il suo vero e proprio contenuto.
 */
public class Message implements Iterable<Part>, Comparable<Message> {
    /** Lista di intestazioni (copiata dalla prima parte) >= 4 */
    private final List<Header> intestazioni = new ArrayList<>();
    /** Lista di parti >= 1 */
    private final List<Part> parti;

    /*
     * RI:  intestazioni != null && intestazioni.size() >= 4 &&
     *      parti != null && parti.size() >= 1
     * 
     * AF:
     */

    /**
     * Costruisce un'istanza di Message a partire dalle sue {@code parti}
     * @param parti parti del messaggio
     * @throws NullPointerException se {@code parti} è null
     * @throws IllegalArgumentException se {@code parti.isEmpty()} o {@code intestazioni.size() < 4}
     */
    public Message(final List<Part> parti) {
        this.parti = List.copyOf(parti);
        parti.get(0).forEach(this.intestazioni::add);
        if (intestazioni.size() < 4)
            throw new IllegalArgumentException("Il messaggio deve avere almeno 4 intestazioni");
        if (parti.isEmpty())
            throw new IllegalArgumentException("Il messaggio deve avere almeno una parte");
    }

    /**
     * Decodifica un'istanza di Message a partire sequenza ASCII
     * <p>
     * La sequenza deve essere codificata secondo lo standard RFC
     * @param sequence sequenza di caratteri ASCII che rappresenta il messaggio codificato
     * @return il messaggio decodificato
     * @throws NullPointerException se {@code sequence} è null
     * @throws IllegalArgumentException se {@code sequence} non è codificata secondo lo standard RFC
     */
    public static Message parse(final ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence, "La sequenza non può essere null");
        final HeaderParser parser = new HeaderParser();
        final List<Part> parti = new ArrayList<>();
        for (Fragment fragment : EntryEncoding.decode(sequence)) {
            List<Header> intestazioni = new ArrayList<>();
            String corpo = fragment.rawBody().toString();
            for (List<ASCIICharSequence> rawHeader : fragment.rawHeaders()) {
                Header i = parser.parse(rawHeader.get(0), rawHeader.get(1));
                if (Objects.nonNull(i)) intestazioni.add(i);
            }
            if (intestazioni.contains(ContentTransferEncoding.parse(ASCIICharSequence.of("base64"))))
                corpo = Base64Encoding.decode(ASCIICharSequence.of(corpo));
            parti.add(new Part(intestazioni, corpo));
        }
        return new Message(parti);
    }

    /**
     * Restituisce la data del messaggio
     * @return la data del messaggio
     */
    private ZonedDateTime getData() {
        return (ZonedDateTime) intestazioni.get(3).value();
    }

    @Override
    public int compareTo(final Message o) {
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
    public Iterator<Header> intestazioni() {
        return intestazioni.iterator();
    }

    @Override
    public Iterator<Part> iterator() {
        return List.copyOf(parti).iterator();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n--frontier\n");
        for (final Part parte : parti) sj.add(parte.toString());
        return sj+((parti.size() > 1) ? "\n--frontier--": "");
    }
}

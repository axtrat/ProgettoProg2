package mua.message;

import mua.message.header.*;
import utils.ASCIICharSequence;
import utils.Base64Encoding;
import utils.EntryEncoding;
import utils.Fragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Classe immutabile che rappresenta un messaggio email 
 * <p>
 * Un messaggio è costituito da una o più parti {@link Part};
 * <p>
 * È possibile accedere alle intestazioni pricipali del messaggio tramite i metodi:
 * <ul>
 *  <li>{@link #sender()}</li>
 *  <li>{@link #recipient()}</li>
 *  <li>{@link #subject()}</li>
 *  <li>{@link #date()}</li>
 * </ul>
 * <p>
 * Il messaggio è iterabile sulle sue parti
 * <p>
 * Il messaggio è ordinabile in base alla data, nel caso in cui due messaggi abbiano la stessa data vengono presi in considerazione mittente, destinatario e oggetto
 */
public class Message implements Iterable<Part>, Comparable<Message> {
    /** Lista di parti >= 1 */
    private final List<Part> parti;
    // Intestazioni principali del messaggio (copiate dalla prima parte)
    /* Mittente del messaggio */
    private Sender sender;
    /* Destinatario/i del messaggio */
    private Recipient recipient;
    /* Oggetto del messaggio */
    private Subject subject;
    /* Data di invio del messaggio */
    private Date date;

    /*
     * RI:  parti != null && parti.size >= 1
     *      sender, recipient, subject, date sono copie delle intestazioni principali del messaggio
     *      sender, recipient, subject, date != null -> parti.get(0) le deve contenere
     * 
     * AF:  AF(parti) = { parte in parti | parte è una parte del messaggio }
     */

    /**
     * Costruisce un'istanza di Message a partire dalle sue {@code parti}
     * @param parti parti del messaggio
     * @throws NullPointerException se {@code parti} è null
     * @throws IllegalArgumentException se {@code parti.isEmpty()} o {@code intestazioni.size() < 4}
     */
    public Message(final List<Part> parti) {
        this.parti = List.copyOf(parti);
        
        if (parti.isEmpty())
            throw new IllegalArgumentException("Il messaggio deve avere almeno una parte");
        
        for (Header header: parti.get(0)) {
            if (header instanceof Sender sender)
                this.sender = sender;
            else if (header instanceof Recipient recipient)
                this.recipient = recipient;
            else if (header instanceof Subject subject)
                this.subject = subject;
            else if (header instanceof Date date)
                this.date = date;
        }

        Objects.requireNonNull(sender, "Il messaggio deve avere un mittente");
        Objects.requireNonNull(recipient, "Il messaggio deve avere un destinatario");
        Objects.requireNonNull(subject, "Il messaggio deve avere un oggetto");
        Objects.requireNonNull(date, "Il messaggio deve avere una data");
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
     * Restituisce il mittente del messaggio
     * @return il mittente del messaggio
     */
    public Sender sender() {
        return sender;
    }

    /**
     * Restituisce il destinatario del messaggio
     * @return il destinatario del messaggio
     */
    public Recipient recipient() {
        return recipient;
    }

    /**
     * Restituisce l'oggetto del messaggio
     * @return l'oggetto del messaggio
     */
    public Subject subject() {
        return subject;
    }

    /**
     * Restituisce la data del messaggio
     * @return la data del messaggio
     */
    public Date date() {
        return date;
    }

    @Override
    public int compareTo(final Message o) {
        int res = this.date.value().compareTo(o.date.value());
        if (res != 0) return res;

        res = this.sender.toString().compareTo(o.sender.toString());
        if (res != 0) return res;

        res = this.recipient.toString().compareTo(o.recipient.toString());
        if (res != 0) return res;

        res = this.subject.toString().compareTo(o.subject.toString());
        return 0;
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

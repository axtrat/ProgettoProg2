package mua;

import java.util.*;

import mua.message.Message;

/**
 * MailBox classe concreta mutabile che rappresenta una collection di messaggi
 * <p>
 * 
 * La MailBox è identificate dal suo nome ed è iterabile sui messaggi in ordine decrescente
 */
public class MailBox implements Iterable<Message> {
    /** Nome della MailBox */
    private final String nome;
    /** Collezione di messaggi */
    private final Collection<Message> messaggi = new TreeSet<>(Comparator.reverseOrder());

    /*
     * RI:  nome, messaggi != null
     *      messaggi non contiene null
     *      per ogni messaggio m1, m2 in messaggi m1.compareTo(m2) == 0 sse m1==m2 // messaggi non contiene duplicati
     * 
     * AF:  AF(nome, messaggi) = nome mailBox, { messaggo in messaggi | messaggio }
     *                          
     */

    /**
     * Costruisce una MailBox di nome {@code nome}
     *
     * @param nome il nome della MailBox
     * @throws NullPointerException se {@code nome} è {@code null}
     */
    public MailBox(String nome) {
        this.nome = Objects.requireNonNull(nome);
    }

    /**
     * Ritorna il numero di messaggi all'interno della MailBox
     *
     * @return il numero di messaggi all'interno della MailBox
     */
    public int size() {
        return messaggi.size();
    }

    /**
     * Ritorna {@code true} se non ci sono messaggi
     *
     * @return {@code true} se non ci sono messaggi
     */
    public boolean isEmpty() {
        return messaggi.isEmpty();
    }

    /**
     * @param i indice del messaggio
     * @return il messaggio scelto
     * @throws IndexOutOfBoundsException se l'indice è maggiore o uguale al numero di messaggi
     */
    public Message getMessage(int i) {
        int j = 0;
        for (Message message : messaggi)
            if (i == j++)
                return message;
        throw new IndexOutOfBoundsException();
    }

    /**
     * Ritorna il {@code nome} della MailBox
     *
     * @return il {@code nome} della MailBox
     */
    public String name() {
        return nome;
    }

    /**
     * Aggiunge il message {@code message} alla MailBox
     * @param message da aggiungere
     */
    public void addMessage(Message message) {
        messaggi.add(Objects.requireNonNull(message));
    }

    /**
     * Rimuove il messaggio di indice {@code n}
     *
     * @param n indice del messaggio da rimuovere
     */
    public void removeMessage(int n) {
        messaggi.remove(getMessage(n));
    }

    @Override
    public Iterator<Message> iterator() {
        return Collections.unmodifiableCollection(messaggi).iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MailBox mbox)
            return nome.equals(mbox.nome);
        return false;
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    @Override
    public String toString() {
        return nome;
    }
}

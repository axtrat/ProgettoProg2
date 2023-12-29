package mua;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import mua.message.Messaggio;

/**
 * MailBox classe concreta mutabile che rappresenta una collezzione di messaggi
 */
public class MailBox implements Iterable<Messaggio> {

    /*
     *
     */

    private String nome = "INBOX";
    private Set<Messaggio> messaggi = new TreeSet<>();

    /**
     * Costruisce una MailBox di nome {@code nome}
     * @param nome il nome della MailBox
     * @throws NullPointerExeption se nome è null
     */
    public MailBox(String nome) {
        this.nome = Objects.requireNonNull(nome); 
    }

    /**
     * Ritorna il numero di messaggi all'interno della MailBox
     * @return il numero di messaggi all'interno della MailBox
     */
    public int size() {
        return messaggi.size();
    }

    /**
     * Ritorna {@code true} se non ci sono messaggi
     * @return {@code true} se non ci sono messaggi
     */
    public boolean isEmpty() {
        return messaggi.isEmpty();
    }
    
    /**
     * 
     * @param i
     * @return il messaggio scelto
     * @throws IndexOutOfBoundsException se l'indice è fuori dal range
     */
    public Messaggio getMessage(int i) {
        int j = 0;
        for (Messaggio messaggio : messaggi)
            if (i == j++) return messaggio;
        throw new IndexOutOfBoundsException();
    }

    /**
     * 
     * @param messaggio
     */
    public void addMessage(Messaggio messaggio) {
        messaggi.add(Objects.requireNonNull(messaggio));
    }

    @Override
    public Iterator<Messaggio> iterator() {
        return Set.copyOf(messaggi).iterator();
    }

}
package mua;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TreeMap;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.*;
import utils.ASCIICharSequence;
import utils.Storage;
import utils.UICard;
import utils.UITable;
import utils.Storage.Box;
import utils.Storage.Box.Entry;


public class Mua {
    /** Lista di mailboxes */
    private final List<MailBox> mBoxes = new ArrayList<>();
    /** Mailbox selezionata */
    private MailBox selected;
    /** Corrispondenza tra Mailbox in memoria e su disco */
    private final Map<MailBox, Box> boxMap = new HashMap<>();
    /** Corrispondenza tra Messaggio in memoria e su disco */
    private final Map<Messaggio, Entry> entryMap = new TreeMap<>();

    public Mua(String baseDir) {
        for (Box box : new Storage(baseDir).boxes()) {
            MailBox mBox = new MailBox(box.toString());
            boxMap.put(mBox, box);
            for (Box.Entry entry : box.entries()) {
                Messaggio messaggio = Messaggio.parse(entry.content());
                entryMap.put(messaggio, entry);
                mBox.addMessage(messaggio);
            }
            mBoxes.add(mBox);
        }
        mBoxes.sort((mb1, mb2) -> mb1.toString().compareTo(mb2.toString()));
    }

    public String listMailboxes() {
        List<List<String>> content = new ArrayList<>();
        for (MailBox mailBox : mBoxes)
            content.add(List.of(mailBox.name(), String.valueOf(mailBox.size())));

        return UITable.table(List.of("Mailbox", "# messages"), content, true, false);
    }

    /**
     * Seleziona la mailbox di indice {@code n} e ne restituisce il nome.
     * @param n l'indice della mailbox da selezionare
     * @return il nome della mailbox selezionata
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di mailbox
     */
    public String selectMailbox(int n) {
        selected = mBoxes.get(n);
        return selected.name();
    }

    /**
     * Aggiunge il messaggio {@code messaggio} alla mailbox selezionata.
     * @param messaggio il messaggio da aggiungere
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws NullPointerException se {@code messaggio} è {@code null}
     */
    public void addMessage(Messaggio messaggio) {
        if (Objects.isNull(selected))
            throw new IllegalStateException("Nessuna mailbox selezionata");
        entryMap.put(messaggio, boxMap.get(selected).entry(ASCIICharSequence.of(Objects.requireNonNull(messaggio).toString())));
        selected.addMessage(messaggio);
    }

    /**
     * Restituisce una tabella contenente i messaggi della mailbox selezionata.
     * @return la stringa che rappresenta la tabella
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     */
    public String listMessages() {
        if (Objects.isNull(selected))
            throw new IllegalStateException("Nessuna mailbox selezionata");
        List<List<String>> content = new ArrayList<>();
        for (Messaggio message : selected) {
            LinkedList<String> row = new LinkedList<>();
            Iterator<Intestazione> it = message.intestazioni();
            row.add(((Indirizzo) it.next().valore()).getEmail());
            StringJoiner sj = new StringJoiner("\n");
            for (Indirizzo indirizzo : ((Destinatario) it.next()).valore())
                sj.add(indirizzo.getEmail());
            row.add(sj.toString()); // To
            row.add(it.next().valore().toString()); // Subject
            ZonedDateTime data = (ZonedDateTime) it.next().valore();
            row.addFirst(data.toLocalDate() + "\n" + data.toLocalTime()); // Date
            content.add(row);
        }

        return UITable.table(List.of("Date", "From", "To", "Subject"), content, true, true);
    }

    /**
     * Elimina il messaggio di indice {@cose n} dalla mailbox selezionata.
     * @param n l'indice del messaggio da eliminare
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di messaggi contenuti nella mailbox
     */
    public void deleteMessage(int n) {
        if (Objects.isNull(selected))
            throw new IllegalStateException("Nessuna mailbox selezionata");
        entryMap.get(selected.getMessage(n)).delete();
        selected.removeMessage(n);
    }

    /**
     * Restitusce una stringa che rappresenta il messaggio di indice {@code n} della mailbox selezionata.
     * <p>
     * Il messaggio è rappresentato come una card di intestazioni e corpi del messaggio.
     * @param n l'indice del messaggio da leggere
     * @return la stringa che rappresenta il messaggio
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di messaggi contenuti nella mailbox
     */
    public String readMessage(int n) {
        if (Objects.isNull(selected))
            throw new IllegalStateException("Nessuna mailbox selezionata");
        Messaggio messaggio = selected.getMessage(n);
        List<String> headers = new ArrayList<>(), values = new ArrayList<>();

        for (Parte parte : messaggio) {
            for (Intestazione intestazione : parte) {
                switch (intestazione.tipo()) {
                    case "From", "Subject", "Date" -> {
                        headers.add(intestazione.tipo());
                        values.add(intestazione.valore().toString());
                    }
                    case "To" -> {
                        headers.add(intestazione.tipo());
                        StringJoiner sj = new StringJoiner("\n");
                        for (Indirizzo indirizzo : ((Destinatario) intestazione).valore())
                            sj.add(indirizzo.toString());
                        values.add(sj.toString());
                    }
                    case "Content-Type" -> {
                        headers.add("Part\n" + intestazione.valore());
                        values.add(parte.corpo());
                    }
                    default -> {
                    }
                }
            }
        }

        return UICard.card(headers, values);
    }
}

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

import mua.message.Message;
import mua.message.Part;
import mua.message.header.*;
import utils.ASCIICharSequence;
import utils.Storage;
import utils.UICard;
import utils.UITable;
import utils.Storage.Box;
import utils.Storage.Box.Entry;

/**
 * Classe che gestisce un Mail User Agent (MUA)
 * <p>
 * Un MUA è un programma che permette di gestire le email.
 * <p>
 * Un MUA permette di:
 * <ul>
 *     <li>visualizzare le mailbox disponibili</li>
 *     <li>selezionare una mailbox</li>
 *     <li>aggiungere un messaggio alla mailbox selezionata</li>
 *     <li>visualizzare i messaggi della mailbox selezionata</li>
 *     <li>eliminare un messaggio dalla mailbox selezionata</li>
 *     <li>visualizzare un messaggio della mailbox selezionata</li>
 * </ul>
 */
public class Mua {
    /** Lista di mailboxes */
    private final List<MailBox> mBoxes = new ArrayList<>();
    /** Mailbox selezionata */
    private MailBox selected;
    /** Corrispondenza tra Mailbox in memoria e su disco */
    private final Map<MailBox, Box> boxMap = new HashMap<>();
    /** Corrispondenza tra Message in memoria e su disco */
    private final Map<Message, Entry> entryMap = new TreeMap<>();

    /*
     * RI:  mBoxes, boxMap, entryMap != null e non contengono null
     *      ad ogni MailBox in mBoxes corrisponde una Entry in boxMap e viceversa
     *      ad ogni Message corrisponde una Entry in entryMap e viceversa
     * 
     * AF:  
     */

    /**
     * Costruisce un'istanza di Mua a partire dalla {@code directory} in cui sono contenute le mailbox.
     * <p> Legge le mailbox contenute nella {@code directory} e le carica in memoria.
     * @param directory directory che contiene le mailbox
     * @throws NullPointerException se {@code directory} è {@code null}
     * @throws IllegalStateException se è presente un messaggio corrotto o non ben formato
     */
    public Mua(String directory) {
        for (Box box : new Storage(Objects.requireNonNull(directory)).boxes()) {
            MailBox mBox = new MailBox(box.toString());
            boxMap.put(mBox, box);
            for (Box.Entry entry : box.entries()) {
                Message message;
                try {
                    message = Message.parse(entry.content());
                } catch (Exception exception) {
                    throw new IllegalStateException(
                        "Il message: " + entry.toString() + " non è codificato secondo lo standard RFC:\n" +
                        exception.getMessage()
                    );
                }
                entryMap.put(message, entry);
                mBox.addMessage(message);
            }
            mBoxes.add(mBox);
        }
        mBoxes.sort((mb1, mb2) -> mb1.toString().compareTo(mb2.toString()));
    }

    /**
     * Restituisce una tabella contenente le mailbox disponibili.
     * @return la stringa che rappresenta la tabella
     */
    public String listMailboxes() {
        List<List<String>> content = new ArrayList<>();
        for (MailBox mailBox : mBoxes)
            content.add(List.of(mailBox.name(), String.valueOf(mailBox.size())));

        return UITable.table(List.of("Mailbox", "# messages"), content, true, false);
    }

    /**
     * Restituisce il numero di mailbox disponibili.
     * @return il numero di mailbox disponibili
     */
    public int mailBoxes() {
        return mBoxes.size();
    }

    /**
     * Seleziona la mailbox di indice {@code n} e ne restituisce il nome.
     * @param n l'indice della mailbox da selezionare
     * @return il nome della mailbox selezionata
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di mailbox
     */
    public String selectMailbox(int n) {
        if (n >= mBoxes.size())
            throw new IndexOutOfBoundsException("Indice maggiore del numero di mailbox");
        selected = mBoxes.get(n);
        return selected.name();
    }

    /**
     * Controlla che sia stata precedentemente selezionata una mailbox.
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     */
    private void checkSelected() {
        if (Objects.isNull(selected))
            throw new IllegalStateException("Nessuna mailbox selezionata");
    }

    /**
     * Restituisce il numero di messaggi contenuti nella mailbox selezionata.
     * @return il numero di messaggi contenuti nella mailbox selezionata
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     */
    public int mailBoxMessages() {
        checkSelected();
        return selected.size();
    }

    /**
     * Aggiunge il message {@code message} alla mailbox selezionata.
     * @param message il message da aggiungere
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws NullPointerException se {@code message} è {@code null}
     */
    public void addMessage(Message message) {
        checkSelected();
        entryMap.put(message, boxMap.get(selected).entry(ASCIICharSequence.of(Objects.requireNonNull(message).toString())));
        selected.addMessage(message);
    }

    /**
     * Restituisce una tabella contenente i messaggi della mailbox selezionata.
     * @return la stringa che rappresenta la tabella
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     */
    public String listMessages() {
        checkSelected();
        List<List<String>> content = new ArrayList<>();
        for (Message message : selected) {
            LinkedList<String> row = new LinkedList<>();
            row.add((message.sender().value()).getEmail());
            StringJoiner sj = new StringJoiner("\n");
            for (Address address : message.recipient().value())
                sj.add(address.getEmail());
            row.add(sj.toString()); // To
            row.add(message.subject().value().toString()); // Subject
            ZonedDateTime data = message.date().value();
            row.addFirst(data.toLocalDate() + "\n" + data.toLocalTime()); // Date
            content.add(row);
        }

        return UITable.table(List.of("Date", "From", "To", "Subject"), content, true, true);
    }

    /**
     * Controlla che l'indice {@code index} sia valido per la mailbox selezionata.
     * @param index l'indice da controllare
     * @throws IndexOutOfBoundsException se {@code index} supera il numero di messaggi contenuti nella mailbox
     */
    private void checkIndex(int index) {
        if (index >= selected.size())
            throw new IndexOutOfBoundsException(
                "Indice: " + index + 
                "maggiore del numero di messaggi nella mailbox selezionata: " + selected.size()
            );
    }

    /**
     * Elimina il messaggio di indice {@code n} dalla mailbox selezionata.
     * @param n l'indice del messaggio da eliminare
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di messaggi contenuti nella mailbox
     */
    public void deleteMessage(int n) {
        checkSelected();
        checkIndex(n);
        entryMap.get(selected.getMessage(n)).delete();
        selected.removeMessage(n);
    }

    /**
     * Restituisce una stringa che rappresenta il messaggio di indice {@code n} della mailbox selezionata.
     * <p>
     * Il messaggio è rappresentato come una card di intestazioni e corpi del messaggio.
     * @param n l'indice del messaggio da leggere
     * @return la stringa che rappresenta il messaggio
     * @throws IllegalStateException se non è stata precedentemente selezionata una mailbox
     * @throws IndexOutOfBoundsException se {@code n} supera il numero di messaggi contenuti nella mailbox
     */
    public String readMessage(int n) {
        checkSelected();
        checkIndex(n);
        Message message = selected.getMessage(n);
        List<String> headers = new ArrayList<>(), values = new ArrayList<>();

        for (Part parte : message) {
            for (Header header : parte) {
                switch (header.type()) {
                    case "From", "Subject", "Date" -> {
                        headers.add(header.type());
                        values.add(header.value().toString());
                    }
                    case "To" -> {
                        headers.add(header.type());
                        StringJoiner sj = new StringJoiner("\n");
                        for (Address address : ((Recipient) header).value())
                            sj.add(address.toString());
                        values.add(sj.toString());
                    }
                    case "Content-Type" -> {
                        headers.add("Part\n" + header.value());
                        values.add(parte.body());
                    }
                    default -> {
                    }
                }
            }
        }

        return UICard.card(headers, values);
    }
}

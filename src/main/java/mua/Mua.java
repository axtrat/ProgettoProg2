package mua;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    
    private final List<MailBox> mBoxes = new ArrayList<>();
    private final Map<MailBox, Box> boxMap = new HashMap<>();
    private final Map<Messaggio, Entry> entryMap = new TreeMap<>();
    private MailBox selected;

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

    public void deleteMessage(int n) {
        entryMap.get(selected.getMessage(n)).delete();
        selected.removeMessage(n);
    }

    public void addMessage(Messaggio messaggio) {
        Entry newEntry = boxMap.get(selected).entry(ASCIICharSequence.of(messaggio.toString()));
        entryMap.put(messaggio, newEntry);
        selected.addMessage(messaggio);
    }

    public String listMailboxes() {
        List<List<String>> content = new ArrayList<>();
        for (MailBox mailBox : mBoxes)
            content.add(List.of(mailBox.name(), String.valueOf(mailBox.size())));

        return UITable.table(List.of("Mailbox", "# messages"), content, true, false);
    }

    public String selectMailbox(int n) {
        selected = mBoxes.get(n);
        return selected.name();
    }

    public String listMessages() {
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

    public String readMessage(int n) {
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

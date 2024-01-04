package mua;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.*;
import utils.*;
import utils.Storage.Box;

/**
 * The application class
 */
public class App {

    private final List<MailBox> mBoxes = new ArrayList<>();
    private MailBox selected;

  /** Inizializza un'istanza */
  public App(String baseDir) {
    for (Box box : new Storage(baseDir).boxes()) {
      MailBox mBox = new MailBox(box.toString());
      for (Box.Entry entry : box.entries())
        mBox.addMessage(Messaggio.parse(entry.content().toString()));
      mBoxes.add(mBox);
    }
  }

    /**
     * Runs the REPL.
     *
     * <p>
     * Develop here the REPL, see the README.md for more details.
     *
     * @param args the first argument is the mailbox base directory.
     */
    public static void main(String[] args) {
        App mua = new App("tests/mbox");
        String nomeBox = "*";
        try (UIInteract ui = UIInteract.getInstance()) {
            for (; ; ) {
                String[] input = ui.command("[%s] > ", nomeBox);
                if (Objects.isNull(input)) break;
                int n = (input.length > 1) ? Integer.parseInt(input[1]) - 1 : 0;
                switch (input[0]) {
                    case "LSM" -> System.out.println(mua.listMailboxes());
                    case "MBOX" -> nomeBox = mua.selectMailbox(n);
                    case "LSE" -> System.out.println(mua.listMessages());
                    case "READ" -> ui.output(mua.readMessage(n));
                    case "DELETE" -> mua.deleteMessage(n);
                    case "COMPOSE" -> {
                        StringJoiner sj = new StringJoiner("\n");
                        String line;
                        sj.add(ui.line("From: "));
                        sj.add(ui.line("To: "));
                        sj.add(ui.line("Subject: "));
                        sj.add(ui.line("Date: "));

                        ui.output("Text Body (. to end):");
                        do sj.add((line = ui.line())); while (!line.equals("."));

                        ui.output("Html Body (. to end):");
                        do sj.add((line = ui.line())); while (!line.equals("."));

                        mua.compose(sj.toString());
                    }
                    default -> ui.error("Unknown command: " + input[0]);
                }
            }
        } catch (IOException ignored) {
        }
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

    public void deleteMessage(int n) {
        selected.removeMessage(n);
    }

    public void compose(String string) {
        List<Intestazione> intestazioni = new ArrayList<>();
        List<Parte> parti = new ArrayList<>();
        try (Scanner sc = new Scanner(string)) {
            intestazioni.add(new Mittente(Indirizzo.parse(sc.nextLine())));
            intestazioni.add(Destinatario.parse(sc.nextLine()));
            intestazioni.add(new Oggetto(sc.nextLine()));
            intestazioni.add(Data.parse(sc.nextLine()));

            Map<String, String> corpi = new HashMap<>();
            StringJoiner sj = new StringJoiner("\n");
            String corpo;
            while (!(corpo = sc.nextLine()).equals(".")) sj.add(corpo);
            if (!(corpo = sj.toString()).isEmpty())
                corpi.put("plain", corpo);

            while (!(corpo = sc.nextLine()).equals(".")) sj.add(corpo);
            if (!(corpo = sj.toString()).isEmpty())
                corpi.put("html", corpo);

            if (corpi.size() > 1) {
                intestazioni.add(new Mime("1.0"));
                intestazioni.add(ContentType.parse("multipart/alternative; boundary=frontier"));
                parti.add(new Parte(intestazioni, "This is a message with multiple parts in MIME format."));
                intestazioni.clear();
            }

            for (String key: corpi.keySet()) {
                if (key.equals("plain") && ASCIICharSequence.isAscii(corpi.get(key))) {
                    intestazioni.add(ContentType.parse("text/plain; charset=\"us-ascii\""));
                } else {
                    intestazioni.add(ContentType.parse("text/" + key + "; charset=\"utf-8\""));
                    intestazioni.add(new ContentTransferEncoding("base64"));
                }
                parti.add(new Parte(intestazioni, corpi.get(key)));
                intestazioni.clear();
            }
        }
        selected.addMessage(new Messaggio(parti));
    }
}

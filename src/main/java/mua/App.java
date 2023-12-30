package mua;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;
import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.ContentType;
import mua.message.header.Data;
import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import mua.message.header.Mittente;
import mua.message.header.Oggetto;
import utils.Storage;
import utils.Storage.Box;
import utils.Storage.Box.Entry;
import utils.UICard;
import utils.UIInteract;
import utils.UITable;

/** The application class */
public class App {

  private final List<MailBox> mBoxes = new ArrayList<>();
  private MailBox selected;

  /** Inizializza un'istanza */
  public App() {
    for (Box box : new Storage("tests/mbox").boxes()) {
      MailBox mBox = new MailBox(box.toString());
      for (Entry entry : box.entries())
        mBox.addMessage(Messaggio.parse(entry.content().toString()));
      mBoxes.add(mBox);
    }
  }

  public String listMailboxes() {
    List<List<String>> content = new ArrayList<>();
    for (MailBox mailBox : mBoxes)
      content.add(List.of(mailBox.name(), String.valueOf(mailBox.size())));

    return UITable.table(List.of("Mailbox", "# messages"), content, true, false);
  }

  public void selectMailbox(int n) {
    selected = mBoxes.get(n);
  }

  public String listMessages() {
    List<List<String>> content = new ArrayList<>();
    for (Messaggio message : selected) {
      LinkedList<String> row = new LinkedList<>();
      Iterator<Intestazione> it = message.intestazioni();
      row.add(((Indirizzo) it.next().valore()).getEmail());
      StringJoiner sj = new StringJoiner("\n");
      for (Indirizzo indirizzo : (List<Indirizzo>) it.next().valore()) sj.add(indirizzo.getEmail());
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
    Iterator<Parte> it = messaggio.iterator();
    Parte parte = it.next();
    List<String> headers = new ArrayList<>(), values = new ArrayList<>();

    for (Intestazione intestazione : (Iterable<Intestazione>) messaggio::intestazioni) {
      headers.add(intestazione.tipo());
      if (intestazione instanceof Destinatario destinatario)
        values.add(
            destinatario.valore().stream()
                .map(Indirizzo::toString)
                .reduce((a, b) -> a + "\n" + b)
                .get());
      else values.add(intestazione.valore().toString());
    }

    if (it.hasNext()) {
      headers.add("Part\nmultipart/alternative");
      values.add("This is a message with multiple parts in MIME format.");
    }

    headers.add("Part\n" + parte.intestazione().valore().get(0));
    values.add(parte.corpo());

    for (Parte p : (Iterable<Parte>) () -> it) {
      headers.add("Part\n" + p.intestazione().valore().get(0));
      values.add(p.corpo());
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
      StringJoiner sj = new StringJoiner("\n");
      String corpo;

      while (!(corpo = sc.nextLine()).equals(".")) sj.add(corpo);
      if (!sj.toString().isBlank())
        parti.add(
            new Parte(
                new ContentType("text", "plain", Map.of("charset", "\"us-ascii\"")),
                sj.toString()));

      sj = new StringJoiner("\n");
      while (!(corpo = sc.nextLine()).equals(".")) sj.add(corpo);
      if (!sj.toString().isBlank())
        parti.add(
            new Parte(
                new ContentType("text", "html", Map.of("charset", "\"us-ascii\"")), sj.toString()));
    }
    selected.addMessage(new Messaggio(intestazioni, parti));
  }

  /**
   * Runs the REPL.
   *
   * <p>Develop here the REPL, see the README.md for more details.
   *
   * @param args the first argument is the mailbox base directory.
   */
  public static void main(String[] args) {
    App mua = new App();
    try (UIInteract ui = UIInteract.getInstance()) {
      for (; ; ) {
        String[] input = ui.command("> ");
        if (Objects.isNull(input)) break;
        int n = (input.length > 1) ? Integer.parseInt(input[1]) - 1 : 0;
        switch (input[0]) {
          case "LSM" -> System.out.println(mua.listMailboxes());
          case "MBOX" -> mua.selectMailbox(n);
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
            do sj.add((line = ui.line()));
            while (!line.equals("."));

            ui.output("Html Body (. to end):");
            do sj.add((line = ui.line()));
            while (!line.equals("."));

            mua.compose(sj.toString());
          }
          default -> ui.error("Unknown command: " + input[0]);
        }
      }
    } catch (IOException ignored) {
    }
  }
}

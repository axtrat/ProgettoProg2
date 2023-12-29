package mua;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import utils.Storage;
import utils.Storage.Box;
import utils.Storage.Box.Entry;
import utils.UICard;
import utils.UIInteract;

/** The application class */
public class App {

    private final List<MailBox> mBoxes = new ArrayList<>();
    private MailBox selected;

    public App() {
        for (Box box : new Storage("tests/mbox").boxes()) {
            MailBox mBox = new MailBox(box.toString());
            for (Entry entry : box.entries())
                mBox.addMessage(Messaggio.parse(entry.content().toString()));
            mBoxes.add(mBox);
        }
    }

    public void selectMailbox(int n) {
        selected = mBoxes.get(n);
    }

    public String readMessage(int n) {
        Messaggio messaggio = selected.getMessage(n);
        Iterator<Parte> it = messaggio.iterator();
        Parte parte = it.next();
        List<String> headers = new ArrayList<>(), values = new ArrayList<>();

        for (Intestazione intestazione : (Iterable<Intestazione>) () -> messaggio.intestazioni()) {
            headers.add(intestazione.tipo());
            if (intestazione instanceof Destinatario destinatario) {
                values.add(destinatario.valore().stream().map(Indirizzo::toString).reduce((a, b) -> a + "\n" + b).get());
            } else
                values.add(intestazione.valore().toString());
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
  
  /** Runs the REPL.
   * 
   * <p>Develop here the REPL, see the README.md for more details.
   *
   * @param args the first argument is the mailbox base directory.
   */
  public static void main(String[] args) {
    /*
    try (UIInteract ui = UIInteract.getInstance()) {
            for (;;) {
                String[] input = ui.command("> ");
                if (input == null) break;
                switch (input[0]) {
                    case "LS":          ui.output("You requested an ls...");
                    break;
                    case "CD":          ui.output("You requested a cd...");
                    break;
                    default: 
                    ui.error("Unknown command: " + input[0]);
                    break;
                }
            }
        }
         */
  }

}

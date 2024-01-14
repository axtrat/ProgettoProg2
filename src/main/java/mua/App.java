package mua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

import mua.message.Message;
import mua.message.Part;
import mua.message.header.*;
import mua.message.header.Recipient;
import utils.ASCIICharSequence;
import utils.UIInteract;

/**
 * The application class
 */
public class App {

    /**
     * Runs the REPL.
     *
     * <p>
     * Develop here the REPL, see the README.md for more details.
     *
     * @param args the first argument is the mailbox base directory.
     */
    public static void main(String[] args) {
        Mua mua = new Mua(args[0]);
        String nomeBox = "*";
        try (UIInteract ui = UIInteract.getInstance()) {
            for (;;) {
                String[] input = ui.command("[%s] > ", nomeBox);
                if (Objects.isNull(input))
                    break;
                int n = (input.length > 1) ? Integer.parseInt(input[1]) - 1 : 0;
                switch (input[0]) {
                    case "LSM": 
                        ui.output(mua.listMailboxes());
                        break;
                    case "MBOX":
                        if (n < mua.mailBoxes()) nomeBox = mua.selectMailbox(n);
                        else ui.error("Inserire un indice valido: [1-" + mua.mailBoxes() + "]");
                        break;
                    case "EXIT": 
                        return;
                    default:
                        if (nomeBox.equals("*")) {
                            ui.error("Selezionare una mailbox");
                            break;
                        }
                        switch (input[0]) {
                            case "LSE": 
                                ui.output(mua.listMessages());
                                break;
                            case "READ": 
                                if (n < mua.mailBoxMessages()) ui.output(mua.readMessage(n));
                                else ui.error("Inserire un indice valido: [1-" + mua.mailBoxMessages() + "]");
                                break;
                            case "DELETE": 
                                if (n < mua.mailBoxMessages()) mua.deleteMessage(n);
                                else ui.error("Inserire un indice valido: [1-" + mua.mailBoxMessages() + "]");
                                break;
                            case "COMPOSE":
                                StringJoiner sj = new StringJoiner("\n");
                                String line;
                                sj.add(ui.line("From: "));
                                sj.add(ui.line("To: "));
                                sj.add(ui.line("Subject: "));
                                sj.add(ui.line("Date: "));
        
                                ui.prompt("Text Body (. to end):");
                                do
                                    sj.add((line = ui.line()));
                                while (!line.equals("."));
        
                                ui.prompt("Html Body (. to end):");
                                do
                                    sj.add((line = ui.line()));
                                while (!line.equals("."));

                                try {
                                    mua.addMessage(compose(sj.toString()));
                                } catch (Exception e) {
                                    throw e;
                                    //ui.error(e.getMessage());
                                }
                                break;
                            default: ui.error("Unknown command: " + input[0]);
                        }
                    break;
                }
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Compone un messaggio a partire dall'{@code input} utente
     * <p>
     * Se l'{@code input} non è valido, viene lanciata un'eccezione
     * @param input l'input utente 
     * @return il messaggio composto se l'{@code input} è valido
     * @throws IllegalArgumentException se l'{@code input} non è valido
     */
    private static Message compose(String input) {
        List<Header> intestazioni = new ArrayList<>();
        List<Part> parti = new ArrayList<>();
        try (Scanner sc = new Scanner(input)) {
            intestazioni.add(Sender.parse(ASCIICharSequence.of(sc.nextLine())));
            intestazioni.add(Recipient.parse(ASCIICharSequence.of(sc.nextLine())));
            intestazioni.add(new Subject(sc.nextLine()));
            intestazioni.add(Date.parse(ASCIICharSequence.of(sc.nextLine())));

            Map<String, String> corpi = new HashMap<>();

            for (String type : new String[]{"plain", "html"}) {
                StringJoiner sj = new StringJoiner("\n");
                String corpo;
                while (!(corpo = sc.nextLine()).equals("."))
                    sj.add(corpo);
                if (!(corpo = sj.toString()).isEmpty())
                    corpi.put(type, corpo);
            }

            if (corpi.size() > 1) {
                intestazioni.add(Mime.MIME_1_0);
                intestazioni.add(ContentType.parse(ASCIICharSequence.of("multipart/alternative; boundary=frontier")));
                parti.add(new Part(intestazioni, "This is a message with multiple parts in MIME format."));
                intestazioni.clear();
            }

            for (String key : corpi.keySet()) {
                if (key.equals("plain") && ASCIICharSequence.isAscii(corpi.get(key))) {
                    intestazioni.add(ContentType.parse(ASCIICharSequence.of("text/plain; charset=\"us-ascii\"")));
                } else {
                    intestazioni.add(ContentType.parse(ASCIICharSequence.of("text/" + key + "; charset=\"utf-8\"")));
                    intestazioni.add(ContentTransferEncoding.parse(ASCIICharSequence.of("base64")));
                }
                parti.add(new Part(intestazioni, corpi.get(key)));
                intestazioni.clear();
            }
        }
        return new Message(parti);
    }
}

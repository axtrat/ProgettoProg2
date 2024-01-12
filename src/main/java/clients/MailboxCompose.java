package clients;

import mua.App;
import mua.Mua;
import utils.UIInteract;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * MailboxCompose
 */
public class MailboxCompose {

    /**
     * Tests message composition and deletion
     *
     * <p>Runs the app on the commands in the stdin, the commands are limited to: MBOX, COMPOSE, READ,
     * DELETE.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Mua app = new Mua("tests/mbox");
        try (UIInteract ui = UIInteract.getInstance()) {
            for (; ; ) {
                String[] input = ui.command("> ");
                if (input == null) break;
                int n = (input.length > 1) ? Integer.parseInt(input[1]) - 1 : 0;
                switch (input[0]) {
                    case "MBOX" -> app.selectMailbox(n);
                    case "READ" -> ui.output(app.readMessage(n));
                    case "DELETE" -> app.deleteMessage(n);
                    case "COMPOSE" -> {
                        StringJoiner sj = new StringJoiner("\n");
                        String line;
                        sj.add(ui.line());
                        sj.add(ui.line());
                        sj.add(ui.line());
                        sj.add(ui.line());

                        for (int i = 0; i < 2; i++)
                            do sj.add((line = ui.line())); while (!line.equals("."));

                        app.addMessage(App.compose(sj.toString()));
                    }
                }
            }
        } catch (IOException ignored) {}
    }
}

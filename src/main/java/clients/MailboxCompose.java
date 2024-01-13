package clients;

import mua.App;

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
        App.main(new String[] {"tests/mbox"});
    }
}

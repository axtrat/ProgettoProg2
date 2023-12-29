package clients;

import java.util.Scanner;

import mua.App;

/** MailboxRead */
public class MailboxRead {
    /**
     * Tests message reading
     *
     * <p>
     * Runs the app on the commands in the stdin, the commands are limited to: MBOX,
     * READ.
     *
     * @param args not used
     */

    public static void main(String[] args) {
        App app = new App();
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String[] input = sc.nextLine().split(" ");
                int n = Integer.parseInt(input[1]) - 1;
                switch (input[0]) {
                    case "mbox" -> app.selectMailbox(n);
                    case "read" -> System.out.println(app.readMessage(n));
                }
            }
        }
    }
}
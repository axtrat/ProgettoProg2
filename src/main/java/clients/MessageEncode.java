package clients;

import mua.message.Message;
import mua.message.Part;
import mua.message.header.*;
import utils.ASCIICharSequence;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * MessageEncode
 */
public class MessageEncode {

    public static final ZonedDateTime DATE =
            ZonedDateTime.of(2023, 12, 6, 12, 30, 20, 200, ZoneId.of("Europe/Rome"));

    /**
     * Tests message encoding
     *
     * <p>Reads a message from stdin and emits its encoding on the stdout.
     *
     * <p>The stdin contains:
     *
     * <ul>
     *   <li>the sender address (three lines, see {@link AddressDecode}),
     *   <li>two recipient addresses (three lines each, as above),
     *   <li>the subject (one line),
     *   <li>the text part (one line, possibly empty),
     *   <li>the HTML part (one line, possibly empty).
     * </ul>
     * <p>
     * To such information, the program adds the date corresponding to {@link #DATE}.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        List<Header> intestazioni = new ArrayList<>();
        List<Part> parti = new ArrayList<>();
        try (Scanner s = new Scanner(System.in)) {
            String nome = s.nextLine();
            String locale = s.nextLine();
            String dominio = s.nextLine();
            intestazioni.add(new Sender(new Address(nome, locale, dominio)));
            List<Address> indirizzi = new ArrayList<>();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("Oggetto")) {
                    intestazioni.add(new Recipient(indirizzi));
                    intestazioni.add(new Subject(line));
                    intestazioni.add(new Date(DATE));
                    break;
                }
                locale = s.nextLine();
                dominio = s.nextLine();
                indirizzi.add(new Address(line, locale, dominio));
            }

            Map<String, String> corpi = new HashMap<>();
            String corpo;

            for (String type : new String[]{"plain", "html"})
                if (!(corpo = s.nextLine()).isEmpty())
                    corpi.put(type, corpo);
            
            if (corpi.size() > 1) {
                intestazioni.add(Mime.parse(ASCIICharSequence.of("1.0")));
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
        System.out.println(new Message(parti));
    }
}

package clients;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.*;
import utils.ASCIICharSequence;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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
        List<Intestazione> intestazioni = new ArrayList<>();
        List<Parte> parti = new ArrayList<>();
        try (Scanner s = new Scanner(System.in)) {
            String nome = s.nextLine();
            String locale = s.nextLine();
            String dominio = s.nextLine();
            intestazioni.add(new Mittente(new Indirizzo(nome, locale, dominio)));
            List<Indirizzo> indirizzi = new ArrayList<>();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("Oggetto")) {
                    intestazioni.add(new Destinatario(indirizzi));
                    intestazioni.add(new Oggetto(line));
                    intestazioni.add(new Data(DATE));
                    break;
                }
                locale = s.nextLine();
                dominio = s.nextLine();
                indirizzi.add(new Indirizzo(line, locale, dominio));
            }
            String line = s.nextLine();
            List<String> corpi = new ArrayList<>();
            while (!line.equals(".") && s.hasNextLine()) {
                String newLine = s.nextLine();
                if (line.startsWith("Versione")) corpi.add(line);
                line = newLine;
            }
            if (corpi.size() > 1) {
                intestazioni.add(new Mime("1.0"));
                intestazioni.add(ContentType.parse("multipart/alternative; boundary=frontier"));
                parti.add(new Parte(intestazioni, "This is a message with multiple parts in MIME format."));
                intestazioni.clear();
            }
            for (String corpo : corpi) {
                parti.add(creaParte(intestazioni, corpo));
            }
        }
        System.out.println(new Messaggio(parti));
    }

    private static Parte creaParte(List<Intestazione> intestazioni, String line) {
        Parte parte;
        if (contieneHTML(line)) {
            intestazioni.add(ContentType.parse("text/html; charset=\"utf-8\""));
            intestazioni.add(new ContentTransferEncoding("base64"));
            parte = new Parte(intestazioni, line);
        } else if (!ASCIICharSequence.isAscii(line)) {
            intestazioni.add(ContentType.parse("text/plain; charset=\"utf-8\""));
            intestazioni.add(new ContentTransferEncoding("base64"));
            parte = new Parte(intestazioni, line);
        } else {
            intestazioni.add(ContentType.parse("text/plain; charset=\"us-ascii\""));
            parte = new Parte(intestazioni, line);
        }
        intestazioni.clear();
        return parte;
    }

    private static boolean contieneHTML(String input) {
        Pattern pattern = Pattern.compile("<[^>]*>");
        return pattern.matcher(input).find();
    }
}

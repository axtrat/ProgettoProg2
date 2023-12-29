package clients;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.*;
import utils.ASCIICharSequence;

/** MessageEncode */
public class MessageEncode {

    public static final ZonedDateTime DATE = ZonedDateTime.of(2023, 12, 6, 12, 30, 20, 200, ZoneId.of("Europe/Rome"));

    /**
     * Tests message encoding
     *
     * <p>
     * Reads a message from stdin and emits its encoding on the stdout.
     *
     * <p>
     * The stdin contains:
     *
     * <ul>
     * <li>the sender address (three lines, see {@link AddressDecode}),
     * <li>two recipient addresses (three lines each, as above),
     * <li>the subject (one line),
     * <li>the text part (one line, possibly empty),
     * <li>the HTML part (one line, possibly empty).
     * </ul>
     *
     * To such information, the program adds the date corresponding to
     * {@link #DATE}.
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
            parti.add(creaParte(s.nextLine()));
            String line = s.nextLine();
            while (!line.equals(".") && s.hasNextLine()) {
                String newLine = s.nextLine();
                if (line.startsWith("Versione"))
                    parti.add(creaParte(line));
                    line = newLine;
                line += newLine;
            }
        }
        System.out.println(new Messaggio(intestazioni, parti));
    }

    private static Parte creaParte(String line) {
        if (contieneHTML(line))
            return new Parte(new ContentType("text/html", "charset=\"utf-8\""), line);
        else if (ASCIICharSequence.isAscii(line))
            return new Parte(new ContentType("text/plain", "charset=\"us-ascii\""), line);
        else
            return new Parte(new ContentType("text/plain", "charset=\"utf-8\""), line);
    }

    private static boolean contieneHTML(String input) {
        Pattern pattern = Pattern.compile("<[^>]*>");

        return pattern.matcher(input).find();
    }

}

package mua.message;

import java.util.*;
import java.util.regex.Pattern;

import mua.message.header.Intestazione;
import utils.ASCIICharSequence;
import utils.Base64Encoding;

/**
 * Classe immutabile che rappresenta la parte iniziale di un messaggio
 */
public class Parte implements Iterable<Intestazione> {
    private final List<Intestazione> intestazioni = new ArrayList<>();
    private String corpo;

    public Parte(List<Intestazione> intestazioni, String corpo) {
        List.copyOf(Objects.requireNonNull(intestazioni)).forEach(
                (i) -> this.intestazioni.add(Objects.requireNonNull(i)));
        this.corpo = Objects.requireNonNull(corpo);
    }

    @Override
    public Iterator<Intestazione> iterator() {
        return intestazioni.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (contieneHTML(corpo)) {
            sb.append("Content-Type: text/html; charset=\"utf-8\"\n");
            sb.append("Content-Transfer-Encoding: base64\n\n");
            sb.append(Base64Encoding.encode(corpo));
        } else if (ASCIICharSequence.isAscii(corpo)) {
            sb.append("Content-Type: text/plain; charset=\"us-ascii\"\n\n");
            sb.append(corpo);
        } else {
            sb.append("Content-Type: text/plain; charset=\"utf-8\"\n");
            sb.append("Content-Transfer-Encoding: base64\n\n");
            sb.append(Base64Encoding.encode(corpo));
        }
        return sb.toString();
    }

    private static boolean contieneHTML(String input) {
        Pattern pattern = Pattern.compile("<[^>]*>");

        return pattern.matcher(input).find();
    }
}

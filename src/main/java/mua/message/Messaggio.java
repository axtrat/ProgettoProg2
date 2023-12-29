package mua.message;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

import mua.message.header.ContentType;
import mua.message.header.Data;
import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import mua.message.header.Mittente;
import mua.message.header.Oggetto;
import utils.ASCIICharSequence;
import utils.Base64Encoding;
import utils.DateEncoding;

public class Messaggio implements Iterable<Parte>, Comparable<Messaggio> {
    private final List<Intestazione> intestazioni;
    private final List<Parte> parti;

    public Messaggio(final List<Intestazione> intestazioni, final List<Parte> parti) {
        this.intestazioni = List.copyOf(intestazioni);
        this.parti = List.copyOf(parti);
    }

    public static Messaggio parse(final String string) {
        final List<Intestazione> intestazioni = new ArrayList<>();
        final List<Parte> parti = new ArrayList<>();
        try (Scanner s = new Scanner(string)) {
            intestazioni.add(new Mittente(Indirizzo.parse(s.nextLine().split(": ")[1])));
            intestazioni.add(Destinatario.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(Oggetto.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(new Data(DateEncoding.decode(ASCIICharSequence.of(s.nextLine().split(": ")[1]))));

            String line = s.nextLine();
            String separatore = "--";
            if (line.startsWith("MIME")) {
                separatore += s.nextLine().split("boundary=")[1]; //Content-Type
                s.nextLine(); // space
                s.nextLine(); // Messaggio
                s.nextLine(); // --frontier
                line = s.nextLine();
            }
            for (;;) {
                final ContentType contentType = ContentType.parse(line.split(": ")[1]);
                s.nextLine();

                final StringJoiner sj = new StringJoiner("\n");
                sj.add(s.nextLine());
                while (s.hasNextLine() && !(line = s.nextLine()).startsWith(separatore))
                    sj.add(line);
                String corpo = sj.toString();

                if (!contentType.isAscii())
                    corpo = Base64Encoding.decode(ASCIICharSequence.of(corpo));

                parti.add(new Parte(contentType, corpo));
                if (!s.hasNextLine() || line.equals(separatore + "--"))
                    break;
                line = s.nextLine();
            }

            return new Messaggio(intestazioni, parti);
        }
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n");
        for (final Intestazione intestazione : intestazioni)
            sj.add(intestazione.tipo() + ": " + intestazione);

        if (parti.size() == 1) {
            sj.add(parti.get(0).toString());
            return sj.toString();
        }

        sj.add("MIME-Version: 1.0");
        sj.add("Content-Type: multipart/alternative; boundary=frontier");
        sj.add("\nThis is a message with multiple parts in MIME format.");
        sj.add("--frontier");
        sj.add(parti.get(0).toString());
        sj.add("--frontier");
        for (final Parte p : parti.subList(1, parti.size())) {
            sj.add(p.toString());
            sj.add("--frontier");
        }
        return sj + "--";
    }

    private ZonedDateTime getData() {
        return (ZonedDateTime) intestazioni.get(3).valore();
    }

    @Override
    public int compareTo(final Messaggio o) {
        return o.getData().compareTo(this.getData());
    }

    @Override
    public Iterator<Parte> iterator() {
        return List.copyOf(parti).iterator();
    }

    public Iterator<Intestazione> intestazioni() {
        return intestazioni.iterator();
    }
}

package mua.message;

import mua.message.header.Data;
import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import mua.message.header.Mittente;
import mua.message.header.Oggetto;
import utils.ASCIICharSequence;
import utils.Base64Encoding;
import utils.DateEncoding;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class Messaggio implements Comparable<Messaggio>{
    private Parte parte;
    private List<Parte> parti;

    public Messaggio(List<Parte> parti) {
        this.parte = parti.get(0);
        this.parti = List.copyOf(parti.subList(1, parti.size()));
        this.parti.forEach(Objects::requireNonNull);
    }



    public static Messaggio parse(String string) {
        List<Intestazione> intestazioni = new ArrayList<>();
        List<Parte> parti = new ArrayList<>();
        try (Scanner s = new Scanner(new StringReader(string))) {
            intestazioni.add(new Mittente(Indirizzo.parse(s.nextLine().split(": ")[1])));
            intestazioni.add(Destinatario.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(Oggetto.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(new Data(DateEncoding.decode(ASCIICharSequence.of(s.nextLine().split(": ")[1]))));
            
            String line = s.nextLine();
            String separatore = "--";
            if (line.startsWith("MIME")) {
                //Salvi il MIME
                separatore += s.nextLine().split("boundary=")[1]; //Salvi il Content-Type
                s.nextLine(); //skip
                s.nextLine(); //intesazione
                s.nextLine(); //skip
                line = s.nextLine();
            }
            for (;;) {
                String contentType = line;
                s.nextLine();
                
                StringJoiner sj = new StringJoiner("\n");
                sj.add(s.nextLine());
                while (s.hasNextLine() && !(line = s.nextLine()).startsWith(separatore)) 
                    sj.add(line);
                String corpo = sj.toString();

                if (contentType.contains("utf-8") || contentType.contains("html"))
                    corpo = Base64Encoding.decode(ASCIICharSequence.of(corpo));

                parti.add(new Parte(intestazioni, corpo));
                intestazioni = List.of();
                if (!s.hasNextLine() || line.equals(separatore+"--")) break;
                line = s.nextLine();
            }

            return new Messaggio(parti);
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        for (Intestazione intestazione : parte)
            sj.add(intestazione.tipo() + ": " + intestazione);

        if (parti.isEmpty()) {
            sj.add(parte.toString());
            return sj.toString();
        }
        
        sj.add("MIME-Version: 1.0");
        sj.add("Content-Type: multipart/alternative; boundary=frontier");
        sj.add("\nThis is a message with multiple parts in MIME format.");
        sj.add("--frontier");
        sj.add(parte.toString());
        sj.add("--frontier");
        for (Parte p : parti) {
            sj.add(p.toString());
            sj.add("--frontier");
        }
        return sj.toString()+ "--";
    }

    private ZonedDateTime getData() {
        Iterator<Intestazione> it = parte.iterator();
        for (int i = 0; i < 3; i++) it.next();
        return (ZonedDateTime) it.next().valore();
    }

    @Override
    public int compareTo(Messaggio o) {
        return this.getData().compareTo(o.getData());
    }
}

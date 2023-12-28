package mua.message;

import mua.message.header.Intestazione;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Messaggio {

    private List<Parte> parti;

    public Messaggio(List<Parte> parti) {
        this.parti = List.copyOf(parti);
        this.parti.forEach(Objects::requireNonNull);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        Parte parte = parti.get(0);
        for (Intestazione intestazione : parte)
            sj.add(intestazione.tipo() + ": " + intestazione);

        if (parti.size() == 1) {
            sj.add(parte.toString());
            return sj.toString();
        }
        
        sj.add("MIME-Version: 1.0");
        sj.add("Content-Type: multipart/alternative; boundary=frontier");
        sj.add("\nThis is a message with multiple parts in MIME format.");
        sj.add("--frontier");
        sj.add(parte.toString());
        sj.add("--frontier");
        for (Parte p : parti.subList(1, parti.size())) {
            sj.add(p.toString());
            sj.add("--frontier");
        }
        return sj.toString()+ "--";
    }
}

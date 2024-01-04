package mua.message.header;

import utils.ASCIICharSequence;

import java.util.Objects;

public class Mime implements Intestazione {

    private final String version;

    public Mime(String version) {
        this.version = Objects.requireNonNull(version);
    }

    public static Mime parse(ASCIICharSequence sequence) {
        return new Mime(Objects.requireNonNull(sequence).toString());
    }

    @Override
    public String tipo() {
        return "MIME-Version";
    }

    @Override
    public String valore() {
        return version;
    }

    @Override
    public String toString() {
        return version;
    }
}

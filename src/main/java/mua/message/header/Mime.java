package mua.message.header;

import java.util.Objects;

public class Mime implements Intestazione {

    private final String version;

    public Mime(String version) {
        this.version = Objects.requireNonNull(version);
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

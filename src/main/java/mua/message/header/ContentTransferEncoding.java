package mua.message.header;

import java.util.Objects;

public class ContentTransferEncoding implements Intestazione {
    private final String encoding;

    public ContentTransferEncoding(String encoding) {
        this.encoding = Objects.requireNonNull(encoding);
    }

    @Override
    public String tipo() {
        return "Content-Transfer-Encoding";
    }

    @Override
    public String valore() {
        return encoding;
    }

    @Override
    public String toString() {
        return encoding;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContentTransferEncoding that)
            return  Objects.equals(encoding, that.encoding);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(encoding);
    }
}

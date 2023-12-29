package mua.message.header;

import java.util.List;
import java.util.Objects;

/**
 * ContentType è una classe immutabile che rappresenta un'intestazione di tipo ContentType.
 * <p>
 * Un'istanza di ContentType contiene le information sul tipo del contenuto.
 */
public class ContentType implements Intestazione {
    private final String[] value = new String[2];

    public ContentType(final String value1, final String value2) {
        value[0] = Objects.requireNonNull(value1);
        value[1] = Objects.requireNonNull(value2);
    }

    public static ContentType parse(final String s) {
        final String[] value = s.split("; ");
        return new ContentType(value[0], value[1]);
    }

    @Override
    public String tipo() {
        return "Content-Type";
    }

    @Override
    public List<String> valore() {
        return List.of(value);
    }

    public boolean isAscii() {
        return value[1].contains("ascii");
    }

    @Override
    public String toString() {
        return value[0] + "; " + value[1];
    }
}

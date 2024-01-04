package mua.message.header;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * ContentType è una classe immutabile che rappresenta un'intestazione di tipo ContentType.
 *
 * <p>Un'istanza di ContentType contiene le information sul tipo del contenuto.
 */
public class ContentType implements Intestazione {
  private final String type, subtype;
  private final Map<String, String> attributes;

  /**
   * Crea un'istanza di ContentType
   *
   * @param type
   * @param subtype
   * @param attribute
   */
  public ContentType(final String type, final String subtype, final Map<String, String> attribute) {
    this.type = Objects.requireNonNull(type);
    this.subtype = Objects.requireNonNull(subtype);
    this.attributes = Map.copyOf(attribute);
  }

  public static ContentType parse(final String s) {
    final String[] values = s.split("; ");
    final String[] types = values[0].split("/");
    final String[] attribute = values[1].split("=");
    return new ContentType(types[0], types[1], Map.of(attribute[0], attribute[1]));
  }

  @Override
  public String tipo() {
    return "Content-Type";
  }

  @Override
  public String valore() {
    return type + "/" + subtype;
  }

  public String get(String key) {
    return attributes.get(key);
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("; ");
    sj.add(type + "/" + subtype);
    attributes.forEach((key, value) -> sj.add(key + "=" + value));
    return sj.toString();
  }
}

package mua.message.header;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import utils.ASCIICharSequence;

/**
 * ContentType è una classe immutabile che rappresenta un'intestazione di tipo ContentType.
 *
 * <p>
 * Un'istanza di ContentType contiene le information sul tipo del contenuto.
 */
public class ContentType implements Header {
    /** SuperTipo e Tipo del contenuto */
    private final String type, subtype;
    /** Attributi */
    private final Map<String, String> attributes;

    /*
     * RI:  type, subtype, attributes != null
     *      attributes non contiene null
     *      type, subtype non vuoti
     *
     * AF: type/subtype; attribute=value...
     */

    /**
     * Crea un'istanza di ContentType
     *
     * @param type      super-tipo del contenuto
     * @param subtype   sotto-tipo del contenuto
     * @param attribute attributi aggiuntivi
     */
    public ContentType(final String type, final String subtype, final Map<String, String> attribute) {
        this.type = Objects.requireNonNull(type);
        this.subtype = Objects.requireNonNull(subtype);
        this.attributes = Map.copyOf(attribute);
    }
    
    /**
     * Crea un'istanza di ContentType a partire da una sequenza che lo rappresenta
     * @param sequence la sequenza che lo rappresenta
     * @return un'istanza di ContentType
     * @throws NullPointerException se la sequenza è null
     */
    public static ContentType parse(final ASCIICharSequence sequence) {
        final String[] values = Objects.requireNonNull(sequence).toString().split("; ");
        final String[] types = values[0].split("/");
        final Map<String, String> attributes = new HashMap<>();
        for (int i = 1; i < values.length; i++) {
            final String[] attribute = values[i].split("=");
            attributes.put(attribute[0], attribute[1]);
        }
        return new ContentType(types[0], types[1], attributes);
    }

    @Override
    public String type() {
        return "Content-Type";
    }

    @Override
    public String value() {
        return type + "/" + subtype;
    }

    /**
     * Restituisce il valore associato all'attributo specificato
     * @param attribute l'attributo a cui è associato il valore da restituire
     * @return il valore associato all'attributo se presente o {@code null} altrimenti
     */
    public String get(String attribute) {
        return attributes.get(attribute);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("; ");
        sj.add(type + "/" + subtype);
        attributes.forEach((key, value) -> sj.add(key + "=" + value));
        return sj.toString();
    }
}

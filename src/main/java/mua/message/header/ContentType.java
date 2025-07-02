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
 * Un'istanza di ContentType contiene le information sul contenuto:
 * <ul>
 *      <li>tipo/sottotipo</li>
 *      <li>attributi</li>
 * </ul>
 */
public class ContentType implements Header {
    /** SuperTipo e Tipo del contenuto */
    private final String type, subtype;
    /** Attributi */
    private final Map<String, String> attributes;

    /*
     * RI:  type, subtype, attributes != null 
     *      type, subtype non vuoti e solo caratteri ASCII
     *      per ogni attributo, chiave e valore sono: non null, non vuoti e solo ASCII
     *      attributes contiene almeno un attributo
     *
     * AF:  AF(type, subtype, attributes) = Content-Type: type/subtype {chiave=valore; | chiave è l'attributo e valore il suo valore}*
     * 
     */

    /**
     * Crea un'istanza di ContentType
     *
     * @param type      super-tipo del contenuto
     * @param subtype   sotto-tipo del contenuto
     * @param attribute attributi aggiuntivi
     * @throws NullPointerException se {@code type}, {@code subtype} o {@code attribute} sono {@code null}
     * @throws NullPointerException se {@code attribute} contiene chiavi o valori {@code null}
     * @throws IllegalArgumentException se {@code type} o {@code subtype} sono vuoti o contengono caratteri non ASCII
     * @throws IllegalArgumentException se {@code attribute} è vuoto
     * @throws IllegalArgumentException se {@code attribute} contiene chiavi e valori vuoti o con caratteri non ASCII
     */
    public ContentType(final String type, final String subtype, final Map<String, String> attribute) {
        this.type = Objects.requireNonNull(type);
        this.subtype = Objects.requireNonNull(subtype);
        this.attributes = Map.copyOf(attribute);

        if (type.isEmpty() || subtype.isEmpty())
            throw new IllegalArgumentException("type e subtype non possono essere vuoti");
        if (!ASCIICharSequence.isAscii(type) || !ASCIICharSequence.isAscii(subtype))
            throw new IllegalArgumentException("type e subtype possono contenere solo caratteri ASCII");
        if (attributes.isEmpty())
            throw new IllegalArgumentException("attributes non può essere vuoto");

        attributes.forEach((key, value) -> {
            if (key.isEmpty() || value.isEmpty())
                throw new IllegalArgumentException("key e value non possono essere vuoti");
            if (!ASCIICharSequence.isAscii(key) || !ASCIICharSequence.isAscii(value))
                throw new IllegalArgumentException("key e value possono contenere solo caratteri ASCII");
        });
    }
    
    /**
     * Crea un'istanza di ContentType a partire da una sequenza che lo rappresenta
     * @param sequence la sequenza ASCII
     * @return un'istanza di ContentType
     * @throws NullPointerException se la sequenza è null
     * @throws IllegalArgumentException se la decodifica fallisce
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

    /**
     * Restituisce il valore associato all'attributo specificato
     * @param attribute l'attributo a cui è associato il valore da restituire
     * @return il valore associato all'attributo se presente o {@code null} altrimenti
     */
    public String get(String attribute) {
        return attributes.get(attribute);
    }

    @Override
    public String type() {
        return "Content-Type";
    }

    @Override
    public String value() {
        return type + "/" + subtype;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("; ");
        sj.add(type + "/" + subtype);
        attributes.forEach((key, value) -> sj.add(key + "=" + value));
        return sj.toString();
    }
}

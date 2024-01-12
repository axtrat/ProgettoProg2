package mua.message.header;

import java.util.List;
import java.util.Objects;
import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutabile che rappresenta un indirizzo email valido: Composto da
 * {@code nome}, {@code
 * locale}, {@code dominio}
 */
public class Indirizzo {
    /** Nome dell'utente */
    private String nome = "";
    /** Locale dell'indirizzo */
    private final String locale;
    /** Dominio dell'indirizzo */
    private final String dominio;

    /*
     * RI:
     * nome, locale e dominio != null e possono contenere solo caratteri ASCII
     * locale e dominio non devono essere vuote e devono essere composte solo da
     * caratteri validi
     */

    /**
     * Crea un indirizzo a partire dal {@code nome} (<i>DisplayName</i>)
     * {@code locale} e {@code
     * dominio}: <i> nome {@literal <}locale@dominio{@literal >}</i>
     *
     * @param nome    il DisplayName associato alla mail
     * @param locale  il locale della mail: <i>{@code locale}@...</i>
     * @param dominio il dominio della mail: <i>...@{@code dominio}</i>
     * @throws NullPointerException     se {@code nome}, {@code locale} o
     *                                  {@code dominio} sono null
     * @throws IllegalArgumentException se {@code nome}, {@code locale} o
     *                                  {@code dominio} contengono
     *                                  caratteri non ASCII
     */
    public Indirizzo(final String nome, final String locale, final String dominio) {
        Objects.requireNonNull(nome);
        Objects.requireNonNull(locale);
        Objects.requireNonNull(dominio);

        if (!ASCIICharSequence.isAscii(nome))
            throw new IllegalArgumentException("Il nome può contenere solo caratteri ASCII");
        if (!ASCIICharSequence.isAscii(locale))
            throw new IllegalArgumentException("Il locale può contenere solo caratteri ASCII");
        if (!ASCIICharSequence.isAscii(dominio))
            throw new IllegalArgumentException("Il dominio può contenere solo caratteri ASCII");

        if (!AddressEncoding.isValidAddressPart(locale))
            throw new IllegalArgumentException("Il locale contiene caratteri non validi");
        if (!AddressEncoding.isValidAddressPart(dominio))
            throw new IllegalArgumentException("Il dominio contiene caratteri non validi");

        this.nome = nome.trim();
        this.locale = locale;
        this.dominio = dominio;
    }

    /**
     * Crea un indirizzo da una sequenza che lo rappresenta
     *
     * <p>
     * Un indirizzo valido è composto da {@code nome}, {@code locale},
     * {@code dominio}: <i>nome
     * {@literal <}locale@dominio{@literal >}</i>
     *
     * <p>
     * Il nome può essere omesso: <i>"locale@dominio"</i>, Nel caso comprenda più di
     * uno spazio va
     * racchiuso tra virgolette es: <i>"nome con spazi"
     * {@literal <}locale@dominio{@literal >}</i>
     *
     * @param sequence la sequenza che rappresenta l'indirizzo
     * @return l'indirizzo creato
     * @throws NullPointerException     se {@code sequence} è {@code null}
     * @throws IllegalArgumentException se {@code locale} o {@code dominio}
     *                                  contengono caratteri non
     *                                  validi
     */
    public static Indirizzo parse(final ASCIICharSequence sequence) {
        Objects.requireNonNull(sequence);

        final List<String> res = AddressEncoding.decode(sequence).get(0);
        return new Indirizzo(res.get(0), res.get(1), res.get(2));
    }

    /**
     * Ritorna il nome (<i>DisplayName</i>) associato all'indirizzo
     *
     * @return il DisplayName dell'indirizzo
     */
    public String nome() {
        return nome;
    }

    /**
     * Ritorna il {@code locale} dell'indirizzo: <i>locale@...</i>
     *
     * @return il {@code locale} dell'indirizzo
     */
    public String locale() {
        return locale;
    }

    /**
     * Ritorna il {@code dominio} dell'indirizzo: <i>...@dominio</i>
     *
     * @return il {@code dominio} dell'indirizzo
     */
    public String dominio() {
        return dominio;
    }

    /**
     * Ritorna l'indirizzo email completo composto da {@code locale} e
     * {@code dominio}
     *
     * @return l'indirizzo email completo: <i>locale@dominio</i>
     */
    public String getEmail() {
        return String.format("%s@%s", locale, dominio);
    }

    @Override
    public String toString() {
        if (nome.isEmpty())
            return getEmail();

        final StringBuilder sb = new StringBuilder();
        if (nome.split(" ").length <= 2)
            sb.append(nome);
        else
            sb.append('"').append(nome).append('"');

        sb.append(" <").append(getEmail()).append(">");

        return sb.toString();
    }
}

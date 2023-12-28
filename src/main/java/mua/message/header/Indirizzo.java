package mua.message.header;

import java.util.List;
import java.util.Objects;

import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutabile che rappresenzta un indirizzo email valido:
 * Composto da nome, locale e dominio
 */
public class Indirizzo {
    private String nome = "";
    private String locale;
    private String dominio;

    /*
     * RI:
     * nome, locale e dominio != null e possono contenere solo caratteri ASCII
     * locale e dominio non devono essere vuote e devono essere composte solo da
     * caratteri validi
     */

    
    /**
     * Crea un indirizzo a partire dal nome (DisplayName) locale e dominio:
     * {@literal nome <locale@dominio>}
     * <p>
     *
     * @param nome    il DisplayName associato alla mail
     * @param locale  il locale della mail "locale"@...
     * @param dominio il dominio della mail ...@"dominio"
     * @throws NullPointerException     se nome, locale o dominio sono null
     * @throws IllegalArgumentException se nome, locale o dominio contengono
     *                                  caratteri non ASCII
     * @throws IllegalArgumentException 
     */
    public Indirizzo(String nome, String locale, String dominio) {
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
     * Crea un indirizzo da una stringa che lo rappresenta
     * <p>
     * Un indirizzo valido è composto da nome, locale e dominio: {@literal "nome <locale@dominio>"}
     * <p>
     * Il nome può essere omesso: {@literal "locale@dominio"},
     * Nel caso comprenda più di uno spazio va racchiuso tra virgolette es: {@literal "nome con spazi" <locale@dominio>}
     * @param indirizzo la stringa che rappresenta l'indirizzo
     * @return l'indirizzo creato
     * @throws NullPointerException se indirizzo è null
     * @throws IllegalArgumentException se indirizzo contiene caratteri non ASCII
     * @throws IllegalArgumentException se locale o dominio contengono caratteri non validi
     */
    public static Indirizzo parse(String indirizzo) {
        Objects.requireNonNull(indirizzo);

        if (!ASCIICharSequence.isAscii(indirizzo))
            throw new IllegalArgumentException("L'indirizzo può contenere solo caratteri ASCII");

        List<String> res = AddressEncoding.decode(ASCIICharSequence.of(indirizzo)).get(0);
        return new Indirizzo(res.get(0), res.get(1), res.get(2));
    }

    /**
     * Ritorna il nome (DispalyName) associato all'indirizzo
     * @return il DisplayName dell'indirizzo
     */ 
    public String nome() {
        return nome;
    }

    /**
     * Ritorna il locale dell'indirizzo: "locale"@...
     * @return il locale dell'indirizzo
     */
    public String locale() {
        return locale;
    } 

    /**
     * Ritorna il dominio dell'indirizzo: ...@"dominio"
     * @return il dominio dell'indirizzo
     */
    public String dominio() {
        return dominio;
    }

    /**
     * Ritorna l'indirizzo email completo composto da locale e dominio
     * @return l'indirizzo email completo: locale@dominio
     */
    public String getEmail() {
        return String.format("%s@%s", locale, dominio);
    }


    @Override
    public String toString() {
        if (nome.isEmpty()) 
            return getEmail();
        
        StringBuilder sb = new StringBuilder();
        if (nome.split(" ").length <= 2)
            sb.append(nome);
        else
            sb.append('"').append(nome).append('"');

        sb.append(" <").append(getEmail()).append(">");

        return sb.toString();
    }
}

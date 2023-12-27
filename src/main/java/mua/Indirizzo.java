package mua;

import java.util.List;
import java.util.Objects;

import utils.ASCIICharSequence;
import utils.AddressEncoding;

/**
 * Classe immutoabile che rappresenzta un indirizzo email valido:
 * Composto da nome, locale@dominio
 *
 *
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
     * @throws IllegalArgumentException se locale o dominio contengono caratteri non
     *                                  validi
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
        if (nome.split(" ").length > 1) 
            sb.append('"').append(nome).append('"');
        else
            sb.append(nome);

        sb.append(" <").append(getEmail()).append(">");

        return sb.toString();
    }
}

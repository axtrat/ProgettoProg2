package mua.message.header;

import utils.ASCIICharSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Classe mutabile utilitaria che si occupa di decodificare le intestazioni
 * <p>
 * Decodifica le intestazioni in modo dinamico ed espandibile
 */
public class HeaderParser {
    /** Mappa il tipo dell'intestazione (in minuscolo) alla sua funzione di parsing */
    private final Map<String, Function<ASCIICharSequence, Header>> parserMap = new HashMap<>();

    /*
     * RI:  parserMap != null e non contiene chiavi o valori nulli
     *      per ogni chiave in parserMap, la chiave è: non null, non vuota e solo caratteri ASCII in lowercase
     * 
     * AF:  AF(parserMap) = { (chiave, valore) in parserMap | chiave è il tipo dell'intestazione in lowercase e valore è la funzione che decodifica l'intestazione }   
     */

    /**
     * Crea un nuovo HeaderParser contenente i parser delle intestazioni principali
     * <p>
     * Le intestazioni principali sono: 
     * {@code From}, 
     * {@code To}, 
     * {@code Subject}, 
     * {@code Date}, 
     * {@code MIME-Version}, 
     * {@code Content-Type}, 
     * {@code Content-Transfer-Encoding}
     */
    public HeaderParser() {
        parserMap.put("from", Sender::parse);
        parserMap.put("to", Recipient::parse);
        parserMap.put("subject", Subject::parse);
        parserMap.put("date", Date::parse);
        parserMap.put("mime-version", Mime::parse);
        parserMap.put("content-type", ContentType::parse);
        parserMap.put("content-transfer-encoding", ContentTransferEncoding::parse);
    }

    /**
     * Parsa l'intestazione di tipo {@code header} a partire dalla sequenza che rappresenta il suo valore
     * @param header tipo dell'intestazione in lowercase
     * @param value sequenza che rappresenta il valore dell'intestazione
     * @return l'intestazione riconosciuta o {@code null} se il tipo non è riconosciuto
     * @throws NullPointerException se {@code header} o {@code value} sono {@code null}
     * @throws IllegalArgumentException se {@code value} non può essere decodificata
     */
    public Header parse(ASCIICharSequence header, ASCIICharSequence value) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(value);
        return parserMap.get(header.toString()).apply(value);
    }

    /**
     * Aggiunge un parser per l'intestazione di tipo {@code header}
     * @param header tipo dell'intestazione
     * @param parser funzione che parsa l'intestazione
     * @throws NullPointerException se {@code header} o {@code parser} sono {@code null}
     */
    public void addHeader(Header header, Function<ASCIICharSequence, Header> parser) {
        parserMap.put(Objects.requireNonNull(header).type().toLowerCase(), Objects.requireNonNull(parser));
    }
}

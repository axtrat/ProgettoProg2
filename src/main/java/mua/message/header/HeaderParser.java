package mua.message.header;

import utils.ASCIICharSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Classe mutabile utilitaria che si occupa di decodificare le intestazioni {@link Header}
 * <p>
 * Questa classe fornisce un metodo univoco per decodificare genericamente le intestazioni. 
 * Presenta già i parser per le intestazioni principali ma permette l'aggiunta di nuovi parser tremite il metodo {@link #addHeader(Header, Function)}
 * Il quale associa un'intestazione (ignorando il case) alla funzione di parsing del suo valore.
 * <p>
 * 
 * Le intestazioni principali sono: 
 * <ul>
 *    <li>From {@link Sender}</li>
 *    <li>To {@link Recipient}</li>
 *    <li>Subject {@link Subject}</li>
 *    <li>Date {@link Date}</li>
 *    <li>Mime-Version {@link Mime}</li>
 *    <li>Content-Type {@link ContentType}</li>
 *    <li>Content-Transfer-Encoding {@link ContentTransferEncoding}</li>
 * </ul>
 * 
 * Un'esempio di utilizzo è:
 * <pre>{@code ...
 * HeaderParser parser = new HeaderParser();
 * String header = "From: Mario Rossi <mario.rossi@gmail.com>";
 * String[] fields = header.split(": ");
 * Intestazione i = parser.parse(fields[0], fields[1]);
 *...}</pre>
 * 
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
     * <p>
     * Se il tipo non è riconosciuto, ritorna {@code null}
     * @param header tipo dell'intestazione case-insensitive
     * @param value sequenza che rappresenta il valore dell'intestazione
     * @return l'intestazione riconosciuta o {@code null} se il tipo non è riconosciuto
     * @throws NullPointerException se {@code header} o {@code value} sono {@code null}
     * @throws IllegalArgumentException se {@code value} non può essere decodificata
     */
    public Header parse(ASCIICharSequence header, ASCIICharSequence value) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(value);
        return parserMap.get(header.toString().toLowerCase()).apply(value);
    }

    /**
     * Aggiunge un parser per l'intestazione di tipo {@code header}
     * <p> la funzione {@code parser} deve prendere come argomento solo la sequenza che rappresenta il valore dell'intestazione
     * @param header tipo dell'intestazione
     * @param parser funzione che parsa l'intestazione
     * @throws NullPointerException se {@code header} o {@code parser} sono {@code null}
     */
    public void addHeader(Header header, Function<ASCIICharSequence, Header> parser) {
        parserMap.put(Objects.requireNonNull(header).type().toLowerCase(), Objects.requireNonNull(parser));
    }
}

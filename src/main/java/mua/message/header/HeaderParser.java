package mua.message.header;

import utils.ASCIICharSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Classe mutabile utilitaria che si occupa di decodificara le intestazioni
 */
public class HeaderParser {
    /** Mappa il tipo dell'intestazione (in minuscolo) alla sua funzione di parsing */
    Map<ASCIICharSequence, Function<ASCIICharSequence, Intestazione>> parserMap = new HashMap<>();

    /**
     * Crea un nuovo HeaderParser contenente i parser delle intestazioni pricipali
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
        parserMap.put(ASCIICharSequence.of("from"), Mittente::parse);
        parserMap.put(ASCIICharSequence.of("to"), Destinatario::parse);
        parserMap.put(ASCIICharSequence.of("subject"), Oggetto::parse);
        parserMap.put(ASCIICharSequence.of("date"), Data::parse);
        parserMap.put(ASCIICharSequence.of("mime-version"), Mime::parse);
        parserMap.put(ASCIICharSequence.of("content-type"), ContentType::parse);
        parserMap.put(ASCIICharSequence.of("content-transfer-encoding"), ContentTransferEncoding::parse);
    }

    /**
     * Parsa l'intestazione di tipo {@code header} a partire dalla sequenza che rappresenta il suo valore
     * @param header tipo dell'intestazione in lowercase
     * @param value sequenza che rappresenta il valore dell'intestazione
     * @return l'intestazione parsata o {@code null} se il tipo non è riconosciuto
     * @throws NullPointerException se {@code header} o {@code value} sono {@code null}
     * @throws IllegalArgumentException se {@code value} non può essere parsato
     */
    public Intestazione parse(ASCIICharSequence header, ASCIICharSequence value) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(value);
        return parserMap.get(header).apply(value);
    }

    /**
     * Aggiunge un parser per l'intestazione di tipo {@code header}
     * @param header tipo dell'intestazione
     * @param parser funzione che parsa l'intestazione
     * @throws NullPointerException se {@code header} o {@code parser} sono {@code null}
     */
    public void addHeader(Intestazione header, Function<ASCIICharSequence, Intestazione> parser) {
        parserMap.put(ASCIICharSequence.of(Objects.requireNonNull(header).tipo().toLowerCase()), Objects.requireNonNull(parser));
    }
}

package mua.message.header;

import utils.ASCIICharSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class HeaderParser {
    Map<ASCIICharSequence, Function<ASCIICharSequence, Intestazione>> parserMap = new HashMap<>();

    public HeaderParser() {
        parserMap.put(ASCIICharSequence.of("from"), Mittente::parse);
        parserMap.put(ASCIICharSequence.of("to"), Destinatario::parse);
        parserMap.put(ASCIICharSequence.of("subject"), Oggetto::parse);
        parserMap.put(ASCIICharSequence.of("date"), Data::parse);
        parserMap.put(ASCIICharSequence.of("mime-version"), Mime::parse);
        parserMap.put(ASCIICharSequence.of("content-type"), ContentType::parse);
        parserMap.put(ASCIICharSequence.of("content-transfer-encoding"), ContentTransferEncoding::parse);
    }

    public Intestazione parse(ASCIICharSequence header, ASCIICharSequence value) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(value);
        return parserMap.get(header).apply(value);
    }

    public void addHeader(Intestazione header, Function<ASCIICharSequence, Intestazione> parser) {
        parserMap.put(ASCIICharSequence.of(Objects.requireNonNull(header).tipo().toLowerCase()), Objects.requireNonNull(parser));
    }
}

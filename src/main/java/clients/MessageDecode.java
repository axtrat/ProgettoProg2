package clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.stream.events.EntityDeclaration;

import mua.message.Messaggio;
import mua.message.Parte;
import mua.message.header.Data;
import mua.message.header.Destinatario;
import mua.message.header.Indirizzo;
import mua.message.header.Intestazione;
import mua.message.header.Mittente;
import mua.message.header.Oggetto;
import utils.ASCIICharSequence;
import utils.Base64Encoding;
import utils.DateEncoding;
import utils.EntryEncoding;
import utils.Fragment;

/** MessageDecode */
public class MessageDecode {

    /**
     * Tests message decoding
     *
     * <p>
     * Reads a message from stdin and emits its fragments on the stdout.
     *
     * <p>
     * Every fragment should be emitted as, for example:
     *
     * <pre>{@code
     * Fragment
     *   Raw headers:
     *     Raw type = from, value = Luca Prigioniero <prigioniero@di.unimi.it>
     *     Raw type = to, value = "Massimo prof. Santini" <santini@di.unimi.it>, info@unimi.it
     *     Raw type = subject, value = Oggetto semplice
     *     Raw type = date, value = Wed, 6 Dec 2023 12:30:20 +0100
     *     Raw type = mime-version, value = 1.0
     *     Raw type = content-type, value = multipart/alternative; boundary=frontier
     *   Raw body:
     *     This is a message with multiple parts in MIME format.
     * }</pre>
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        List<Intestazione> intestazioni = new ArrayList<>();
        List<Parte> parti = new ArrayList<>();
        
        try (Scanner s = new Scanner(System.in)) {
            intestazioni.add(new Mittente(Indirizzo.parse(s.nextLine().split(": ")[1])));
            intestazioni.add(Destinatario.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(Oggetto.parse(s.nextLine().split(": ")[1]));
            intestazioni.add(new Data(DateEncoding.decode(ASCIICharSequence.of(s.nextLine().split(": ")[1]))));
            
            String line = s.nextLine();
            if (line.startsWith("MIME")) {
                //Salvi il MIME
                s.nextLine(); //Salvi il Content-Type
                s.nextLine(); //skip
                s.nextLine(); //intesazione
                s.nextLine(); //skip
                line = s.nextLine();
            }
            while (s.hasNextLine()) {
                s.nextLine();
                String corpo = s.nextLine();
                if (line.contains("utf-8") || line.contains("html")) {
                    corpo = Base64Encoding.decode(ASCIICharSequence.of(s.nextLine()));
                }
                parti.add(new Parte(intestazioni, corpo));
                if (s.hasNextLine() && s.nextLine().equals("--frontier")) {
                    line = s.nextLine();
                }
            }
            
        }
        Messaggio messaggio = new Messaggio(parti);
        List<Fragment> fragments = EntryEncoding.decode(ASCIICharSequence.of(messaggio.toString()));  
        for (Fragment fragment : fragments) {    
            System.out.println("Fragment\n    Raw headers:");   
            for (List<ASCIICharSequence> rawHeader : fragment.rawHeaders()) 
                System.out.println("        Raw type = " + rawHeader.get(0) + ", value = " + rawHeader.get(1));
            System.out.println("    Raw body: \n        " + fragment.rawBody() + "\n");
        }
    }

}

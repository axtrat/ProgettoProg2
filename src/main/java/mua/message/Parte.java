package mua.message;

import java.util.Objects;
import mua.message.header.ContentType;
import utils.Base64Encoding;

/** Classe immutabile che rappresenta la parte iniziale di un messaggio */
public class Parte {
  private final ContentType intestazione;
  private final String corpo;

  public Parte(final ContentType intestazione, final String corpo) {
    this.intestazione = Objects.requireNonNull(intestazione);
    this.corpo = Objects.requireNonNull(corpo);
  }

  public String corpo() {
    return corpo;
  }

  public ContentType intestazione() {
    return intestazione;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(intestazione.tipo()).append(": ").append(intestazione.toString());
    if (!intestazione.isAscii()) {
      sb.append("\nContent-Transfer-Encoding: base64\n\n");
      sb.append(Base64Encoding.encode(corpo));
    } else {
      sb.append("\n\n");
      sb.append(corpo);
    }

    return sb.toString();
  }
}

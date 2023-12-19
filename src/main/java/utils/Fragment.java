/*

Copyright 2023 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/

package utils;

import java.util.List;
import java.util.Objects;

/**
 * A representation of a <em>part</em> of a multipart email message.
 *
 * <p>For an example see {@link utils.EntryEncoding}.
 */
public class Fragment {
  private final List<List<ASCIICharSequence>> rawHeaders;
  private final ASCIICharSequence rawBody;

  Fragment(List<List<ASCIICharSequence>> rawHeaders, ASCIICharSequence rawBody) {
    this.rawBody = Objects.requireNonNull(rawBody);
    this.rawHeaders = Objects.requireNonNull(rawHeaders);
  }

  /**
   * Returns the (possibly Base64 encoded) body of the part.
   *
   * @return the raw body of the part.
   */
  public ASCIICharSequence rawBody() {
    return rawBody;
  }

  /**
   * Returns a list of raw headers found in the part.
   *
   * <p>Every element of the list is a list of two elements: the first is the {@link
   * ASCIICharSequence} corresponding to the name of the header (the part before {@code :}, in
   * uppercase), the second is the {@link ASCIICharSequence} corresponding to the value of the
   * header (the part after {@code :}).
   *
   * @return a list of raw headers.
   */
  public List<List<ASCIICharSequence>> rawHeaders() {
    return rawHeaders;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Fragment\nRaw headers:\n");
    rawHeaders.forEach(h -> sb.append(String.format("%s: %s\n", h.get(0), h.get(1))));
    sb.append("Raw body: \n" + rawBody);
    return sb.toString();
  }
}

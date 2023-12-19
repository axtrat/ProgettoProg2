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

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/** An utility class to encode and decode a date. */
public class DateEncoding {

  private DateEncoding() {}

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

  /** The {@link ZoneId} of Europe/Rome. */
  public static final ZoneId EUROPE_ROME = ZoneId.of("Europe/Rome");

  /**
   * Encodes a {@link ZonedDateTime} date to an {@link ASCIICharSequence}.
   *
   * @param date the date.
   * @return the encoded sequence.
   * @throws DateTimeException if the date cannot be formatted.
   * @throws NullPointerException if the date is {@code null}.
   */
  public static ASCIICharSequence encode(final ZonedDateTime date)
      throws DateTimeException, NullPointerException {
    return ASCIICharSequence.of(Objects.requireNonNull(date).format(DATE_FORMATTER));
  }

  /**
   * Decodes the {@link ASCIICharSequence} to a {@link ZonedDateTime}.
   *
   * @param sequence the sequence.
   * @return the date.
   * @throws DateTimeParseException if the sequence cannot be parsed.
   * @throws NullPointerException if the sequence is {@code null}.
   */
  public static ZonedDateTime decode(final ASCIICharSequence sequence) {
    return ZonedDateTime.parse(Objects.requireNonNull(sequence), DATE_FORMATTER);
  }
}

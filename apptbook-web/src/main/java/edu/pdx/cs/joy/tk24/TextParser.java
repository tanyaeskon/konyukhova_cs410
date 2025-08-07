package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A parser for reading appointment books from text format.
 * Parses text with owner name on first line and pipe-delimited appointments.
 */
public class TextParser {
  private final Reader reader;

  /**
   * Creates a new TextParser that reads from the specified reader.
   *
   * @param reader the reader to parse text from
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses an appointment book from text format.
   * Expects owner name on first line, followed by appointments in
   * "description | begin time | end time" format.
   *
   * @return the parsed appointment book
   * @throws ParserException if the text format is invalid or parsing fails
   */
  public AppointmentBook parse() throws ParserException {
    try (BufferedReader br = new BufferedReader(this.reader)) {
      String owner = br.readLine();
      if (owner == null) {
        throw new ParserException("Missing owner line");
      }

      AppointmentBook book = new AppointmentBook(owner);
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
          throw new ParserException("Malformed appointment line: " + line);
        }

        String description = parts[0].trim();
        String beginTimeStr = parts[1].trim();
        String endTimeStr = parts[2].trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
        LocalDateTime beginTime = LocalDateTime.parse(beginTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        book.addAppointment(new Appointment(description, beginTime, endTime));

      }
      return book;

    } catch (IOException e) {
      throw new ParserException("While parsing appointment book", e);
    }
  }
}
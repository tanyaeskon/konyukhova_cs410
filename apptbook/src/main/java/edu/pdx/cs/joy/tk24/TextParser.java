package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookParser;
import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses an {@link AppointmentBook} from plain text input.
 * Expects the first line to contain the owner's name,
 * followed by one line per appointment in the format:
 * description | begin time | end time
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {

  private final Reader reader;

  /**
   * Creates a new parser with the given reader as its input source.
   *
   * @param reader The reader to read text from
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses the contents of the text file and returns an {@link AppointmentBook}
   * with the owner and all valid appointments.
   *
   * The first line must contain the owner's name.
   * Each following line must have exactly three fields separated by '|':
   * description, begin time, and end time.
   *
   * @return The parsed {@link AppointmentBook}
   * @throws ParserException If the file is malformed or an I/O error occurs
   */
  @Override
  public AppointmentBook parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String owner = br.readLine();

      if (owner == null) {
        throw new ParserException("Missing owner");
      }

      AppointmentBook appointmentBook = new AppointmentBook(owner);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

      String line;
      while ((line = br.readLine()) != null) {
        if(line.trim().isEmpty()){
          continue;
        }
        String[] fields = line.split("\\|");

        if (fields.length != 3) {
          throw new ParserException("Invalid line format");
        }

        String description = fields[0].trim();
        String beginStr = fields[1].trim();
        String endStr = fields[2].trim();

        try {
          LocalDateTime beginTime = LocalDateTime.parse(beginStr, formatter);
          LocalDateTime endTime = LocalDateTime.parse(endStr, formatter);
          appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));
        } catch (DateTimeParseException e) {
          throw new ParserException("Invalid date/time format in line: " + line, e);
        }
      }
      return appointmentBook;

    } catch (IOException e) {
      throw new ParserException("While parsing appointment book text", e);
    }
  }
}

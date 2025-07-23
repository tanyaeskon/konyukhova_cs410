package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookParser;
import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses the contents of the text file and returns an {@link AppointmentBook}
   * with the owner and all valid appointments.
   * <p>
   * The first line must contain the owner name.
   * Each following line must be a single appointment with three fields separated by '|'.
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

     // return new AppointmentBook(owner); remove this because you're returning before reading any of the appointments

      AppointmentBook appointmentBook = new AppointmentBook(owner);

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
        String beginTime = fields[1].trim();
        String endTime = fields[2].trim();

        appointmentBook.addAppointment(new Appointment(description, beginTime, endTime));

      }
      return appointmentBook;

    } catch (IOException e) {
      throw new ParserException("While parsing appointment book text", e);
    }
  }
}

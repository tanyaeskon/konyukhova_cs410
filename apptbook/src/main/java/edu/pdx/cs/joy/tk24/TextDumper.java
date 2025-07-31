package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

/**
 * A class that writes the contents of an AppointmentBook to a text format.
 *
 * Each appointment is written on its own line in the format:
 * Owner Name
 * description|begin time|end time
 *
 * Dates and times are formatted as MM/dd/yyyy h:mm a.
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook> {

  private final Writer writer;

  /**
   * Creates a new TextDumper that writes to the specified writer.
   *
   * @param writer The writer to output the appointment book to
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dumps the appointment book's contents to the writer.
   *
   * The first line is the owner's name.
   * Each following line contains one appointment with fields separated by "|".
   *
   * @param book The AppointmentBook to write
   */
  @Override
  public void dump(AppointmentBook book) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println(book.getOwnerName());

      DateTimeFormatter textFileFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

      for(Appointment appointment : book.getAppointments()) {
        String beginTime = appointment.getBeginTime().format(textFileFormatter);
        String endTime = appointment.getEndTime().format(textFileFormatter);

        pw.println(appointment.getDescription() + "|" + beginTime + "|" + endTime);
      }
      pw.flush();
    }
  }
}

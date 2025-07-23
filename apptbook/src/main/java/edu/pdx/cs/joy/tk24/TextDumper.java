package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookDumper;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dumps the contents of the given {@link AppointmentBook} to the writer.
   * The output format is:
   * <pre>
   *   Owner Name
   *   description|begin time|end time
   *   ...
   * </pre>
   *
   * @param book The appointment book to dump
   */
  @Override
  public void dump(AppointmentBook book) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println(book.getOwnerName());

      for(Appointment appointment : book.getAppointments()) {
        pw.println(appointment.getDescription() + "|" +
                appointment.getBeginTimeString() + "|" +
                appointment.getEndTimeString());


      }

      pw.flush();
    }

  }
}

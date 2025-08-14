package edu.pdx.cs.joy.tk24;


import java.io.PrintWriter;
import java.io.Writer;

public class PrettyPrinter {
  private final Writer writer;


  /**
   * Creates a new PrettyPrinter that writes to the specified writer.
   *
   * @param writer the writer to output formatted text to
   */
  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  /**
   * Prints an appointment book in a formatted, human-readable style.
   * Shows owner name, appointment descriptions, times, and calculated durations.
   *
   * @param book the appointment book to format and print
   */
  public void dump(AppointmentBook book) {
    try (
            PrintWriter pw = new PrintWriter(this.writer)
    ) {
      pw.println("Owner: " + book.getOwnerName());
      pw.println("Appointments:");

      for (Appointment appt : book.getAppointments()) {
        pw.println("  Description: " + appt.getDescription());
        pw.println("  Begins at:   " + appt.getBeginTime());
        pw.println("  Ends at:     " + appt.getEndTime());
        pw.println("  Duration:    " + appt.getBeginTime().until(appt.getEndTime(), java.time.temporal.ChronoUnit.MINUTES) + " minutes");
        pw.println();
      }

      pw.flush();
    }
  }
}

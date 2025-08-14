package edu.pdx.cs.joy.tk24;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;



public class TextDumper {
    private final Writer writer;

    /**
     * Creates a new TextDumper that writes to the specified writer.
     *
     * @param writer the writer to output text to
     */
    public TextDumper(Writer writer) {
        this.writer = writer;
    }


    /**
     * Dumps an appointment book to text format.
     * Outputs owner name followed by appointments in pipe-delimited format.
     *
     * @param book the appointment book to dump
     */
    public void dump(AppointmentBook book) {
        try (PrintWriter pw = new PrintWriter(this.writer)) {
            pw.println(book.getOwnerName());

            for (Appointment appointment : book.getAppointments()) {
                pw.printf("%s | %s | %s%n",
                        appointment.getDescription(),
                        appointment.getBeginTimeString(),
                        appointment.getEndTimeString()
                );
            }

            pw.flush();
        }
    }
}

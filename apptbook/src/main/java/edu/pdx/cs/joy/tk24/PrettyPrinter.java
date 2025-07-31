package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that formats and outputs an appointment book in a readable form.
 *
 * The output includes the owner's name and details for each appointment,
 * including description, begin time, end time, and duration in minutes.
 */
public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;

    /**
     * Creates a new PrettyPrinter that writes to the given writer.
     *
     * @param writer The writer to output the pretty-printed appointment book
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Outputs a formatted and sorted list of appointments for the given appointment book.
     *
     * Each appointment is printed with its description, begin time, end time,
     * and duration (in minutes). Appointments are sorted before printing.
     *
     * @param book The AppointmentBook to pretty print
     * @throws IOException If writing to the output fails
     */
    @Override
    public void dump(AppointmentBook book) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println("Appointment Book for: " + book.getOwnerName());
        List<Appointment>appointments = new ArrayList<>(book.getAppointments());
        Collections.sort(appointments);

        for (Appointment appointment : appointments) {
            printWriter.println("Description : " + appointment.getDescription());;
            printWriter.println("Begin Time : " + appointment.getBeginTimeString());
            printWriter.println("End Time   : " + appointment.getEndTimeString());
            long duration = Duration.between(appointment.getBeginTime(), appointment.getEndTime()).toMinutes();
            printWriter.println("Duration : " + duration);
        }
        printWriter.close();
    }
}

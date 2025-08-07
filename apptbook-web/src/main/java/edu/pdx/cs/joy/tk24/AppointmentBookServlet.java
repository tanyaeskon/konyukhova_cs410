package edu.pdx.cs.joy.tk24;

import com.google.common.annotations.VisibleForTesting;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import static jakarta.xml.bind.DatatypeConverter.parseDateTime;
import java.time.format.DateTimeFormatter;


/**
 * This servlet provides a REST API for working with appointment books.
 * It supports HTTP GET, POST, and DELETE operations to manage appointments
 * for multiple owners. All date/time parameters use "MM/dd/yyyy h:mm AM/PM" format.
 */
public class AppointmentBookServlet extends HttpServlet
{
    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String BEGIN_PARAMETER = "begin";
    static final String END_PARAMETER = "end";

    private final Map<String, AppointmentBook> AppointmentBooks = new HashMap<>();

    /**
     * Handles HTTP GET requests to retrieve appointments. Can return appointments
     * for a specific owner, filtered by date range, or all appointments.
     *
     * @param request the HTTP servlet request containing query parameters
     * @param response the HTTP servlet response to write the appointment data to
     * @throws IOException if an error occurs while writing to the response
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );
        String begin = getParameter(BEGIN_PARAMETER, request);
        String end = getParameter(END_PARAMETER, request);


        if (owner != null && begin != null && end != null) {
            AppointmentBook book = this.AppointmentBooks.get(owner);
            if (book == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            try {
                LocalDateTime beginTime = parseDateTime(begin);
                LocalDateTime endTime = parseDateTime(end);

                AppointmentBook filtered = new AppointmentBook(owner);
                for (Appointment appt : book.getAppointments()) {
                    LocalDateTime start = appt.getBeginTime();
                    if ((start.isEqual(beginTime) || start.isAfter(beginTime)) &&
                            (start.isBefore(endTime) || start.isEqual(endTime))) {
                        filtered.addAppointment(appt);
                    }
                }

                PrintWriter pw = response.getWriter();
                new TextDumper(pw).dump(filtered);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DateTimeParseException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid begin or end date format: " + ex.getMessage());
            }

        } else if (owner != null) {
            log("GET " + owner);
            writeDefinition(owner, response);
        } else {
            log("GET all dictionary entries");
            writeAllDictionaryEntries(response);
        }

    }

    /**
     * Handles HTTP POST requests to add new appointments. Creates a new appointment
     * with owner, description, begin time, and end time parameters.
     *
     * @param request the HTTP servlet request containing the appointment parameters
     * @param response the HTTP servlet response to write confirmation message to
     * @throws IOException if an error occurs while writing to the response
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );

        if (owner == null) {
            missingRequiredParameter(response, OWNER_PARAMETER);
            return;
        }

        String description = getParameter(DESCRIPTION_PARAMETER, request );
        if (description == null) {
            missingRequiredParameter( response, DESCRIPTION_PARAMETER );
            return;
        }

        String beginTime = getParameter(BEGIN_PARAMETER, request );
        if(beginTime == null) {
            missingRequiredParameter( response, BEGIN_PARAMETER );
            return;
        }

        String endTime = getParameter(END_PARAMETER, request );
        if(endTime == null) {
            missingRequiredParameter( response, END_PARAMETER );
            return;
        }

        log("POST " + owner + " -> " + description);

        AppointmentBook book = this.AppointmentBooks.get( owner );
        if (book == null) {
            book = new AppointmentBook(owner);
            this.AppointmentBooks.put(owner, book);
        }

        try{
            LocalDateTime beginDate = parseDateTime(beginTime);
            LocalDateTime endDate = parseDateTime(endTime);

            Appointment appointment = new Appointment(description, beginDate, endDate);
            book.addAppointment(appointment);

            PrintWriter pw = response.getWriter();
            pw.println("Added appointment: " + description + " from " + beginTime + " to " + endTime + " for " + owner);
            pw.flush();

            response.setStatus( HttpServletResponse.SC_OK);
        }
        catch(DateTimeParseException ex) {
            String message = "Invalid date/time format: " + ex.getMessage();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
        }


    }

    /**
     * Handles HTTP DELETE requests by removing all appointment books.
     * This operation is primarily for testing purposes.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response to write confirmation message to
     * @throws IOException if an error occurs while writing to the response
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        log("DELETE all appointment books");

        this.AppointmentBooks.clear();

        PrintWriter pw = response.getWriter();
        pw.println("All appointment books deleted");
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing required parameter to the HTTP response.
     * Sets the HTTP status to 412 (Precondition Failed) and includes an error message
     * indicating which parameter is missing.
     *
     * @param response the HTTP servlet response to write the error to
     * @param parameterName the name of the missing parameter
     * @throws IOException if an error occurs while writing to the response
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        //String message = Messages.missingRequiredParameter(parameterName);
        String message = "Missing required parameter: " + parameterName;
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the appointments for the specified owner to the HTTP response.
     * If no appointment book exists for the owner, only the owner name is written.
     * Otherwise, all appointments are formatted using {@link TextDumper}.
     *
     * @param owner the name of the appointment book owner
     * @param response the HTTP servlet response to write the appointments to
     * @throws IOException if an error occurs while writing to the response
     */
    private void writeDefinition(String owner, HttpServletResponse response) throws IOException {

        AppointmentBook book = this.AppointmentBooks.get(owner);
        PrintWriter pw = response.getWriter();


        if (book == null) {
            pw.println(owner);

        } else {

            TextDumper dumper = new TextDumper(pw);
            dumper.dump(book);
        }
        pw.flush();
        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes all appointment books for all owners to the HTTP response.
     * Each appointment book is formatted using {@link TextDumper}.
     *
     * @param response the HTTP servlet response to write all appointments to
     * @throws IOException if an error occurs while writing to the response
     */
    private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();

        for (AppointmentBook book : AppointmentBooks.values()) {
            TextDumper dumper = new TextDumper(pw);
            dumper.dump(book);
        }

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     * Returns null if the parameter value is null or an empty string.
     *
     * @param name the name of the parameter to retrieve
     * @param request the HTTP servlet request containing the parameter
     * @return the parameter value, or null if the parameter is null or empty
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;
      } else {
        return value;
      }
    }

    /**
     * Returns the appointment book for the specified owner. This method is
     * primarily used for testing purposes to verify the internal state of the servlet.
     *
     * @param owner the name of the appointment book owner
     * @return the appointment book for the owner, or null if no book exists
     */
    @VisibleForTesting
    AppointmentBook getAppointmentBook(String owner) {
        return this.AppointmentBooks.get(owner);
    }

    /**
     * Logs a message to the console. This method overrides the default servlet
     * logging behavior to output messages to System.out for easier debugging.
     *
     * @param msg the message to log
     */
    @Override
    public void log(String msg) {
      System.out.println(msg);
    }

    /**
     * Parses a date/time string in the format "MM/dd/yyyy h:mm AM/PM" into a LocalDateTime object.
     * This method provides consistent date/time parsing throughout the servlet.
     *
     * @param dateTimeString the date/time string to parse
     * @return a LocalDateTime object representing the parsed date/time
     * @throws DateTimeParseException if the string cannot be parsed with the expected format
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}

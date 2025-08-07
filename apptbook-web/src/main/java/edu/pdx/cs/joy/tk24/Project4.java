package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AppointmentBookParser;
import edu.pdx.cs.joy.ParserException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    /**
     * Main entry point for the appointment book client application.
     * Parses command line arguments and performs operations with the appointment book server.
     *
     * @param args command line arguments including options and appointment data
     */
    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        String owner = null;
        String description = null;
        String beginTime = null;
        String endTime = null;

        boolean searchFlag = false;
        boolean printFlag = false;
        boolean readmeFlag = false;

        List<String> remainingArgs = new ArrayList<>();

        if (args.length == 0) {
            System.err.println(MISSING_ARGS);
            return;
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-search")) {
                searchFlag = true;
            } else if (arg.equals("-print")) {
                printFlag = true;
            } else if (arg.equals("-README")) {
                readmeFlag = true;
            } else if (arg.equals("-host")) {
                if (i + 1 < args.length) {
                    hostName = args[++i];
                } else {
                    usage("Missing hostname after -host");
                    return;
                }
            } else if (arg.equals("-port")) {
                if (i + 1 < args.length) {
                    portString = args[++i];
                } else {
                    usage("Missing port number after -port");
                    return;
                }
            } else {
                remainingArgs.add(arg);
            }
        }

        if (readmeFlag) {
            System.out.println("""
                Project4: Appointment Book Application
              
                This is a REST client for an appointment book web service.
                
                Usage: java -jar target/apptbook-client.jar [options] <args>
                
                args are (in this order):
                  owner       The person who owns the appointment book
                  description A description of the appointment
                  begin       When the appointment begins (MM/dd/yyyy h:mm AM/PM)
                  end         When the appointment ends (MM/dd/yyyy h:mm AM/PM)
                
                options are (options may appear in any order):
                  -host hostname  Host computer on which the server runs
                  -port port      Port on which the server is listening
                  -search         Search for appointments
                  -print          Prints a description of the new appointment
                  -README         Prints a README for this project and exits
                
                It is an error to specify a host without a port and vice versa.
                """);
            return;
        }

        if (hostName == null && portString != null) {
            usage("Cannot specify port without host");
            return;
        }
        if (hostName != null && portString == null) {
            usage("Cannot specify host without port");
            return;
        }

        if (hostName == null || portString == null) {
            usage("Missing host and port");
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        if (searchFlag) {
            if (remainingArgs.size() < 1) {
                usage("Missing owner for search");
                return;
            } else if (remainingArgs.size() == 1) {
                owner = remainingArgs.get(0);
            } else if (remainingArgs.size() == 3) {
                owner = remainingArgs.get(0);
                beginTime = remainingArgs.get(1);
                endTime = remainingArgs.get(2);
            } else {
                usage("Search requires owner, or owner with begin and end times");
                return;
            }
        } else {
            if (remainingArgs.size() != 4) {
                usage("Expected: owner description begin end");
                return;
            }
            owner = remainingArgs.get(0);
            description = remainingArgs.get(1);
            beginTime = remainingArgs.get(2);
            endTime = remainingArgs.get(3);
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        try {
            if (searchFlag) {
                String appointments;
                if (beginTime != null && endTime != null) {
                    appointments = client.getAppointmentsBetween(owner, beginTime, endTime);
                } else {
                    appointments = client.getAllAppointmentEntries(owner);
                }

                if (appointments == null || appointments.trim().isEmpty()) {
                    System.out.println(Messages.noAppointmentsFound(owner));

                } else {

                    StringReader reader = new StringReader(appointments);
                    TextParser parser = new TextParser(reader);
                    AppointmentBook book = parser.parse();

                    PrintWriter writer = new PrintWriter(System.out, true);
                    PrettyPrinter printer = new PrettyPrinter(writer);
                    printer.dump(book);

                }

            } else {
                client.addAppointment(owner, description, beginTime, endTime);

                if (printFlag) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
                        LocalDateTime beginDateTime = LocalDateTime.parse(beginTime, formatter);
                        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);

                        Appointment appt = new Appointment(description, beginDateTime, endDateTime);
                        AppointmentBook book = new AppointmentBook(owner);
                        book.addAppointment(appt);

                        PrintWriter writer = new PrintWriter(System.out, true);
                        PrettyPrinter printer = new PrettyPrinter(writer);
                        printer.dump(book);
                    } catch (DateTimeParseException ex) {
                        error("Invalid date/time format: " + ex.getMessage());
                        return;
                    }
                } else {
                    System.out.println(Messages.appointmentAddedSuccessfully());
                }
            }

        } catch (IOException ex) {
            error("While contacting server: " + ex.getMessage());
            return;

        }
        catch (ParserException ex) {
            error("While parsing server response: " + ex.getMessage());
            return;
        }

    }

    /**
     * Prints an error message to standard error with a prefix.
     *
     * @param message the error message to display
     */
    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits.
     *
     * @param message An error message to print
     */
    private static void usage(String message) {
        PrintStream err = System.err;
        err.println(message);
        err.println();
        err.println("usage: java -jar target/apptbook-client.jar [options] <args>");
        err.println("args are (in this order):");
        err.println("  owner       The person who owns the appointment book");
        err.println("  description A description of the appointment");
        err.println("  begin       When the appointment begins (MM/dd/yyyy h:mm AM/PM)");
        err.println("  end         When the appointment ends (MM/dd/yyyy h:mm AM/PM)");
        err.println();
        err.println("options are (options may appear in any order):");
        err.println("  -host hostname  Host computer on which the server runs");
        err.println("  -port port      Port on which the server is listening");
        err.println("  -search         Search for appointments");
        err.println("  -print          Prints a description of the new appointment");
        err.println("  -README         Prints a README for this project and exits");
        err.println();
        err.println("It is an error to specify a host without a port and vice versa.");
        err.println();
    }
}
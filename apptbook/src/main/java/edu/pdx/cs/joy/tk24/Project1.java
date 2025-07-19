package edu.pdx.cs.joy.tk24;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the Appointment Book Project
 */
public class Project1 {

  /**
   * Checks whether the given date and time string has a valid format.
   * This is a placeholder method for internal use and testing.
   *
   * @param dateAndTime A date and time string to validate
   * @return true if the string is valid (always true for now)
   */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  /**
   * The main method that processes command-line arguments and either
   * prints a README, prints the new appointment, or reports errors.
   *
   * @param args Command line arguments: [-README] [-print] &lt;owner&gt; &lt;description&gt; &lt;beginTime&gt; &lt;endTime&gt;
   */
  public static void main(String[] args) {

    // Show error if no arguments are provided
    if (args.length == 0) {
      System.err.println("Missing command line arguments.\nUsage: java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>");
      return;
    }
    boolean print = false;
    boolean readme = false;

    List<String> remainingArgs = new ArrayList<>();

    // Parse options and arguments
    for (String arg : args) {
      if (arg.equals("-print")) {
        print = true;
      } else if (arg.equals("-README")) {
        readme = true;
      }
      else if (arg.startsWith("-")) {
        System.err.println("Unknown command line option");
        return;
      }
      else {
        remainingArgs.add(arg);
      }
    }

    // Print README text and exit
    if (readme) {
      System.out.println("""
        Project1: Appointment Book Application

        Usage: java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>
        Options:
          -README : Prints this README and exits
          -print  : Prints the new appointment
          
        Arguments:
          owner       The person who owns the appointment book
          description A description of the appointment
          beginTime   When the appointment begins (mm/dd/yyyy hh:mm)
          endTime     When the appointment ends (mm/dd/yyyy hh:mm)
      """);
      return;
    }

    // Validate the number of arguments
    if (remainingArgs.size() < 4) {
      System.err.println("Missing command line arguments: owner, description, begin time, or end time");
      return;
    } else if (remainingArgs.size() > 4) {
      System.err.println("Too many command line arguments. Expected: owner, description, begin time, end time");
      return;
    }

    // Extract appointment details
    String owner = remainingArgs.get(0);
    String description = remainingArgs.get(1);
    String beginTime = remainingArgs.get(2);
    String endTime = remainingArgs.get(3);

    // Validate description and time format
    if (description.trim().isEmpty()) {
      System.err.println("Description cannot be empty");
      return;
    }

    if (!isValidDateTime(beginTime)) {
      System.err.println("Invalid begin time format. Expected: mm/dd/yyyy hh:mm");
      return;
    }

    if (!isValidDateTime(endTime)) {
      System.err.println("Invalid end time format. Expected: mm/dd/yyyy hh:mm");
      return;
    }

    Appointment appt = new Appointment(description, beginTime, endTime);
    AppointmentBook book = new AppointmentBook(owner);
    book.addAppointment(appt);

    // Print appointment if requested
    if (print) {
      System.out.println(appt);
    }
  }

  /**
   * Validates whether the input matches the expected date/time format: mm/dd/yyyy hh:mm
   *
   * @param input The string to check
   * @return true if the input is in a valid format
   */
  static boolean isValidDateTime(String input) {
    return input.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{2}");
  }


}
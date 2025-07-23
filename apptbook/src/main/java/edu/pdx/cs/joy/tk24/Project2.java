package edu.pdx.cs.joy.tk24;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the Appointment Book Project
 */
public class Project2 {

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
    String textFile = null;

    List<String> remainingArgs = new ArrayList<>();
    // Parse options and arguments
    //for (String arg : args) {
      for(int i = 0; i < args.length; i++){
          String arg = args[i];
          if (arg.equals("-print")) {
        print = true;
      } else if (arg.equals("-README")) {
        readme = true;
      } else if(arg.equals("-textFile")) {
        //if(i + 1>= args.length)
          if (i + 1 >= args.length || args[i + 1].startsWith("-"))
        {
          System.err.println("Missing filename after -textFile");
          return;
        }
        textFile = args[++i];
      } else if (arg.startsWith("-")) {
        System.err.println("Unknown command line option");
        return;
      } else {
        remainingArgs.add(arg);
      }
    }

    // Print README text and exit
    if (readme) {
      System.out.println("""
                Project2: Appointment Book Application
              
                Usage: java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>
                Options:
                  -README : Prints this README and exits
                  -print  : Prints the new appointment
                  -textFile file : Reads/writes appointment book from/to a text file
              
                Arguments:
                  owner       The person who owns the appointment book
                  description A description of the appointment
                  beginTime   When the appointment begins (mm/dd/yyyy HH:mm)
                  endTime     When the appointment ends (mm/dd/yyyy HH:mm)
              """);
      return;
    }


    // Validate the number of arguments
      if (remainingArgs.size() < 6) {
        System.err.println("Missing command line arguments: owner, description, begin time, or end time");
        return;
      } else if (remainingArgs.size() > 6) {
        System.err.println("Too many command line arguments. Expected: owner, description, begin time, end time");
          return;
      }




    // Extract appointment details
    String owner = remainingArgs.get(0);
    String description = remainingArgs.get(1);
    String beginTime = remainingArgs.get(2) + " " + remainingArgs.get(3);
    String endTime = remainingArgs.get(4) + " " + remainingArgs.get(5);

    // Validate description and time format
    if (description.trim().isEmpty()) {
      System.err.println("Description cannot be empty");
      return;
    }

    if (!isValidDateTime(beginTime)) {
      System.err.println("Invalid begin time format");
      return;
    }

    if (!isValidDateTime(endTime)) {
      System.err.println("Invalid end time format");
      return;
    }

    Appointment appt = new Appointment(description, beginTime, endTime);
    AppointmentBook book = new AppointmentBook(owner);

    if(textFile != null) {
      File file = new File(textFile);
      if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          TextParser parser = new TextParser(reader);
          book = parser.parse();

          if (!book.getOwnerName().equals(owner)) {
            System.err.println("Invalid owner name");
            return;
          }

        } catch (IOException | edu.pdx.cs.joy.ParserException e) {
          System.err.println(e.getMessage());
          return;
        }

      } else {
        book = new AppointmentBook(owner);
      }
    }else{
      book = new AppointmentBook(owner);
      }

    book.addAppointment(appt);

    // Print appointment if requested
    if (print) {
      System.out.println(appt);
    }
    if(textFile != null) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
        TextDumper dumper = new TextDumper(writer);
        dumper.dump(book);
      }catch(IOException e){
        System.err.println(e.getMessage());
      }
    }
  }



  /**
   * Validates whether the input matches the expected date/time format: mm/dd/yyyy hh:mm
   *
   * @param input The string to check
   * @return true if the input is in a valid format
   */

  static boolean isValidDateTime(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm");
    try {
      LocalDateTime.parse(input, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }

  }


}
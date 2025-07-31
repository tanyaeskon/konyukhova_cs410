package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the Appointment Book Project
 */
public class Project3 {

  /**
   * The main method that processes command-line arguments and either
   * prints a README, prints the new appointment, or reports errors.
   *
   * @param args Command line arguments: [-README] [-print] &lt;owner&gt; &lt;description&gt; &lt;beginTime&gt; &lt;endTime&gt;
   */
  public static void main(String[] args) {

    //Show error if no arguments are provided
    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      return;
    }

    boolean print = false;
    boolean readme = false;
    String textFile = null;
    String prettyFile = null;

    List<String> remainingArgs = new ArrayList<>();

    //Parse options and arguments
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.equals("-print")) {
        print = true;
      } else if (arg.equals("-README")) {
        readme = true;
      } else if (arg.equals("-textFile")) {
        if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
          System.err.println("Missing filename after -textFile");
          return;
        }
        textFile = args[++i];

      }
      //else if (arg.equals("-pretty")) {
        //if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
          //System.err.println("Missing filename after -pretty");
          //return;
       // }
        //prettyFile = args[++i];
      else if (arg.equals("-pretty")) {
        if (i + 1 >= args.length) {
          System.err.println("Missing filename after -pretty");
          return;
        }
        String nextArg = args[i + 1];
        if (nextArg.startsWith("-") && !nextArg.equals("-")) {
          System.err.println("Missing filename after -pretty");
          return;


        }
        prettyFile = args[++i];

      } else if (arg.startsWith("-")) {
        System.err.println("Unknown command line option");
        return;
      } else {
        remainingArgs.add(arg);
      }
    }

    //Print README text and exit
    if (readme) {
      System.out.println("""
                Project3: Appointment Book Application
              
                Usage: java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>
                Options:
                  -README : Prints this README and exits
                  -print  : Prints the new appointment
                  -textFile file : Reads/writes appointment book from/to a text file
                  -pretty file : Pretty print the appointment book (use "-" for standard output)
              
                Arguments:
                  owner       The person who owns the appointment book
                  description A description of the appointment
                  beginTime   When the appointment begins (mm/dd/yyyy h:mm a)
                  endTime     When the appointment ends (mm/dd/yyyy h:mm a)
              """);
      return;
    }

    //Validate the number of arguments
    if (remainingArgs.size() < 8) {
      System.err.println("Missing command line arguments");
      return;
    } else if (remainingArgs.size() > 8) {
      System.err.println("Too many command line arguments");
      return;
    }

    //Extract appointment details
    String owner = remainingArgs.get(0);
    String description = remainingArgs.get(1);

    String beginTimeStr = remainingArgs.get(2) + " " + remainingArgs.get(3) + " " + remainingArgs.get(4);
    String endTimeStr = remainingArgs.get(5) + " " + remainingArgs.get(6) + " " + remainingArgs.get(7);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
    LocalDateTime beginTime, endTime;

    try {
      beginTime = LocalDateTime.parse(beginTimeStr, formatter);
    } catch (DateTimeParseException e) {
      System.err.println("Invalid begin time format");
      return;
    }

    try {
      endTime = LocalDateTime.parse(endTimeStr, formatter);
    } catch (DateTimeParseException e) {
      System.err.println("Invalid end time format");
      return;
    }

    if (endTime.isBefore(beginTime)) {
      System.err.println("End time cannot be before begin time");
      return;
    }

    Appointment appt = new Appointment(description, beginTime, endTime);
    AppointmentBook book;

    if (textFile != null) {
      File file = new File(textFile);
      if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          TextParser parser = new TextParser(reader);
          book = parser.parse();

          if (!book.getOwnerName().equals(owner)) {
            System.err.println("Invalid owner name");
            return;
          }
        } catch (ParserException e) {
          System.err.println("Could not parse file: " + e.getMessage());
          return;
        } catch (IOException e) {
          System.err.println("Could not read file: " + e.getMessage());
          return;
        }
      } else {
        book = new AppointmentBook(owner);
      }
    } else {
      book = new AppointmentBook(owner);
    }

    //Add the new appointment to the book
    book.addAppointment(appt);

    //Print appointment if requested
    if (print) {
      System.out.println(appt);
    }

    //Write updated appointment book to text file if specified
    if (textFile != null) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
        TextDumper dumper = new TextDumper(writer);
        dumper.dump(book);
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }

    //Pretty print to file or stdout if requested
    if (prettyFile != null) {
      try (Writer writer = prettyFile.equals("-")
              ? new OutputStreamWriter(System.out)
              : new FileWriter(prettyFile)) {
        PrettyPrinter prettyPrinter = new PrettyPrinter(writer);
        prettyPrinter.dump(book);
      } catch (IOException e) {
        System.err.println("Error writing pretty file: " + e.getMessage());
      }
    }
  }
}







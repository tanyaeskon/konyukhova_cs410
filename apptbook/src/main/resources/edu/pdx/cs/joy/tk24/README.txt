This is a README file!
README for Project 3: Appointment Book with Pretty Printing
CS410 Joy of Coding – Summer 2025
Tanya Konyukhova

--------------------------------------------------
What this program does
--------------------------------------------------

This is a Java command-line application that manages an appointment book.
It allows users to add appointments, save them to a file, load them from a file,
and print them in either a raw or "pretty" formatted style.

You provide the owner’s name, a description, the start time, and the end time.
The appointment is added to an appointment book that can be stored in a text file
or printed to the console in a readable format.

Example:
  java -jar target/apptbook-1.0.0.jar -pretty - "Tanya" "Doctor Visit" 07/30/2025 2:00 PM 07/30/2025 3:00 PM

This adds the appointment and pretty prints the book to standard output.

--------------------------------------------------
How to run it
--------------------------------------------------

First, build the project with Maven:

  ./mvnw verify

Then, run the program:
  java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>

--------------------------------------------------
Command line options
--------------------------------------------------

  -README   Prints out this README and exits.
  -print    Prints the appointment that was added.
  -textFile     Specifies a text file to read from or write to (must be followed by a filename
  -pretty DEST      Pretty-prints the appointment book.
                        - Use "-" to print to standard output.
                        - Use a filename (e.g., pretty.txt) to save to a file.


You can include one or both of these options. The order doesn’t matter.

If the file specified with `-textFile` exists, appointments are loaded from it.
If it does not exist, a new file is created after adding the current appointment.

--------------------------------------------------
Required arguments
--------------------------------------------------

  <owner>        The person who owns the appointment book (example: "Tanya")
  <description>  A short description of the appointment (example: "Doctor Visit")
  <beginTime>    When the appointment starts (format: mm/dd/yyyy h:mm a)
  <endTime>      When the appointment ends (format: mm/dd/yyyy h:mm a)

All of these are required unless -README is used by itself.

--------------------------------------------------
Example usage
--------------------------------------------------

Add and print an appointment to the console:
  java -jar target/apptbook-1.0.0.jar -print "Tanya" "Doctor Visit" 07/30/2025 2:00 PM 07/30/2025 3:00 PM

Add and store an appointment in a file:
  java -jar target/apptbook-1.0.0.jar -textFile appts.txt "Tanya" "CS advising" 07/31/2025 9:00 AM 07/31/2025 9:30 AM

Pretty-print an appointment book to the screen:
  java -jar target/apptbook-1.0.0.jar -textFile appts.txt -pretty - "Tanya" "Research Meeting" 08/01/2025 1:00 PM 08/01/2025 2:15 PM

Pretty-print to a file:
  java -jar target/apptbook-1.0.0.jar -textFile appts.txt -pretty pretty.txt "Tanya" "Lab" 08/02/2025 10:00 AM 08/02/2025 11:00 AM

--------------------------------------------------
Error handling
--------------------------------------------------

The program checks for common issues like:

   - Missing arguments
    - Too many arguments
    - Invalid date/time formats
    - Empty description
    - Mismatched owner names between arguments and file contents
    - File I/O errors (e.g., unreadable or malformed files)

 If anything goes wrong, it prints an error message and exits without crashing.

--------------------------------------------------
Classes I implemented
--------------------------------------------------

 Project3.java
     - The main class. It handles parsing the command-line arguments, coordinating the logic, and reporting errors.

   Appointment.java
     - A simple class that stores the description, begin time, and end time of an appointment.

   AppointmentBook.java
     - Represents a collection of appointments for one owner.

   TextParser.java
     - Reads appointments from a text file and constructs an AppointmentBook object.

   TextDumper.java
     - Writes the contents of an AppointmentBook to a text file in a readable format.

   AppointmentTest.java
     - Unit tests for the Appointment class.

   Project1IT.java
     - Integration tests for the full Project2 application, covering file and command-line use cases.

   PrettyPrinter.java
        - Nicely formats and prints an appointment book either to the screen or a file.

   TextDumperTest.java
        - Unit and integration tests for text dumping and reparsing of appointment books.

   TextParserTest.java
        - Tests for parsing valid and invalid appointment book input.

   PrettyPrinterTest.java
         - Tests for proper formatting by the PrettyPrinter.

--------------------------------------------------
Notes
--------------------------------------------------

This is Project 3 of the Appointment Book project. It builds on the basic command-line logic from Project 1 and 2
and adds the ability to load and save appointments from a plain-text file.

This version emphasizes:
  - Working with Java classes
  - File input/output and exception handling
  - Robust command-line argument parsing
  - Data persistence using text files
  - Pretty printing structured output
  - Writing both unit and integration tests

--------------------------------------------------

Author: Tanya Esther Konyukhova
Portland State University


This is a README file!
README for Project 2: Appointment Book
CS410 Joy of Coding – Summer 2025
Tanya Konyukhova

--------------------------------------------------
What this program does
--------------------------------------------------

This is a command-line Java application that creates an appointment book for a person.
You give it a name (the owner), a description, a begin time, and an end time.
The program stores the appointment in an appointment book and can print it out
if the -print option is used.

This version builds upon Project 1 by introducing basic file support.
It allows appointments to be read from and written to a plain text file using `-textFile`.

The application still focuses on command-line interfaces and core Java concepts,
but now introduces file I/O, parsing, and more robust input validation.

--------------------------------------------------
How to run it
--------------------------------------------------

First, build the project with Maven:

  ./mvnw verify

Then, you can run the program like this:

  java -jar target/apptbook-1.0.0.jar [options] <owner> <description> <beginTime> <endTime>

--------------------------------------------------
Command line options
--------------------------------------------------

  -README   Prints out this README and exits.
  -print    Prints the appointment that was added.
  -textFile     Specifies a text file to read from or write to (must be followed by a filename)

You can include one or both of these options. The order doesn’t matter.

If the file specified with `-textFile` exists, appointments are loaded from it.
If it does not exist, a new file is created after adding the current appointment.

--------------------------------------------------
Required arguments
--------------------------------------------------

  <owner>        The person who owns the appointment book (example: "Tanya")
  <description>  A short description of the appointment (example: "Doctor Visit")
  <beginTime>    When the appointment starts (format: mm/dd/yyyy HH:mm)
  <endTime>      When the appointment ends (format: mm/dd/yyyy HH:mm)

All of these are required unless -README is used by itself.

--------------------------------------------------
Example usage
--------------------------------------------------

java -jar target/apptbook-1.0.0.jar -print "Tanya" "Doctor Visit" 7/18/2025 14:00 7/18/2025 15:00

java -jar target/apptbook-1.0.0.jar -textFile appts.txt -print "Tanya" "CS advising" 7/19/2025 09:00 7/19/2025 09:30

These examples add appointments to either memory or a file, and print the appointment if `-print` is included.

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

 Project2.java
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

--------------------------------------------------
Notes
--------------------------------------------------

This is Project 2 of the Appointment Book project. It builds on the basic command-line logic from Project 1
and adds the ability to load and save appointments from a plain-text file.

This version emphasizes:
  - Working with Java classes
  - File input/output and exception handling
  - Robust command-line argument parsing
  - Data persistence using text files

--------------------------------------------------

Author: Tanya Esther Konyukhova
Portland State University


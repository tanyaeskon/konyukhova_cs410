This is a README file!
README for Project 1: Appointment Book
CS410 Joy of Coding – Summer 2025
Tanya Konyukhova

--------------------------------------------------
What this program does
--------------------------------------------------

This is a command-line Java application that creates an appointment book for a person.
You give it a name (the owner), a description, a begin time, and an end time.
The program stores the appointment in an appointment book and can print it out
if the -print option is used.

At this point, appointments are just stored in memory. Nothing is saved to a file or loaded from a file yet.
This project focuses on getting used to working with classes and command-line arguments.

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

You can include one or both of these options. The order doesn’t matter.

--------------------------------------------------
Required arguments
--------------------------------------------------

  <owner>        The person who owns the appointment book (example: "Tanya")
  <description>  A short description of the appointment (example: "Doctor Visit")
  <beginTime>    When the appointment starts (format: mm/dd/yyyy hh:mm)
  <endTime>      When the appointment ends (format: mm/dd/yyyy hh:mm)

All of these are required unless -README is used by itself.

--------------------------------------------------
Example usage
--------------------------------------------------

  java -jar target/apptbook-1.0.0.jar -print "Tanya" "Doctor Visit" 7/18/2025 14:00 7/18/2025 15:00

This creates an appointment book for Tanya, adds a "Doctor Visit" appointment from 2:00 PM to 3:00 PM,
and prints out the appointment.

--------------------------------------------------
Error handling
--------------------------------------------------

The program checks for common issues like:

  - Missing arguments
  - Too many arguments
  - Invalid date/time formats
  - Empty description

If anything goes wrong, it prints an error message and exits without crashing.

--------------------------------------------------
Classes I implemented
--------------------------------------------------

  Project1.java
    - The main class. It handles parsing the command-line arguments and printing the appointment.

  Appointment.java
    - A simple class that stores the description, begin time, and end time of an appointment.

  AppointmentBook.java
    - Represents a collection of appointments for one owner.

  AppointmentTest.java
    - Unit tests for the Appointment class.

  Project1IT.java
    - Integration tests for the Project1 main program.

--------------------------------------------------
Notes
--------------------------------------------------

This is just Part 1 of the Appointment Book project. Later versions will add features
like reading/writing from files and searching appointments. For now, this version is
all about working with classes, constructors, and strings passed via the command line.

--------------------------------------------------

Author: Tanya Esther Konyukhova
Portland State University


This is a README file!
README for Project 4: A REST-ful Appointment Book Web Service
CS410 Joy of Coding – Summer 2025
Tanya Konyukhova

--------------------------------------------------
What this program does
--------------------------------------------------

This is a Java application that manages an appointment book using a REST-ful web service.
It includes both a command-line client program and a server-side servlet.

The client allows users to add appointments, search for appointments, and pretty-print them.
Appointments can be added to a server, retrieved from it, and displayed in a human-readable format.

You provide the owner’s name, a description, the start time, and the end time.
The appointment is sent to the server and stored in the corresponding appointment book.

Example:
  java -jar target/apptbook-client.jar -host localhost -port 8080 \
    "Tanya" "Doctor Visit" 07/30/2025 2:00 PM 07/30/2025 3:00 PM

This adds the appointment to the server.

--------------------------------------------------
How to run it
--------------------------------------------------

First, build the project with Maven:

    ./mvnw verify

To start the server (Jetty):
    ./mvnw jetty:run

Then, run the client:
    java -jar target/apptbook-client.jar [options] <owner> <description> <beginTime> <endTime>

--------------------------------------------------
Command line options
--------------------------------------------------

  -README   Prints out this README and exits.
  -print    Prints the appointment that was added.
  -search         Searches for appointments between two times.
  -host <name>    Hostname where the server is running.
  -port <number>  Port number the server is listening on.

The -host and -port options are required to connect to the server.
If -search is used, only the owner is required. beginTime and endTime are optional.

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

Add and print an appointment to the server:
  java -jar target/apptbook-client.jar -host localhost -port 8080 -print \
    "Tanya" "Doctor Visit" 07/30/2025 2:00 PM 07/30/2025 3:00 PM

Search for appointments between two times:
  java -jar target/apptbook-client.jar -host localhost -port 8080 -search \
    "Tanya" 08/01/2025 12:00 AM 08/02/2025 11:59 PM

Pretty-print all appointments for a user:
  java -jar target/apptbook-client.jar -host localhost -port 8080 -search "Tanya"

--------------------------------------------------
Error handling
--------------------------------------------------

The program checks for common issues like:

   - Missing arguments
    - Too many arguments
    - Invalid date/time formats
    - Empty description
    - Mismatched owner names between arguments and file contents
    - Server connection errors
    - Bad request data or unparseable input

 If anything goes wrong, it prints an error message and exits without crashing.

--------------------------------------------------
Some classes I implemented
--------------------------------------------------

 Project4.java
     - The main class. It parses command-line arguments, communicates with the server, and handles output.

   Appointment.java
     - A simple class that stores the description, begin time, and end time of an appointment.

   AppointmentBook.java
     - Represents a collection of appointments for one owner.

   PrettyPrinter.java
        - Nicely formats and prints an appointment book either to the screen or a file.

   AppointmentBookServlet.java
        - The server-side servlet that handles HTTP GET and POST requests to manage appointment books.

    AppointmentBookRestClient.java
        - Sends HTTP requests (GET and POST) to the server and handles the responses.


--------------------------------------------------
Notes
--------------------------------------------------

This is Project 4 of the Appointment Book project. It builds on the basic logic from Projects 1–3
and adds the ability to interact with a server using RESTful HTTP requests.

This version emphasizes:
   - Java web application development
   - Client-server communication over HTTP
   - RESTful API design
   - Parsing and sending data via query parameters
   - Searching by time range
   - Pretty printing server responses
   - Writing end-to-end integration tests

--------------------------------------------------

Author: Tanya Esther Konyukhova
Portland State University


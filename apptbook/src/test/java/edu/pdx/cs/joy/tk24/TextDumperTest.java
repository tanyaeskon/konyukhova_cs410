package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for the {@link TextDumper} and {@link TextParser} classes.
 * These tests verify correct serialization and deserialization of {@link AppointmentBook} data.
 */
public class TextDumperTest {

  /**
   * Verifies that the owner's name is included in the text output produced by {@link TextDumper}.
   */
  @Test
  void appointmentBookOwnerIsDumpedInTextFormat() {
    String owner = "Test Appointment Book";
    AppointmentBook book = new AppointmentBook(owner);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(book);

    String text = sw.toString();
    assertThat(text, containsString(owner));
  }

  /**
   * Tests that an {@link AppointmentBook} dumped to a file can be read back
   * correctly using {@link TextParser}, preserving the owner's name.
   *
   * @param tempDir Temporary directory injected by JUnit for safe file writing
   */
  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String owner = "Test Appointment Book";
    AppointmentBook book = new AppointmentBook(owner);

    File textFile = new File(tempDir, "apptbook.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(book);

    TextParser parser = new TextParser(new FileReader(textFile));
    AppointmentBook read = parser.parse();
    assertThat(read.getOwnerName(), equalTo(owner));
  }

  /**
   * Verifies that the {@link TextParser} throws a {@link ParserException}
   * when given malformed appointment data without the expected pipe separators.
   */
  @Test
  void parserThrowsOnMalformedLine() {
    String malformed = """
      Tanya
      Incomplete appointment line with no separator
      """;

    TextParser parser = new TextParser(new StringReader(malformed));
    org.junit.jupiter.api.Assertions.assertThrows(ParserException.class, parser::parse);
  }

  /**
   * Tests that an appointment with a description and proper date/time strings
   * can be dumped and then parsed correctly, preserving all appointment details.
   */
  @Test
  void testFirstAppointmentDescription() throws ParserException, IOException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

    LocalDateTime begin = LocalDateTime.parse("07/25/2025 9:00 AM", formatter);
    LocalDateTime end = LocalDateTime.parse("07/25/2025 10:00 AM", formatter);



    AppointmentBook book = new AppointmentBook("Tanya");
    book.addAppointment(new Appointment("Dentist", begin, end));

    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(book);

    StringReader reader = new StringReader(writer.toString());
    AppointmentBook parsed = new TextParser(reader).parse();

    assertThat(parsed.getOwnerName(), equalTo("Tanya"));
    assertThat(parsed.getAppointments().size(), equalTo(1));

    DateTimeFormatter localized = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);



    for (Appointment appt : parsed.getAppointments()) {
      assertThat(appt.getDescription(), equalTo("Dentist"));
      assertThat(appt.getBeginTimeString(), equalTo("07/25/25 9:00 AM"));
      assertThat(appt.getEndTimeString(), equalTo("07/25/25 10:00 AM"));


    }
  }
}

package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for verifying that TextDumper and TextParser work together correctly.
 * Tests the round-trip conversion of appointment books to text and back.
 */
public class TextDumperParserTest {

  /**
   * Tests that an appointment book can be dumped to text format and then
   * parsed back into an equivalent appointment book. Verifies that all
   * appointment data is preserved through the dump and parse cycle.
   *
   * @throws ParserException if parsing the dumped text fails
   */
  @Test
  void dumpedTextCanBeParsedIntoSameAppointment() throws ParserException {
    String owner = "Tanya";
    String description = "Dentist Appointment";
    String begin = "08/06/2025 9:00 AM";
    String end = "08/06/2025 10:00 AM";

    AppointmentBook book = new AppointmentBook(owner);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
    LocalDateTime beginTime = LocalDateTime.parse(begin, formatter);
    LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    book.addAppointment(new Appointment(description, beginTime, endTime));

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(book);

    String text = sw.toString();
    TextParser parser = new TextParser(new StringReader(text));
    AppointmentBook parsedBook = parser.parse();

    assertThat(parsedBook.getOwnerName(), equalTo(book.getOwnerName()));
    assertThat(parsedBook.getAppointments().size(), equalTo(book.getAppointments().size()));

    Appointment parsedAppt = parsedBook.getAppointments().iterator().next();
    Appointment originalAppt = book.getAppointments().iterator().next();

    assertThat(parsedAppt.getDescription(), equalTo(originalAppt.getDescription()));
    assertThat(parsedAppt.getBeginTime(), equalTo(originalAppt.getBeginTime()));
    assertThat(parsedAppt.getEndTime(), equalTo(originalAppt.getEndTime()));
  }
}

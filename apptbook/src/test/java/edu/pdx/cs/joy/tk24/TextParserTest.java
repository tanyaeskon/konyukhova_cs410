package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

  /**
   * Tests that a valid appointment book text file can be successfully parsed.
   * Expects the parsed object to contain the correct owner name.
   *
   * @throws ParserException If parsing fails unexpectedly.
   */
  @Test
  void validTextFileCanBeParsed() throws ParserException {
    InputStream resource = getClass().getResourceAsStream("valid-apptbook.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    AppointmentBook book = parser.parse();
    assertThat(book.getOwnerName(), equalTo("Test Appointment Book"));
  }

  /**
   * Tests that an invalid text file (in this case, empty or malformed)
   * causes the {@link TextParser} to throw a {@link ParserException}.
   */
  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-apptbook.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  /**
   * Tests that input with only an owner line (no appointments)
   * results in a valid, empty {@link AppointmentBook}.
   *
   * @throws ParserException If parsing fails unexpectedly.
   */
  @Test
  void parsesOwnerOnlyTextAsEmptyAppointmentBook() throws ParserException {
    String input = "Test Appointment Book\n";
    TextParser parser = new TextParser(new StringReader(input));
    AppointmentBook book = parser.parse();

    assertThat(book.getOwnerName(), equalTo("Test Appointment Book"));
    assertThat(book.getAppointments().size(), equalTo(0));
  }
}

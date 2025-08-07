package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the {@link AppointmentBookRestClient} class.
 * This test class verifies that the REST client correctly communicates with
 * the appointment book web service and properly handles HTTP requests and responses.
 */
public class AppointmentBookRestClientTest {

  /**
   * Tests that getAllAppointmentEntries performs an HTTP GET request
   * with the correct owner parameter and returns the expected response content.
   *
   * @throws ParserException if parsing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void getAllAppointmentEntriesPerformsHttpGetWithOwnerParameter() throws ParserException, IOException {

    AppointmentBook testBook = new AppointmentBook("TestOwner");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
    LocalDateTime beginTime = LocalDateTime.parse("08/06/2025 9:00 AM", formatter);
    LocalDateTime endTime = LocalDateTime.parse("08/06/2025 10:00 AM", formatter);
    testBook.addAppointment(new Appointment("Test Meeting", beginTime, endTime));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    String expectedOwner = "testOwner";

    when(http.get(eq(Map.of(AppointmentBookServlet.OWNER_PARAMETER, expectedOwner)))) .thenReturn(appointmentDataAsText(testBook));

    AppointmentBookRestClient client = new AppointmentBookRestClient(http);

    String result = client.getAllAppointmentEntries(expectedOwner);
    String expected = appointmentDataAsText(testBook).getContent();

    assertThat(result, equalTo(expected));
  }

  /**
   * Helper method that converts an appointment book to a text response format
   * using the same TextDumper format that the real server uses.
   *
   * @param book the appointment book to convert to text format
   * @return an HTTP response containing the appointment book data as text
   */
  private HttpRequestHelper.Response appointmentDataAsText(AppointmentBook book) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(book);
    return new HttpRequestHelper.Response(writer.toString());
  }

}

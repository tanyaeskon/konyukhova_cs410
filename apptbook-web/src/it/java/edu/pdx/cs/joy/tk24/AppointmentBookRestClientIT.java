package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@TestMethodOrder(MethodName.class)
class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  /**
   * Creates a new {@link AppointmentBookRestClient} configured to connect
   * to the test server.
   *
   * @return A new REST client instance
   */
  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  /**
   * Tests that all appointment entries can be removed from the server.
   * This test runs first to ensure a clean state for subsequent tests.
   *
   * @throws IOException if there is an error communicating with the server
   */
  @Test
  void test0RemoveAllAppointmentEntries() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    client.removeAllAppointmentEntries();
  }

  /**
   * Tests that an empty server contains no appointments for a given owner.
   * Verifies that querying for a non-existent appointment book returns
   * an empty appointment book.
   *
   * @throws IOException if there is an error communicating with the server
   * @throws ParserException if there is an error parsing the server response
   */
  @Test
  void test1EmptyServerContainsNoAppointments() throws IOException, ParserException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "TEST WORD";
    AppointmentBook book = client.getAppointmentBook(owner);
    assertThat(book.getAppointments().size(), equalTo(0));

  }

  /**
   * Tests adding a single appointment to the server and verifying it can be retrieved.
   * Creates an appointment with valid data and confirms that the appointment
   * is stored correctly with the proper owner, description, and begin time.
   *
   * @throws IOException if there is an error communicating with the server
   * @throws ParserException if there is an error parsing the server response
   */
  @Test
  void test2AddOneAppointment() throws IOException, ParserException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TEST WORD";
    String description = "TEST DEFINITION";
    String beginTime = "08/06/2025 9:00 AM";
    String endTime = "08/06/2025 10:00 AM";

    client.addAppointment(owner, description, beginTime, endTime);

    AppointmentBook book = client.getAppointmentBook(owner);
    Appointment appt = book.getAppointments().iterator().next();

    assertThat(book.getOwnerName(), equalTo(owner));
    assertThat(appt.getDescription(), equalTo(description));
    assertThat(appt.getBeginTimeString(), containsString("08/06/2025"));

  }

  /**
   * Tests that attempting to add an appointment with missing required fields
   * throws an appropriate exception. Verifies that the server properly validates
   * input and returns a meaningful error response when required parameters are missing.
   *
   * @throws RestException when required parameters are missing (expected behavior)
   */
  @Test
  void test3AddAppointmentWithMissingFieldsThrowsException() {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String emptyString = "";

    RestException ex = assertThrows(RestException.class, () -> client.addAppointment("", "", "", "")
    );

    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), containsString("Missing required parameter"));

  }
}


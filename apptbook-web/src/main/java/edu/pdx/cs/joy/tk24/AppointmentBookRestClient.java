package edu.pdx.cs.joy.tk24;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper.Response;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the appointment book REST service. This client provides
 * methods to interact with the appointment book web service, including adding appointments,
 * searching for appointments, and managing appointment books through HTTP requests.
 *
 * <p>The client communicates with a REST service that manages appointment books for
 * different owners and supports operations like creating, reading, and deleting appointments.</p>
 */
public class AppointmentBookRestClient {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final HttpRequestHelper http;


  /**
   * Creates a client to the appointment book REST service running on the given host and port.
   * The client will connect to the service at http://hostName:port/apptbook/appointments.
   *
   * @param hostName The name of the host where the service is running
   * @param port     The port number on which the service is listening
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
  }

  /**
   * Creates a client with a custom HTTP request helper. This constructor is primarily
   * used for testing purposes to inject a mock HTTP helper.
   *
   * @param http The HTTP request helper to use for making REST calls
   */
  @VisibleForTesting
  AppointmentBookRestClient(HttpRequestHelper http) {
    this.http = http;
  }

  /**
   * Retrieves all appointment entries for the specified owner from the server.
   * The appointments are returned as a text representation formatted using TextDumper.
   *
   * @param owner The name of the appointment book owner
   * @return A string containing all appointments for the owner in text format
   * @throws IOException if there is an error communicating with the server
   * @throws ParserException if there is an error parsing the server response
   */
  public String getAllAppointmentEntries(String owner) throws IOException, ParserException {
    Response response = http.get(Map.of(AppointmentBookServlet.OWNER_PARAMETER, owner));
    throwExceptionIfNotOkayHttpStatus(response);
    return response.getContent();

  }

  /**
   * Adds a new appointment to the server for the specified owner.
   * If an appointment book for the owner doesn't exist, a new one will be created.
   *
   * @param owner The name of the appointment book owner
   * @param description A description of the appointment
   * @param beginTime The start time of the appointment in MM/dd/yyyy h:mm AM/PM format
   * @param endTime The end time of the appointment in MM/dd/yyyy h:mm AM/PM format
   * @throws IOException if there is an error communicating with the server
   * @throws RestException if the server returns an error response (e.g., invalid parameters)
   */
  public void addAppointment(String owner, String description, String beginTime, String endTime) throws IOException {
    Response response = postToMyURL(Map.of(
            AppointmentBookServlet.OWNER_PARAMETER, owner,
            AppointmentBookServlet.DESCRIPTION_PARAMETER, description,
            AppointmentBookServlet.BEGIN_PARAMETER, beginTime,
            AppointmentBookServlet.END_PARAMETER, endTime
    ));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Makes a POST request to the appointment book service with the provided parameters.
   * This method is used internally by other methods to add appointments.
   *
   * @param dictionaryEntries A map of parameter names to values to send in the POST request
   * @return The HTTP response from the server
   * @throws IOException if there is an error communicating with the server
   */
  @VisibleForTesting
  Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {
    return http.post(dictionaryEntries);
  }

  /**
   * Removes all appointment entries from the server. This operation affects all
   * appointment books for all owners and is primarily used for testing purposes.
   *
   * @throws IOException if there is an error communicating with the server
   * @throws RestException if the server returns an error response
   */
  public void removeAllAppointmentEntries() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Checks the HTTP status code of a response and throws a RestException if
   * the status indicates an error condition (not HTTP 200 OK).
   *
   * @param response The HTTP response to check
   * @throws RestException if the response status code is not HTTP 200 OK
   */
  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

  /**
   * Retrieves appointments for the specified owner that begin between the given
   * start and end times. The appointments are returned as a text representation
   * formatted using TextDumper.
   *
   * @param owner The name of the appointment book owner
   * @param beginTime The earliest start time for appointments to include (MM/dd/yyyy h:mm AM/PM format)
   * @param endTime The latest start time for appointments to include (MM/dd/yyyy h:mm AM/PM format)
   * @return A string containing matching appointments in text format
   * @throws IOException if there is an error communicating with the server
   */
  public String getAppointmentsBetween(String owner, String beginTime, String endTime) throws IOException {
    Response response = http.get(Map.of(
            AppointmentBookServlet.OWNER_PARAMETER, owner,
            AppointmentBookServlet.BEGIN_PARAMETER, beginTime,
            AppointmentBookServlet.END_PARAMETER, endTime
    ));
    throwExceptionIfNotOkayHttpStatus(response);
    return response.getContent();
  }


  /**
   * Retrieves and parses the complete appointment book for the specified owner.
   * The server response is parsed into an AppointmentBook object that can be
   * used programmatically.
   *
   * @param owner The name of the appointment book owner
   * @return An AppointmentBook object containing all appointments for the owner
   * @throws IOException if there is an error communicating with the server
   * @throws ParserException if there is an error parsing the server response
   * @throws IllegalArgumentException if the owner parameter is null or empty
   */
  public AppointmentBook getAppointmentBook(String owner) throws IOException, ParserException {
    if (owner == null || owner.isEmpty()) {
      throw new IllegalArgumentException("Owner cannot be null or empty");
    }

    Response response = this.http.get(Map.of(AppointmentBookServlet.OWNER_PARAMETER, owner));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();
    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();

  }
}

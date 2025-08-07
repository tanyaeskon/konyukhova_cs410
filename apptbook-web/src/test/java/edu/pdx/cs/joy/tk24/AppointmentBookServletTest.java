package edu.pdx.cs.joy.tk24;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  /**
   * Tests that a newly created servlet contains no appointment books.
   * Verifies that a GET request to an empty servlet returns successfully
   * without writing any appointment data.
   *
   * @throws ServletException if servlet processing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void initiallyServletContainsNoAppointmentBooks() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(pw, never()).println(anyString());
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Tests adding a single appointment to the appointment book via POST request.
   * Verifies that the appointment is stored correctly and can be retrieved,
   * and that the response contains the expected confirmation message.
   *
   * @throws ServletException if servlet processing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void addOneAppointmentToAppointmentBook() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";
    String beginTime = "01/01/2025 1:00 PM";
    String endTime = "01/01/2025 2:00 PM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);
    when(request.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn(description);
    when(request.getParameter(AppointmentBookServlet.BEGIN_PARAMETER)).thenReturn(beginTime);
    when(request.getParameter(AppointmentBookServlet.END_PARAMETER)).thenReturn(endTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString("Added appointment"));
    assertThat(stringWriter.toString(), containsString(description));
    assertThat(stringWriter.toString(), containsString(owner));

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    AppointmentBook book = servlet.getAppointmentBook(owner);
    assertThat(book.getOwnerName(), equalTo(owner));
    assertThat(new ArrayList<>(book.getAppointments()).get(0).getDescription(), equalTo(description));
  }

  /**
   * Tests retrieving an appointment book for an existing owner via GET request.
   * First adds an appointment, then verifies that a GET request returns
   * the appointment data in the expected format.
   *
   * @throws ServletException if servlet processing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void getAppointmentBookForExistingOwner() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";
    String beginTime = "01/01/2025 1:00 PM";
    String endTime = "01/01/2025 2:00 PM";

    HttpServletRequest postRequest = mock(HttpServletRequest.class);
    when(postRequest.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);
    when(postRequest.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn(description);
    when(postRequest.getParameter(AppointmentBookServlet.BEGIN_PARAMETER)).thenReturn(beginTime);
    when(postRequest.getParameter(AppointmentBookServlet.END_PARAMETER)).thenReturn(endTime);

    HttpServletResponse postResponse = mock(HttpServletResponse.class);
    StringWriter postWriter = new StringWriter();
    PrintWriter postPw = new PrintWriter(postWriter, true);
    when(postResponse.getWriter()).thenReturn(postPw);

    servlet.doPost(postRequest, postResponse);

    HttpServletRequest getRequest = mock(HttpServletRequest.class);
    when(getRequest.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);

    HttpServletResponse getResponse = mock(HttpServletResponse.class);
    StringWriter getWriter = new StringWriter();
    PrintWriter getPw = new PrintWriter(getWriter, true);
    when(getResponse.getWriter()).thenReturn(getPw);

    servlet.doGet(getRequest, getResponse);

    verify(getResponse).setStatus(HttpServletResponse.SC_OK);
    String output = getWriter.toString();
    assertThat(output, containsString(owner));
    assertThat(output, containsString(description));
  }

  /**
   * Tests retrieving an appointment book for a non-existent owner.
   * Verifies that the servlet handles requests for owners that don't
   * have appointment books gracefully.
   *
   * @throws ServletException if servlet processing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void getAppointmentBookForNonExistentOwner() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn("NonExistentOwner");

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter writer = new StringWriter();
    PrintWriter pw = new PrintWriter(writer, true);
    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    String output = writer.toString();
    assertThat(output, containsString("NonExistentOwner"));
  }

  /**
   * Tests that a POST request with a missing owner parameter returns
   * an appropriate error response. Verifies proper validation of required parameters.
   *
   * @throws ServletException if servlet processing fails
   * @throws IOException if an I/O error occurs
   */
  @Test
  void postAppointmentMissingOwner() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(null);
    when(request.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn("Description");
    when(request.getParameter(AppointmentBookServlet.BEGIN_PARAMETER)).thenReturn("01/01/2025 1:00 PM");
    when(request.getParameter(AppointmentBookServlet.END_PARAMETER)).thenReturn("01/01/2025 2:00 PM");

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).sendError(eq(HttpServletResponse.SC_PRECONDITION_FAILED),
            contains("Missing required parameter: owner"));
  }
}

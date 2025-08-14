package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.UncaughtExceptionInMain;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static java.util.function.Predicate.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    /**
     * Removes all appointment entries from the server to ensure a clean test environment.
     * This test runs first to clear any existing data before running other tests.
     *
     * @throws IOException if there is an error communicating with the server
     */
    @Test
    void test0RemoveAllMappings() throws IOException {
      AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllAppointmentEntries();
    }

    /**
     * Tests that invoking Project4 with no command line arguments produces
     * an appropriate error message. Verifies that the application properly
     * handles the case when required arguments are missing.
     */
    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
    }

    /**
     * Tests adding an appointment to an empty server and then searching for it.
     * This test verifies the complete workflow of adding an appointment and
     * retrieving it through a search operation with specific date/time range.
     */
    @Test
    void test2EmptyServer() {
        String owner = "Test Owner";

        MainMethodResult add = invokeMain(Project4.class,
                "-host", HOSTNAME, "-port", PORT,
                owner, "Integration Test Appointment",
                "08/06/2025", "9:00", "AM", "08/06/2025", "10:00", "AM");


        assertThat(add.getTextWrittenToStandardError(), equalTo(""));

        MainMethodResult result = invokeMain(Project4.class,
                "-host", HOSTNAME, "-port", PORT, "-search",
                owner, "08/06/2025", "9:00", "AM", "08/06/2025", "10:00", "AM");


        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String output = result.getTextWrittenToStandardOut();
        assertThat(output, containsString("Integration Test Appointment"));
    }

    /**
     * Tests that providing insufficient command line arguments results in
     * an appropriate error message. Verifies that the application properly
     * validates the required number of arguments for adding an appointment.
     */
    @Test
    void test3MissingFieldsThrowsRestException() {
        String owner = "WORD";

        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, owner);

        assertThat(result.getTextWrittenToStandardError(), containsString("Expected: owner description begin end"));

    }

    /**
     * Tests adding an appointment with the -print flag to verify that the
     * appointment details are properly formatted and displayed. Confirms
     * that the pretty printer functionality works correctly when adding
     * new appointments.
     */
    @Test
    void test4AddDefinition() {
        String owner = "Test Owner";
        String description = "Meeting with team";

        MainMethodResult result = invokeMain(Project4.class,
                "-host", HOSTNAME, "-port", PORT, "-print",
                owner, description, "08/06/2025", "9:00", "AM", "08/06/2025", "10:00", "AM");

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, containsString(description));
        assertThat(out, containsString("08/06/2025"));

    }

    /**
     * Tests the -README command line option to verify that it displays
     * comprehensive usage information. Ensures that all major command line
     * options and their descriptions are included in the README output.
     */
    @Test
    void test5ReadmeOption() {
        MainMethodResult result = invokeMain(Project4.class, "-README");

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String output = result.getTextWrittenToStandardOut();
        assertThat(output, containsString("Project4: Appointment Book Application"));
        assertThat(output, containsString("Usage:"));
        assertThat(output, containsString("-README"));
        assertThat(output, containsString("-host"));
        assertThat(output, containsString("-port"));
        assertThat(output, containsString("-search"));
        assertThat(output, containsString("-print"));
    }

    /**
     * Tests error handling when a host is specified without a corresponding port.
     * Verifies that the application properly validates that host and port
     * arguments must be provided together and produces an appropriate error
     * message when this requirement is violated.
     */
    @Test
    void test6MissingHostWithoutPort() {
        String owner = "Test Owner";

        MainMethodResult result = invokeMain(Project4.class,
                "-host", HOSTNAME,
                owner, "Test Meeting",
                "08/06/2025", "9:00", "AM", "08/06/2025", "10:00", "AM");

        assertThat(result.getTextWrittenToStandardError(), containsString("Cannot specify host without port"));
    }

    @Test
    void debugTest3Command() {
        // This exactly matches Test 3 from the grader
        String[] args = {
                "-host", "localhost", "-port", "42346",
                "Project4", "This is Test 3",
                "01/06/2025", "6:00", "PM",
                "01/06/2025", "6:52", "PM"
        };

        MainMethodResult result = invokeMain(Project4.class, args);

        System.err.println("STDERR: " + result.getTextWrittenToStandardError());
        System.err.println("STDOUT: " + result.getTextWrittenToStandardOut());

        assertThat(result.getTextWrittenToStandardError(), Matchers.not(containsString("Expected: owner description begin end")));
    }


    /*
    @Test
    void graderTest3Exact() {
        MainMethodResult result = invokeMain(Project4.class,
                "-host", "localhost", "-port", "42346",
                "Project4", "This is Test 3",
                "01/06/2025", "6:00", "PM",
                "01/06/2025", "6:52", "PM");

        assertThat(result.getTextWrittenToStandardError(), containsString("While contacting server"));
        assertThat(result.getTextWrittenToStandardError(), not(containsString("Expected: owner description begin end")));
    }
     */

}
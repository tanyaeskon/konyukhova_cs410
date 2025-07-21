package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

  /**
   * Helper method to invoke the {@link Project1#main(String[])} method with given arguments.
   *
   * @param args The command line arguments to pass
   * @return A {@code MainMethodResult} containing the output from stdout and stderr
   */
  private MainMethodResult invokeMain(String... args) {

    return invokeMain( Project1.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  /**
   * Tests that the -README option prints the README and exits successfully.
   */
  @Test
  void testReadmeOptionPrintsReadmeAndExits() {
    MainMethodResult result = invokeMain("-README");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Appointment Book Application"));
  }

  /**
   * Tests that the -print option causes the appointment to be printed to standard output.
   */
  @Test
  void testPrintsAppointmentWhenPrintOptionIsUsed() {
    MainMethodResult result = invokeMain(
            "-print",
            "Tanya",
            "Doctor Visit",
            "7/18/2025", "14:00",
            "7/18/2025", "15:00"
    );
    assertThat(result.getTextWrittenToStandardOut(), containsString("Doctor Visit"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("7/18/2025 14:00"));

    //assertThat(result.getTextWrittenToStandardOut(), containsString("7/18/2025"));
   // assertThat(result.getTextWrittenToStandardout(), containsString("14:00"));
  }

  /**
   * Tests that an invalid begin time format triggers an appropriate error message.
   */
  @Test
  void testInvalidBeginTimeFormatPrintsError() {
    MainMethodResult result = invokeMain(
            "Tanya",
            "Doctor Visit",
            "not-a-date", "bad time",
            "7/18/2025", "15:00"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin time format"));
  }

  /**
   * Tests that missing one or more required arguments produces a helpful error message.
   */
  @Test
  void testMissingArgumentsShowsHelpfulError() {
    MainMethodResult result = invokeMain("Tanya", "Doctor Visit", "7/18/2025", "14:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  /**
   * Tests that supplying too many arguments results in an error message.
   */
  @Test
  void testTooManyArgumentsShowsHelpfulError() {
    MainMethodResult result = invokeMain(
            "Tanya",
            "Doctor Visit",
            "7/18/2025", "14:00",
            "7/18/2025", "15:00",
            "ExtraArg"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
  }

  /**
   * Tests that omitting the end time argument results in an error about missing arguments.
   */
  @Test
  void testMissingEndTimeArgument() {
    MainMethodResult result = invokeMain(
            "Tanya",
            "Doctor Visit",
            "7/18/2025", "14:00"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }


  /**
   * Tests that providing an invalid format for the end time results in an error message.
   */
  @Test
  void testInvalidEndTimeFormatPrintsError() {
    MainMethodResult result = invokeMain(
            "Tanya",
            "Doctor Visit",
            "7/18/2025", "14:00",
            "bad-end-time", "bad"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid end time format"));
  }


  /**
   * Tests that supplying an unknown option (not -README or -print) produces an error.
   */
  @Test
  void testUnknownOptionTriggersError() {
    MainMethodResult result = invokeMain(
            "-fred",
            "Tanya",
            "Doctor Visit",
            "7/18/2025", "14:00",
            "7/18/2025",  "15:00"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown command line option"));
  }



}
package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Integration tests for the {@link Project3} main class.
 */
class Project1IT extends InvokeMainTestCase {

  /**
   * Helper method to invoke the {@link Project3#main(String[])} method with given arguments.
   *
   * @param args The command line arguments to pass
   * @return A {@code MainMethodResult} containing the output from stdout and stderr
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
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
            "07/18/2025", "2:00", "PM",
            "07/18/2025", "3:00", "PM"
    );
    assertThat(result.getTextWrittenToStandardOut(), containsString("Doctor Visit"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("07/18/2025 2:00 PM"));
    assertThat(result.getTextWrittenToStandardOut(), containsString("7/18/2025 3:00 PM"));
  }

  /**
   * Tests that an invalid begin time format triggers an appropriate error message.
   */
  @Test
  void testInvalidBeginTimeFormatPrintsError() {
    MainMethodResult result = invokeMain(
            "Tanya",
            "Doctor Visit",
            "not-a-date", "bad", "AM",
            "07/18/2025", "3:00", "PM"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid begin time format"));
  }

  /**
   * Tests that missing one or more required arguments produces a helpful error message.
   */
  @Test
  void testMissingArgumentsShowsHelpfulError() {
    MainMethodResult result = invokeMain("Tanya", "Doctor Visit", "07/18/2025", "2:00", "PM");
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
            "07/18/2025", "2:00", "PM",
            "07/18/2025", "3:00", "PM",
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
            "07/18/2025", "2:00", "PM"
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
            "07/18/2025", "2:00", "PM",
            "bad-end-time", "bad", "PM"
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
            "07/18/2025", "2:00", "PM",
            "07/18/2025", "3:00", "PM"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Unknown command line option"));
  }

  /**
   * Tests that parsing an empty appointment book file throws a ParserException.
   */
  @Test
  void emptyAppointmentBookThrows() {
    InputStream input = getClass().getResourceAsStream("/edu/pdx/cs/joy/tk24/empty-apptbook.txt");
    assertNotNull(input, "Could not load empty-apptbook.txt");

    TextParser parser = new TextParser(new InputStreamReader(input));
    assertThrows(ParserException.class, parser::parse);
  }

  /**
   * Tests writing an appointment to a file using the -textFile option and
   * verifies the contents were correctly written.
   *
   * @param tempDir A temporary directory provided by JUnit for safely creating test files.
   * @throws IOException If file writing or reading fails.
   */
  @Test
  void testWriteAppointmentToTextFileWithTextFileOption(@TempDir Path tempDir) throws IOException {
    //Create a temporary file for the text file
    File textFile = tempDir.resolve("apptbook.txt").toFile();

    //Run the main program with -textFile option
    MainMethodResult result = invokeMain(
            "-textFile", textFile.getAbsolutePath(),
            "Tanya",
            "Study Session",
            "07/26/2025", "1:00", "PM",
            "07/26/2025", "2:00", "PM"
    );

    assertTrue(textFile.exists());

    String content = Files.readString(textFile.toPath());

    assertThat(content, containsString("Tanya"));
    assertThat(content, containsString("Study Session"));
    assertThat(content, containsString("07/26/2025"));
    assertThat(content, containsString("1:00"));
  }

  /**
   * Tests that the program detects an owner mismatch between the appointment book file
   * and the command-line argument.
   *
   * @param tempDir A temporary directory for test file creation.
   * @throws IOException If file I/O fails.
   */
  @Test
  void testOwnerMismatchBetweenFileAndCommandLine(@TempDir Path tempDir) throws IOException {
    File textFile = tempDir.resolve("apptbook.txt").toFile();

    //Write an existing appointment with owner "Alice"
    Files.writeString(textFile.toPath(), """
      Alice
      Meeting|07/25/2025 01:00 PM |07/25/2025 02:00 PM
      """);

    //Now run the program with a different owner name
    MainMethodResult result = invokeMain(
            "-textFile", textFile.getAbsolutePath(),
            "Bob",
            "Lunch",
            "07/26/2025", "12:00", "PM",
            "07/26/2025", "1:00", "PM"
    );

    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid owner name"));
  }

  /**
   * Tests that a malformed line in the appointment file (missing separator "|")
   * triggers a parser exception with an appropriate error message.
   *
   * @param tempDir A temporary directory for the malformed test file.
   * @throws IOException If file I/O fails.
   */
  @Test
  void testMalformedLineTriggersParserException(@TempDir Path tempDir) throws IOException {
    File textFile = tempDir.resolve("apptbook.txt").toFile();

    //Write bad input: Missing separator "|"
    Files.writeString(textFile.toPath(), """
      Tanya
      This is a bad line with no separator
      """);

    MainMethodResult result = invokeMain(
            "-textFile", textFile.getAbsolutePath(),
            "Tanya",
            "Lunch",
            "08/02/2025", "12:00", "PM",
            "08/02/2025", "1:00", "PM"
    );
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid line format"));
  }

  /**
   * Tests that providing the -textFile option without a filename
   * prints an appropriate error message.
   */
  @Test
  void testMissingFilenameAfterTextFileOption() {
    MainMethodResult result = invokeMain("-textFile");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing filename after -textFile"));
  }

  @Test
  void testPrettyPrintToStandardOutWithTextFile(@TempDir Path tempDir) throws IOException {
    // Recreate Test 7 scenario
    File textFile = tempDir.resolve("test.txt").toFile();

    MainMethodResult result = invokeMain(
            "-textFile", textFile.getAbsolutePath(),
            "-pretty", "-",  // This was failing before
            "Project3",
            "Test 7",
            "01/08/2025", "8:00", "AM",
            "01/08/2025", "8:15", "AM"
    );

    String output = result.getTextWrittenToStandardOut();
    assertThat(output, containsString("Appointment Book for: Project3"));
    assertThat(output, containsString("Test 7"));
    assertThat(output, containsString("Duration"));
  }
}
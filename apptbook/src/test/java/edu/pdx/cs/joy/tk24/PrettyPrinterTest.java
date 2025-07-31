package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;


/**
 * Unit and integration tests for the {@link PrettyPrinter} class.
 *
 * Verifies correct formatting, file output, and content of pretty-printed appointment books.
 */
public class PrettyPrinterTest extends InvokeMainTestCase {

    /**
     * Helper method to invoke the {@link Project3#main(String[])} method with given arguments.
     *
     * @param args Command-line arguments
     * @return The result of executing the main method
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project3.class, args);
    }

    /**
     * Tests that the {@link PrettyPrinter} constructor correctly initializes with a {@link StringWriter}.
     */
    @Test
    void testPrettyPrinterConstructor() {
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);
        assertNotNull(printer);
    }

    /**
     * Tests that dumping an empty {@link AppointmentBook} still produces output with the owner's name.
     *
     * @throws IOException If writing fails
     */
    @Test
    void testDumpEmptyAppointmentBook() throws IOException {
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);

        AppointmentBook book = new AppointmentBook("John Doe");
        printer.dump(book);

        String output = writer.toString();
        assertThat(output, containsString("Appointment Book for: John Doe"));
    }

    /**
     * Tests that dumping a book with one appointment includes all details,
     * such as description, duration, and owner name.
     *
     * @throws IOException If writing fails
     */
    @Test
    void testDumpAppointmentBookWithOneAppointment() throws IOException{
        StringWriter writer = new StringWriter();
        PrettyPrinter printer = new PrettyPrinter(writer);

        AppointmentBook book = new AppointmentBook("Tanya Konyukhova");
        LocalDateTime begin = LocalDateTime.of(2025, 7, 30, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 30, 15, 30);

        Appointment appointment = new Appointment("Doctor Visit", begin, end);
        book.addAppointment(appointment);

        printer.dump(book);

        String output = writer.toString();
        assertThat(output, containsString("Appointment Book for: Tanya Konyukhova"));
        assertThat(output, containsString("Doctor Visit"));
        assertThat(output, containsString("Duration"));
        assertThat(output, containsString("90")); // 90 minutes duration
    }

    /**
     * Tests pretty-printing an appointment to a file using the -pretty option.
     * Verifies that the file is created and includes expected appointment data.
     *
     * @param tempDir A temporary directory provided by JUnit for test files
     * @throws IOException If file writing fails
     */
    @Test
    void testPrettyPrintToFile(@TempDir Path tempDir) throws IOException {
        File prettyFile = tempDir.resolve("pretty.txt").toFile();

        InvokeMainTestCase.MainMethodResult result = invokeMain(
                "-pretty", prettyFile.getAbsolutePath(),
                "Alice",
                "Project Review",
                "08/01/2025", "2:00", "PM",
                "08/01/2025", "3:30", "PM"
        );

        assertTrue(prettyFile.exists());
        String content = Files.readString(prettyFile.toPath());
        assertThat(content, CoreMatchers.containsString("Appointment Book for: Alice"));
        assertThat(content, CoreMatchers.containsString("Project Review"));
        assertThat(content, CoreMatchers.containsString("90")); // 90 minutes
    }
}




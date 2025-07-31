package edu.pdx.cs.joy.tk24;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


/**
 * Unit tests for the {@link Appointment} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AppointmentTest {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");


  @Test
  void beginTimeStringReturnsCorrectValue() {
    LocalDateTime begin = LocalDateTime.parse("07/18/2025 2:00 PM", formatter);
    LocalDateTime end = LocalDateTime.parse("07/18/2025 3:00 PM", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getDescription(), is("Doctor appointment"));
  }

  /**
   * Tests that the description passed into the constructor is stored correctly.
   */
  @Test
  void descriptionIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("07/18/2025 2:00 PM", formatter);
    LocalDateTime end = LocalDateTime.parse("07/18/2025 3:00 PM", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getDescription(), is("Doctor appointment"));
  }

  /**
   * Tests that the begin time is stored and returned correctly from getBeginTimeString().
   */
  @Test
  void beginTimeIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("07/18/2025 2:00 PM", formatter);
    LocalDateTime end = LocalDateTime.parse("07/18/2025 3:00 PM", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getBeginTime(), is(begin));
    assertThat(appointment.getEndTime(), is(end));
  }

  /**
   * Tests that the end time is stored and returned correctly from getEndTimeString().
   */
  @Test
  void endTimeIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("07/18/2025 2:00 PM", formatter);
    LocalDateTime end = LocalDateTime.parse("07/18/2025 3:00 PM", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getEndTime(), is(end));
  }
}

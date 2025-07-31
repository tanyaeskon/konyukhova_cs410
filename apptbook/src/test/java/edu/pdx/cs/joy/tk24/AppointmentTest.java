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

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm");

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void getBeginTimeStringNeedsToBeImplemented() {
    LocalDateTime begin = LocalDateTime.parse("7/18/2025 14:00", formatter);
    LocalDateTime end = LocalDateTime.parse("7/18/2025 15:00", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getDescription(), is("Doctor appointment"));
  }

  /**
   * Tests that the description passed into the constructor is stored correctly.
   */
  @Test
  void descriptionIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("7/18/2025 14:00", formatter);
    LocalDateTime end = LocalDateTime.parse("7/18/2025 15:00", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getDescription(), is("Doctor appointment"));
  }

  /**
   * Tests that the begin time is stored and returned correctly from getBeginTimeString().
   */
  @Test
  void beginTimeIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("7/18/2025 14:00", formatter);
    LocalDateTime end = LocalDateTime.parse("7/18/2025 15:00", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getBeginTime(), is(begin));
    assertThat(appointment.getEndTime(), is(end));
  }

  /**
   * Tests that the end time is stored and returned correctly from getEndTimeString().
   */
  @Test
  void endTimeIsStoredCorrectly() {
    LocalDateTime begin = LocalDateTime.parse("7/18/2025 14:00", formatter);
    LocalDateTime end = LocalDateTime.parse("7/18/2025 15:00", formatter);
    Appointment appointment = new Appointment("Doctor appointment", begin, end);
    assertThat(appointment.getEndTime(), is(end));
  }
}

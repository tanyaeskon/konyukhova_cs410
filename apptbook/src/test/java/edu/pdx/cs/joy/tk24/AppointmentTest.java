package edu.pdx.cs.joy.tk24;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Appointment} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AppointmentTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void getBeginTimeStringNeedsToBeImplemented() {
    Appointment appointment = new Appointment("Doctor appointment", "7/18/2025 14:00", "7/18/2025 15:00");
    //assertThrows(UnsupportedOperationException.class, appointment::getBeginTimeString);
    assertThat(appointment.getDescription(), is("Doctor appointment"));

  }

  /**
   * Tests that the description passed into the constructor is stored correctly.
   */
  @Test
  void descriptionIsStoredCorrectly() {
    Appointment appointment = new Appointment("Doctor appointment", "7/18/2025 14:00", "7/18/2025 15:00");
    assertThat(appointment.getDescription(), is("Doctor appointment"));
  }

  /**
   * Tests that the begin time is stored and returned correctly from getBeginTimeString().
   */
  @Test
  void beginTimeIsStoredCorrectly() {
    Appointment appointment = new Appointment("Doctor appointment", "7/18/2025 14:00", "7/18/2025 15:00");
    assertThat(appointment.getBeginTimeString(), is("7/18/2025 14:00"));
  }

  /**
   * Tests that the end time is stored and returned correctly from getEndTimeString().
   */
  @Test
  void endTimeIsStoredCorrectly() {
    Appointment appointment = new Appointment("Doctor appointment", "7/18/2025 14:00", "7/18/2025 15:00");
    assertThat(appointment.getEndTimeString(), is("7/18/2025 15:00"));
  }

}

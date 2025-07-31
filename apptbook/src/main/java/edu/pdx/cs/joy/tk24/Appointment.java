package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AbstractAppointment;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.lang.Comparable;


/**
 * Represents a single appointment with a description, start time, and end time.
 *
 * This class extends {@link AbstractAppointment} and implements {@link Comparable} to allow
 * sorting based on begin time, then end time, then description.
 *
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {

  private final String description;
  private final LocalDateTime beginTime;
  private final LocalDateTime endTime;

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

  /**
   * Constructs a new {@code Appointment} instance.
   *
   * @param description A short description of the appointment
   * @param beginTime   The start time of the appointment as a {@link LocalDateTime}
   * @param endTime     The end time of the appointment as a {@link LocalDateTime}
   * @throws IllegalArgumentException if the end time is before the begin time
   */
  public Appointment(String description, LocalDateTime beginTime, LocalDateTime endTime) {
    if(endTime.isBefore(beginTime)){
      throw new IllegalArgumentException("End time cannot be before begin time");
    }

    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * Returns the begin time of this appointment as a formatted string.
   *
   * @return The formatted begin time string (e.g., "07/30/2025 2:00 PM")
   */
  @Override
  public String getBeginTimeString() {
    return this.beginTime.format(FORMATTER);
  }

  /**
   * Returns the end time of this appointment as a formatted string.
   *
   * @return The formatted end time string (e.g., "07/30/2025 3:00 PM")
   */
  @Override
  public String getEndTimeString() {
    return this.endTime.format(FORMATTER);

  }

  /**
   * Returns the start time of this appointment as a {@link LocalDateTime} object.
   *
   * @return The start time of the appointment
   */
  @Override
  public LocalDateTime getBeginTime() {
    return beginTime;
  }

  /**
   * Returns the end time of this appointment as a {@link LocalDateTime} object.
   *
   * @return The end time of the appointment
   */
  @Override
  public LocalDateTime getEndTime() {
    return endTime;
  }

  /**
   * Returns the description of the appointment.
   *
   * @return A short description of the appointment
   */
   @Override
   public String getDescription() {
     return this.description;
  }

  /**
   * Compares this appointment to another appointment for sorting.
   *
   * The comparison is based first on begin time, then end time, then description.
   *
   *
   * @param other The other appointment to compare to
   * @return A negative integer, zero, or a positive integer if this appointment is less than,
   * equal to, or greater than the specified appointment
   */
  @Override
  public int compareTo(Appointment other) {
    int beginCompare = this.getBeginTime().compareTo(other.getBeginTime());
    if (beginCompare != 0) return beginCompare;

    int endCompare = this.getEndTime().compareTo(other.getEndTime());
    if (endCompare != 0) return endCompare;

    return this.getDescription().compareTo(other.getDescription());
  }
}

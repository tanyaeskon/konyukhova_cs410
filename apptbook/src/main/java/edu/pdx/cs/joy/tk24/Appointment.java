package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AbstractAppointment;

/**
 * Represents a single appointment with a description, start time, and end time.
 * <p>
 * This class extends {@link AbstractAppointment} and provides implementations
 * for accessing appointment details as strings.
 * </p>
 */
public class Appointment extends AbstractAppointment {

  private final String description;
  private final String beginTime;
  private final String endTime;


  /**
   * Creates a new {@code Appointment} with a description, start time, and end time.
   *
   * @param description A short description of the appointment
   * @param beginTime   The beginning time of the appointment (in string format)
   * @param endTime     The ending time of the appointment (in string format)
   */
  public Appointment(String description, String beginTime, String endTime) {
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * Returns the start time of the appointment as a string.
   *
   * @return The appointment's start time
   */
  @Override
  public String getBeginTimeString() {
    return this.beginTime;
  }

  /**
   * Returns the end time of the appointment as a string.
   *
   * @return The appointment's end time
   */
  @Override
  public String getEndTimeString() {
    return this.endTime;
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


}

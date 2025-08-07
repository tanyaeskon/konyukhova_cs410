package edu.pdx.cs.joy.tk24;

import edu.pdx.cs.joy.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents an appointment book that belongs to a specific owner
 * and stores multiple {@link Appointment} entries.
 *
 * This class extends {@link AbstractAppointmentBook} and provides
 * functionality to store, retrieve, and sort appointments.
 *
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
  private final String owner;
  private final List<Appointment> appointments;

  /**
   * Creates a new {@code AppointmentBook} for the given owner.
   *
   * @param owner The name of the appointment book owner.
   */
  public AppointmentBook(String owner) {
    this.owner = owner;
    this.appointments = new ArrayList<>();
  }

  /**
   * Returns the name of the owner of this appointment book.
   *
   * @return The owner's name.
   */
  @Override
  public String getOwnerName() {
    return this.owner;
  }

  /**
   * Returns all appointments currently in the appointment book.
   *
   * @return A collection of all {@link Appointment} objects in the book.
   */
  @Override
  public Collection<Appointment> getAppointments() {
    List<Appointment> sortedAppointments = new ArrayList<>(this.appointments);
    Collections.sort(sortedAppointments);
    return sortedAppointments;
  }

  /**
   * Adds a new appointment to this appointment book.
   *
   * @param appt The {@link Appointment} to add.
   */
  @Override
  public void addAppointment(Appointment appt) {
    this.appointments.add(appt);
    Collections.sort(this.appointments);
  }
}

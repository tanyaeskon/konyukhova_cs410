package edu.pdx.cs.joy.tk24;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    /**
     * Returns an error message for a missing required parameter.
     *
     * @param parameterName the name of the missing parameter
     * @return formatted error message indicating which parameter is missing
     */
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    /**
     * Returns a success message for when an appointment is added.
     *
     * @return success message confirming appointment was added
     */
    public static String appointmentAddedSuccessfully()
    {
        return "Appointment added successfully";
    }

    /**
     * Returns a message indicating no appointments were found for an owner.
     *
     * @param owner the name of the appointment book owner
     * @return message indicating no appointments exist for the specified owner
     */
    public static String noAppointmentsFound(String owner){
        return String.format("There are no appointments found for %s",owner);
    }

}

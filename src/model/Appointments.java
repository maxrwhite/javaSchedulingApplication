package model;
import DBconnect.DataBaseLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

public class Appointments {


    int appointmentID;
    String title;
    String description;
    String location;
    String type;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int customerID;
    int userID;
    int contactID;
    String contactName;



    /**
     * created appt
     */
    public static ObservableList<Appointments> appointments = DBAppointments.getAllAppointments();

    /**
     *appt object
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startTime
     * @param endTime
     * @param customerID
     * @param userID
     * @param contactID
     * @param contactName
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, int customerID, int userID, int contactID, String contactName) {


        this.appointmentID = appointmentID;
        this.title = title;
        this.location = location;
        this.description = description;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }


    /**
     * get appt id
     * @return
     */
    public int getAppointmentID() {

        return appointmentID;
    }



    /**
     * Get contact id
     * @return contact id
     */
    public int getContactID() {

        return contactID;
    }



    /**
     * Get customer id
     * @return customer ID
     */
    public int getCustomerID() {

        return customerID;
    }


    /**
     * Get user ID
     * @return user id
     */
    public int getUserID() {

        return userID;
    }


    /**
     * Get contact name
     * @return contact name
     */
    public String getContactName() {

        return contactName;
    }


    /**
     * Get description
     * @return description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Get location
     * @return location
     */
    public String getLocation() {

        return location;
    }

    /**
     * Get Title
     * @return title
     */
    public String getTitle() {

        return title;
    }

    /**
     * Get type
     * @return type
     */
    public String getType() {
        return type;
    }




    /**
     * Get start time
     *
     * @return start time
     */
    public Timestamp getStartTime() {

        return Timestamp.valueOf(startTime);
    }

    /**
     * Get end time
     *
     * @return end time
     */
    public Timestamp getEndTime() {

        return Timestamp.valueOf(endTime);
    }


    /**
     * Appointment array list.
     * @return list of all appointments
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return appointments;
    }


    /**
     * Adds new appointment to appointment array
     * @param newAppointment new appointment created.
     */
    public static void addAppointment(Appointments newAppointment){

        appointments.add(newAppointment);
    }




    /**
     * Removes the appointment from array.
     * @param selectedAppointment
     */
    public static void deleteAppt(Appointments selectedAppointment){

        appointments.remove(selectedAppointment);
    }



    /**
     * Updates existing appointment in array
     * @param Index
     * @param appointment
     */
    public static void updateAppointment(int Index, Appointments appointment){

        appointments.set(Index, appointment);
    }







    /**
     * array to populate the radio button month view of appts
     * returns appts in month
     * @return array of appts next month
     */
    public static ObservableList<Appointments> getMonthAppointments() {
        ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();

        LocalDateTime searchApptStartTime = LocalDateTime.now(DataBaseLogin.getUserZoneID());
        LocalDateTime searchApptEndTime = searchApptStartTime.plusMonths(1);

        for (Appointments searchAppt : appointments) {
            LocalDateTime searchTime = searchAppt.getStartTime().toLocalDateTime();


            //checks if the time is within the month
            if (searchTime.isAfter(searchApptStartTime) && searchTime.isBefore(searchApptEndTime)) {
                monthlyAppointments.add(searchAppt);


            }
        }


        return monthlyAppointments;
    }



    /**
     * A3.e checks for upcoming appt in 15 min
     *
     * @return appointments in the next 15 minutes.
     */
    public static ObservableList<Appointments> incomingAppt(){


        LocalDateTime beginTime = LocalDateTime.now(DataBaseLogin.getUserZoneID());
        LocalDateTime endTime = beginTime.plusMinutes(15);



        ObservableList<Appointments> approachingAppt = FXCollections.observableArrayList();
        for (Appointments searchAppt : appointments)

        {
            ZonedDateTime zoneSearch = ZonedDateTime.from(searchAppt.getStartTime().toLocalDateTime().atZone(DataBaseLogin.getUserZoneID()));
            zoneSearch = zoneSearch.withZoneSameInstant(DataBaseLogin.getUserZoneID());

            LocalDateTime searchTime = zoneSearch.toLocalDateTime();

            //if the time search time is within in 15min
            if (searchTime.isAfter(beginTime) && searchTime.isBefore(endTime)) {
                approachingAppt.add(searchAppt);
                return approachingAppt;
            }
        }
        return null;
    }





    /**
     * A3.e ALERT within the next 15 minutes
     * A3.e also alerts no appt
     *
     */
    public static void nextAppt(){


        if( Appointments.incomingAppt() != null){

            //if appt does occur in the 15 minutes
            for(int i = 0;
                i < incomingAppt().size(); i++) {

                Appointments nextAppt = incomingAppt().get(i);
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Appointment");
                warning.setHeaderText("An appointment will occur in the next 15 minutes.");
                warning.setContentText("The Appointment ID is " + nextAppt.getAppointmentID() + " Date and Time " + nextAppt.getStartTime());
                warning.show();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Appt");
            alert.setHeaderText("No appointments will occur in the next 15 minutes.");
            alert.show();
        }

    }



    /**
     * 3.d CHECKS FOR SCHEDULING OVERLAP
     *
     * @param custID cust id
     * @param newDate new appt date
     * @param apptNewStart new start time
     * @param newEnd new end time
     * @return TRUE if appointment overlap.
     * @throws SQLException
     */
    public static boolean schedulingOverLap(int custID, LocalDate newDate, LocalTime apptNewStart, LocalTime newEnd, int apptID) throws SQLException {


        ObservableList<Appointments> customerAppts = DBAppointments.getApptByID(custID);

        boolean scheduleOverLap = false;
        LocalDate startDate;
        LocalTime apptStartTime;
        LocalTime apptEndTime;



        for (int i = 0;


             i < customerAppts.size(); i++) {

            Appointments appt = customerAppts.get(i);

            startDate = appt.getStartTime().toLocalDateTime().toLocalDate();


            if( apptID == appt.getAppointmentID()){

                continue;

            } else if (startDate.equals(newDate)) {

                apptStartTime = appt.getStartTime().toLocalDateTime().toLocalTime();
                apptEndTime = appt.getEndTime().toLocalDateTime().toLocalTime();


                if ((apptNewStart.isAfter(apptStartTime) || apptNewStart.equals(apptStartTime))
                        && apptNewStart.isBefore(apptEndTime))
                {
                    scheduleOverLap = true;
                }
                else if (newEnd.isAfter(apptStartTime) && (apptNewStart.isBefore(apptEndTime)
                        || newEnd.equals(apptEndTime)))
                {
                    scheduleOverLap = true;
                }


                else if ((apptNewStart.isBefore(apptStartTime) || apptNewStart.equals(apptStartTime))
                        && (newEnd.isAfter(apptEndTime) || newEnd.equals(apptEndTime))) {


                    scheduleOverLap = true;
                }
            }


        }
        return scheduleOverLap;
    }


    /**
     * Checks business hours to verify appts
     * @param timeOfAppt appointment time when adding/ changing appointment
     * @return
     */
    public static boolean businessHourCheck(LocalDateTime timeOfAppt) {


        ZonedDateTime apptZone = timeOfAppt.atZone(ZoneId.systemDefault());

        apptZone = apptZone.withZoneSameInstant(ZoneId.of("US/Eastern"));

        timeOfAppt = apptZone.toLocalDateTime();


        //defined business hours based on assignment
        LocalTime startOfBusiness = LocalTime.of(8,0);
        LocalTime endOfBusiness = LocalTime.of(22,0);


        return (timeOfAppt.toLocalTime().isAfter(startOfBusiness) || timeOfAppt.toLocalTime().equals(startOfBusiness)) && timeOfAppt.toLocalTime().isBefore(endOfBusiness);

    }

}

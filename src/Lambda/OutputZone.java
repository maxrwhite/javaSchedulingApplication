package Lambda;

import DBconnect.DataBaseLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static model.Appointments.appointments;

public interface OutputZone {



    /**
     * Lambda expression: reviews appts and compares time to only show appts within that week.
     *
     * Returns appts for WEEK
     * @return  array of appointments in the next week
     */
    public static ObservableList<Appointments> getWeekAppointments() {


        LocalDateTime beginTime = LocalDateTime.now(DataBaseLogin.getUserZoneID());
        LocalDateTime endTime = beginTime.plusWeeks(1);
        ObservableList<Appointments> weeklyAppointments = FXCollections.observableArrayList();



        appointments.forEach(appointment -> {

            LocalDateTime searchTime = appointment.getStartTime().toLocalDateTime();

            if (searchTime.isAfter(beginTime) && searchTime.isBefore(endTime)) {
                weeklyAppointments.add(appointment);
            }
        });

        return weeklyAppointments;
    }


    /**
     * lambda expression #2 method will take the zoneID and convert it to a string for the login
     * @param currentZone
     *
     * @return the current time zone into the label that is on the login screen
     */
    String getMessage(ZoneId currentZone);

}

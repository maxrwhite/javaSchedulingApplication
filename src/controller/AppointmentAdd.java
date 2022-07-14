package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**

 */
public class AppointmentAdd implements Initializable {


    public ComboBox contactComboBox;
    public ComboBox addCustID;
    public ComboBox addUserID;
    public DatePicker dateSelect;
    public TextField addStart;
    public TextField addEnd;
    public TextField addApptID;


    public TextField addTitle;
    public TextField addType;
    public TextField addDesc;
    public TextField addLocation;
    public Button addButton;
    public Button cancelButton;



    //APPT ADD BUTTON

    /**
     * APPT ADD BUTTON
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void addButtonAction(ActionEvent actionEvent) throws SQLException, IOException {

        boolean addAppointment = false;


        try {


            LocalTime startTime;
            LocalTime endTime;

            String title = addTitle.getText();
            String desc = addDesc.getText();
            String location = addLocation.getText();
            String type = addType.getText();


            String contact = String.valueOf(contactComboBox.getValue());
            int custID = (int) addCustID.getValue();
            int userID = (int) addUserID.getValue();
            int apptID = Integer.parseInt(addApptID.getText());
            int contactID = DBContacts.getContactID(contact);
            LocalDate dateSelected = dateSelect.getValue();
            String userName = DBUsers.getUser(userID);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


            LocalDateTime now = LocalDateTime.now();
            startTime = LocalTime.parse(addStart.getText(), formatter);
            endTime = LocalTime.parse(addEnd.getText(), formatter);
            LocalDateTime startDateTime = LocalDateTime.of(dateSelected, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(dateSelected, endTime);


            ZoneId localZoneID = ZoneId.systemDefault();
            ZonedDateTime localZDTStart = ZonedDateTime.of(startDateTime, localZoneID);
            ZonedDateTime localZDTEnd = ZonedDateTime.of(endDateTime, localZoneID);

            ZoneId easternZID = ZoneId.of("America/New_York");
            ZonedDateTime endEasternZDT = ZonedDateTime.ofInstant(localZDTEnd.toInstant(), easternZID);
            ZonedDateTime startEasternZDT = ZonedDateTime.ofInstant(localZDTStart.toInstant(), easternZID);




        if (title.isBlank() || type.isBlank() ||
        desc.isBlank() || location.isBlank() ){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("Fields must not be left blank");
            error.showAndWait();
        } else
            if (Appointments.businessHourCheck(startDateTime) && Appointments.businessHourCheck(endDateTime)) {


                if (startDateTime.toLocalTime().isBefore(endDateTime.toLocalTime())) {

                    if (Appointments.schedulingOverLap(custID, dateSelected, startTime, endTime, apptID)) {

                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setContentText("appt schedule overlap"
                        );
                        error.showAndWait();
                    } else {


                        //SUCCESSFULLY ADDS TO DB AND LOADS APPT FORM
                        Appointments newAppt = new Appointments(apptID, title, desc, location, type, startDateTime, endDateTime, custID, userID, contactID, contact);
                        Appointments.addAppointment(newAppt);

                        DBAppointments.addAppointment(title, desc, location, type, startDateTime, endDateTime, now, userName, now, userName, custID, userID, contactID);
                        addAppointment = true;
                        if (addAppointment) {


                            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            stage.setTitle("Appointments");
                            stage.setScene(new Scene(root));
                            stage.show();


                        }
                    }
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("appt is not correct time");
                    error.showAndWait();
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("business hour check");
                error.showAndWait();
            }

        } catch (NullPointerException e) {

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("Fields must not be left blank");
            error.showAndWait();


        }


    }








    /**
     * CANCEL BUTTON - RETURN BACK TO APPT MAIN FORM
     * @throws
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * A.3
     *     loads new appt with an ID that is the max ID based on what is in the Database
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {


            addApptID.setText(String.valueOf(DBAppointments.getMaxID() + 1));


            contactComboBox.setItems(DBContacts.getContactName());


            addCustID.setItems(DBCustomers.getCustID());


            addUserID.setItems(DBUsers.getUserID());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


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

public class AppointmentUpdate implements Initializable {


    public DatePicker dateSelect;
    public TextField updateStart;
    public TextField updateEnd;
    public TextField addApptID;


    public TextField updateType;
    public TextField updateTitle;
    public TextField updateDesc;
    public TextField updateLocation;
    public Button updateButton;
    public Button cancelButton;
    public ComboBox contactComboBox;
    public ComboBox<Integer> updateUserID;
    public ComboBox<Integer> updateCustID;

    int index;


    @Override
    /**
     * populates all the combo boxes
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactComboBox.setItems(DBContacts.getContactName());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            updateCustID.setItems(DBCustomers.getCustID());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            updateUserID.setItems(DBUsers.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * gets the appt info from the selected appt from main form
     *
     * @param send
     */
    public void apptInfo(Appointments send) {

        dateSelect.setValue(send.getStartTime().toLocalDateTime().toLocalDate());
        updateUserID.setValue(Integer.valueOf(send.getUserID()));
        addApptID.setText(String.valueOf(send.getAppointmentID()));
        updateTitle.setText(send.getTitle());
        updateDesc.setText(send.getDescription());
        updateLocation.setText(send.getLocation());
        updateType.setText(send.getType());
        updateCustID.setValue(Integer.valueOf(send.getCustomerID()));

        contactComboBox.setValue(send.getContactName());
//        contactComboBox.setItems(DBContacts.getContactName());

        updateEnd.setText(String.valueOf(send.getEndTime().toLocalDateTime().toLocalTime()));
        updateStart.setText(String.valueOf(send.getStartTime().toLocalDateTime().toLocalTime()));

    }

    /**
     * retrieves index used for table view
     *
     * @param selectedIndex
     */
    public void setIndex(int selectedIndex) {

        index = selectedIndex;
    }


    /**
     * updates the current values
     *
     * @param actionEvent
     */
    public void updateButtonAction(javafx.event.ActionEvent actionEvent) {

        boolean updateAppointment = false;


        try {
            LocalTime startTime;
            LocalTime endTime;

            String title = updateTitle.getText();
            String desc = updateDesc.getText();
            String location = updateLocation.getText();
            String type = updateType.getText();


            String contact = String.valueOf(contactComboBox.getValue());
            int custID = updateCustID.getValue();
            int userID = updateCustID.getValue();
            int apptID = Integer.parseInt(addApptID.getText());
            int contactID = DBContacts.getContactID(contact);
            LocalDate dateSelected = dateSelect.getValue();
            String userName = DBUsers.getUser(userID);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


            LocalDateTime now = LocalDateTime.now();
            startTime = LocalTime.parse(updateStart.getText(), formatter);
            endTime = LocalTime.parse(updateEnd.getText(), formatter);
            LocalDateTime startDateTime = LocalDateTime.of(dateSelected, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(dateSelected, endTime);


            ZoneId localZoneID = ZoneId.systemDefault();
            ZonedDateTime localZDTStart = ZonedDateTime.of(startDateTime, localZoneID);
            ZonedDateTime localZDTEnd = ZonedDateTime.of(endDateTime, localZoneID);

            ZoneId easternZID = ZoneId.of("America/New_York");
            ZonedDateTime endEasternZDT = ZonedDateTime.ofInstant(localZDTEnd.toInstant(), easternZID);
            ZonedDateTime startEasternZDT = ZonedDateTime.ofInstant(localZDTStart.toInstant(), easternZID);

            if (Appointments.businessHourCheck(startDateTime) && Appointments.businessHourCheck(endDateTime)) {


                if (startDateTime.toLocalTime().isBefore(endDateTime.toLocalTime())) {

                    if (Appointments.schedulingOverLap(custID, dateSelected, startTime, endTime, apptID)) {

                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setContentText("appt schedule overlap"
                        );
                        error.showAndWait();
                    } else {


                        Appointments updateAppt = new Appointments(apptID, title, desc, location, type, startDateTime, endDateTime, custID, userID, contactID, contact);
                        Appointments.updateAppointment(index, updateAppt);

                        DBAppointments.updateAppointment(apptID, title, desc, location, type, startDateTime, endDateTime, now, userName, custID, userID, contactID);
                        updateAppointment = true;
                        if (updateAppointment) {


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
                    error.setContentText("business hour change");
                    error.showAndWait();
                }
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText("business hour check");
                error.showAndWait();
            }

        } catch (NullPointerException | SQLException | IOException e) {

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("Fields must not be left blank");
            error.showAndWait();


        }
    }






    /**
     * cancels the page and returns to the main form
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root));
        stage.show();

    }


}

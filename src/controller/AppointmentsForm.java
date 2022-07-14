package controller;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.DBAppointments;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Appointments.getMonthAppointments;

import static Lambda.OutputZone.getWeekAppointments;
import static model.Appointments.updateAppointment;

public class AppointmentsForm implements Initializable {



    //FXML
    public TableView<Appointments> apptTableView;
    public TableColumn<Appointments, Integer> apptIDCol;
    public TableColumn<Appointments, Integer> custIDCol;
    public TableColumn<Appointments, Integer> userIDCol;

    public TableColumn<Appointments, String> descCol;
    public TableColumn<Appointments, String> typeCol;
    public TableColumn<Appointments, String> locationCol;
    public TableColumn<Appointments, String> apptTitleCol;
    public TableColumn<Appointments, String> contactCol;

    public TableColumn<Appointments, ZonedDateTime> startCol;
    public TableColumn<Appointments, ZonedDateTime> endCol;

    public ToggleGroup apptView;











    //RETURN TO OPTIONS MENU

    /**
     * return to option menu
     * @param actionEvent
     * @throws IOException
     */
    public void optionsMenuAction(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/SceneSelectorForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.show();
    }





    //DELETE BUTTON

    /**
     *delete button
     * @throws SQLException
     */
    public void deleteButtonAction() throws SQLException {

        //Appt selected will be removed and an alert will verify that user wants to do that.
        Appointments apptDelete = apptTableView.getSelectionModel().getSelectedItem();


        if (apptDelete != null) {

            //confirms with user before delete

            //if user accepts - APPT WILL DELETE IN DB - auto

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Your Appointment will be Deleted");
            alert.setContentText("Are you sure you want to delete appt ID " + apptDelete.getAppointmentID() + " / " + apptDelete.getType() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {

                int rowsAffected = DBAppointments.deleteAppt(apptDelete.getAppointmentID());
                if (rowsAffected > 0) {

                    Appointments.getAllAppointments().remove(apptDelete);
                    DBAppointments.autoIncrement();

                }
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setContentText("Please select an item from the table to delete!");
            alert.showAndWait();
        }


    }



    //UPDATE BUTTON

    /**
     * update button
     * @param actionEvent
     * @throws IOException
     */
    public void updateButtonAction(ActionEvent actionEvent) throws IOException  {

        //TAKES YOU TO UPDATE APPT SCENE
    try {
        FXMLLoader apptLoader = new FXMLLoader();
        apptLoader.setLocation(getClass().getResource("/view/AppointmentUpdateForm.fxml"));
        apptLoader.load();

        AppointmentUpdate ApptUpdate = apptLoader.getController();

        ApptUpdate.apptInfo(apptTableView.getSelectionModel().getSelectedItem());
        ApptUpdate.setIndex(apptTableView.getSelectionModel().getSelectedIndex());


        Parent root = apptLoader.getRoot();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointment Update");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (NullPointerException exception) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText("select an appt please");
        error.show();

    }
    catch (IOException exception) {
    throw new RuntimeException(exception);
    }


    }
    //ADD BUTTON

    /**
     * add button
     * @param actionEvent
     * @throws IOException
     */
    public void addButtonAction(ActionEvent actionEvent) throws IOException {

        //MOVES TO ADD APPOINTMENT SCENE
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentAddForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root));
        stage.show();

    }



    //ALL APPT RADIO BUTTON

    /**
     * shows all appts
     * @param actionEvent
     */
    public void allRadioButton(ActionEvent actionEvent) {

        //Accesses DBAppointments
        apptTableView.setItems(DBAppointments.getAllAppointments());

    }


    //MONTH RADIO BUTTON

    /**
     * shows appts for the month
     * @throws SQLException
     */
    public void monthRadioButton() throws SQLException {

        //access Appointments - month
        apptTableView.setItems(getMonthAppointments());

    }



    //WEEK RADIO BUTTON

    /**
     * shows week appts
     */
    public void weekRadioButton() {

        //access Appointments - week
        apptTableView.setItems(getWeekAppointments());

    }

    /**
     * LOAD APPT DATA ON SCENE STARTUP
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //pulls data into the scene
        apptTableView.setItems(Appointments.getAllAppointments());
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));


    }
}

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DBAppointments;
import model.DBContacts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static model.DBAppointments.getApptByType;

public class Report implements Initializable {

    public String selectApptMonth;
    public String selectApptType;



    public ComboBox apptMonthComboBox;
    public ComboBox apptTypeComboBox;
    public Button searchButton;
    public Label answerText;
    public ComboBox contactComboBox;
    public Button optionsMenuButton;
    public TableView contactScheduleView;
    public TableColumn contactID;
    public TableColumn custID;
    public TableColumn title;
    public TableColumn location;
    public TableColumn desc;
    public TableColumn start;
    public TableColumn end;
    public TableColumn type;
    public ComboBox contactSearchComboBox;
    public Label contactIDLabel;


    /**
     * search button on the tab to TOTAL the amount
     * @param actionEvent
     * @throws SQLException
     */
    public void searchButtonAction(ActionEvent actionEvent) throws SQLException {

        int apptNumber = getApptByType(selectApptType, selectApptMonth).size();
        answerText.setText(String.valueOf(apptNumber));
    }


    /**
     * returns back to options menu
     *
     * @param actionEvent
     */
    public void optionsMenuButtonAction(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/SceneSelectorForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * appt types in combobox
     * @param actionEvent
     */
    public void apptTypeComboBoxAction(ActionEvent actionEvent) {

        //selected value from the combo box.
        selectApptType = String.valueOf(apptTypeComboBox.getValue());

    }

    /**
     * appt month combobox
     * @param actionEvent
     */
    public void apptMonthComboBoxAction(ActionEvent actionEvent) {

        //selected value from combo box
        selectApptMonth = String.valueOf(apptMonthComboBox.getValue());

    }


    /**
     * contacts filled into combobox
     * @param actionEvent
     * @throws SQLException
     */
    public void contactComboBoxAction(ActionEvent actionEvent) throws SQLException {

        String nameOfContact = String.valueOf(contactComboBox.getValue());

        int contactID = DBContacts.getContactID(nameOfContact);

        //IF THAT CONTACT HAS NO APPTS
        if (DBAppointments.apptByContact(contactID).isEmpty()) {


            contactScheduleView.setItems(DBAppointments.apptByContact(contactID));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No APPT");
            alert.setContentText("No appt found for that Contact");
            alert.show();


        }
        else {
            contactScheduleView.setItems(DBAppointments.apptByContact(contactID));
        }

    }

    @Override
    /**
     * fills combo box for all reports
     * tables values
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //fill combo box for all reports
        try {

            apptMonthComboBox.setItems(DBAppointments.getApptMonths());
            contactComboBox.setItems(DBContacts.getContactName());
            apptTypeComboBox.setItems(DBAppointments.getAllApptType());
            contactSearchComboBox.setItems(DBContacts.getContactName());


        } catch (SQLException e) {
            e.printStackTrace();
        }


        //table values for contact schedule view

        custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));


    }



    //3.F 3rd  additional report of my choice
    //combo box with contact ID - gives you the name of contact

    /**
     * 3.F 3rd  additional report of my choice
     * combo box with contact ID - gives you the name of contact
     * @param actionEvent
     * @throws SQLException
     */
    public void contactSearchComboBoxAction(ActionEvent actionEvent) throws SQLException {

        String contactIDFromName = String.valueOf(contactSearchComboBox.getValue());


        int contactID = DBContacts.getContactID(contactIDFromName);
        contactIDLabel.setText(String.valueOf(contactID));


    }
}

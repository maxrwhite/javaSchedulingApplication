package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;
import model.DBCustomers;
import model.DBFLD;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CustomerAdd implements Initializable {

    //FLD FILL BASED ON COUNTRYx

    /**
     * FLD fills based on the country selected
     * @throws SQLException
     */
    public void selectCountry() throws SQLException {

        //disables box to prevent until Country is determined
        custStateComboBox.setDisable(false);
        try {

            custStateComboBox.setItems(DBFLD.getDivision(custCountryComboBox.getValue()));


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    //COMBOBOXES AND FIELDS
    public ComboBox<String> custStateComboBox;
    public ComboBox<String> custCountryComboBox;
    public TextField custNameField;
    public TextField custStreetField;
    public TextField custZipCodeField;
    public TextField custPhoneField;
    public TextField custIDField;


    //CLEAR DATA FIELD BUTTON

    /**
     * clears all the data and you can start over
     * @param actionEvent
     */
    public void clearButtonAction(ActionEvent actionEvent) {

        //values to be cleared and reset
        custNameField.setText("");
        custZipCodeField.setText("");
        custPhoneField.setText("");
        custStreetField.setText("");
        custStateComboBox.getItems().clear();
        custCountryComboBox.getItems().clear();
    }

    //CANCEL BUTTON - return back to Customer Form


    /**
     * cancels adding a customer and returns to the main customer form
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {

        //returns back to the Customer Form Scene
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.show();

    }

    //ADD BUTTON - add customer to database
    //A.2

    /**
     * add customer to database
     * A.2
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void addButtonAction(ActionEvent actionEvent) throws IOException, SQLException {

        //local variables
        int id;
        String name;
        String street;
        String zip;
        String phone;
        int fld = 0;
        LocalTime timeNow = LocalTime.now();
        LocalDate dateNow = LocalDate.now();
        LocalDateTime OSTime = LocalDateTime.of(dateNow, timeNow);
        //used to determine if all fields are completed
        boolean addCustomer = false;


        //insert the customer values recieved into the Database
        id = Integer.parseInt(custIDField.getText());
        name = custNameField.getText();
        street = custStreetField.getText();
        zip = custZipCodeField.getText();
        phone = custPhoneField.getText();
        fld = DBFLD.getFLDID(custStateComboBox.getValue());
        String user = User.getUserName();

        //checks the values to be filled

        if (name.isBlank() || street.isBlank() || zip.isBlank() ||
                phone.isBlank() || fld == 0) {

            addCustomer = false;

            //FXML ERROR DIAG
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setHeaderText("Incomplete Fields");
            errorMessage.setContentText("Please fill in ALL text fields");
            errorMessage.show();
        } else {
            addCustomer = true;

            Customers newCustomer = new Customers(id, name, street, zip, phone, custStateComboBox.getValue(),
                    fld, custCountryComboBox.getValue());

            DBCustomers.customerEntry(name, street, zip, phone, OSTime, user, OSTime, user, fld);

            Customers.addCustomers(newCustomer);


            //if addCustomer is successful, screen closes and returns to Customer Form
            if (addCustomer = true) {

                Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(new Scene(root));
                stage.show();

            }

        }


    }


    //FILL COMBOBOXES - COUNTRY/FLD
    //CUSTID APPLIED

    /**
     * fill combo boxes country/fld
     * cust id applied
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            custCountryComboBox.setItems(DBFLD.getAllCountries());

            if (custCountryComboBox.getSelectionModel().getSelectedItem() == null) {
                custStateComboBox.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Customer ID is added (unable to edit)/increases from last SQL entry
        try {
            custIDField.setText(String.valueOf(DBCustomers.getMaxID() + 1));
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }
}


package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class CustomerUpdate implements Initializable {

    //COMBOBOXES AND FIELDS
    public ComboBox<String> custStateComboBox;
    public ComboBox<String> custCountryComboBox;
    public TextField custNameField;
    public TextField custIDField;
    public TextField custStreetField;
    public TextField custZipCodeField;
    public TextField custPhoneField;

    //COUNTRY COMBO BOX DATA FILL

    /**
     * country combo box data filled
     * @param actionEvent
     * @throws SQLException
     */
    public void selectCountry(ActionEvent actionEvent) throws SQLException {

        custStateComboBox.setItems(DBFLD.getDivision(custCountryComboBox.getValue()));

    }



    //CANCEL BUTTON

    /**
     * cancels customer update and returns to main customer form
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

    //CLEAR BUTTON

    /**
     * clears customer info and you can start over
     * @param actionEvent
     */
    public void clearButtonAction(ActionEvent actionEvent) {

        //values to be cleared and reset
        custNameField.setText("");
        custZipCodeField.setText("");
        custPhoneField.setText("");
        custStreetField.setText("");
        custStateComboBox.getItems().clear();

    }

    //UPDATE BUTTON
    //customer info is updated and sent to database and shown through CustomerForm table view

    /**
     * cust info is updated and sent to database
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void updateButtonAction(ActionEvent actionEvent) throws SQLException, IOException {

        //set variables to work with for inserting values
        String phone;
        String zipCode;
        String address;
        String name;
        int id;
        int custFLD;
        String user = User.getUserName();

        //update time/date for log
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        LocalDateTime currentTime = LocalDateTime.of(dateNow, timeNow);

        //gets values from fields and insert to DB
        custFLD = DBFLD.getFLDID(custStateComboBox.getValue());
        id = Integer.parseInt(custIDField.getText());
        name = custNameField.getText();
        address = custStreetField.getText();
        zipCode = custZipCodeField.getText();
        phone = custPhoneField.getText();


        Customers newCustomer = new Customers(id, name, address, zipCode, phone,
                custStateComboBox.getValue(), custFLD, custCountryComboBox.getValue());

        //updates in the database
        DBCustomers.updateCustomer(id, name, address, zipCode, phone, currentTime, user, custFLD);
        Customers.updateCustomer(index, newCustomer);

        //Update complete, returns to CustomerForm
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Form");
        stage.setScene(new Scene(root));
        stage.show();
    }

    //TABLE UPDATE FROM INDEX on main CustomerForm
    int index;
    public void tableIndex(int indexSelection) {
        index = indexSelection;
    }

    //RETRIEVE DATA FROM selected customer on CustomerForm (customerTableView)
    //A.2 updating a customer, data autopopulates in the form

    /**
     * retrieves data from selected customer on the customerTableView
     * A.2
     * @param retrieve
     * @throws SQLException
     */
    public void customerRetrieve(Customers retrieve) throws SQLException {

        //values that are retrieved
        custNameField.setText(retrieve.getName());
        custIDField.setText(String.valueOf(retrieve.getId()));
        custStreetField.setText(retrieve.getAddress());
        custPhoneField.setText(retrieve.getPhoneNumber());
        custZipCodeField.setText(retrieve.getZipCode());

        custCountryComboBox.setValue(retrieve.getCustomerCountry());
        custStateComboBox.setValue(retrieve.getCustomerDivision());
        custCountryComboBox.setItems(DBFLD.getAllCountries());
        custStateComboBox.setItems(DBFLD.getDivision(custCountryComboBox.getValue()));

    }


    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

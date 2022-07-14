package controller;
import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import model.DBAppointments;
import model.DBCustomers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerForm implements Initializable {
    public TableView TableViewCustomers;
    public AnchorPane customersForm;
    public Button addButton;
    public Button updateButton;
    public Button deleteButton;
    public Button optionsMenuButton;


    //RETURN TO OPTIONS MENU

    /**
     * returns to option menu
     * @param actionEvent
     * @throws IOException
     */
    public void optionsMenu(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/SceneSelectorForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Options Menu");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * moves to add customer form
     * @param actionEvent
     * @throws IOException
     */
    public void addCustomerAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAddForm.fxml"));
        Stage stage = (Stage)((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }





    /**
     * delete customer from customer list, unless it has appt
     * @param actionEvent
     */
    public void deleteCustomerAction(ActionEvent actionEvent) throws SQLException {

        //DBCustomers.deleteSelectedCustomer();
        boolean custRemove = false;

        Customers removeCustomer = (Customers) TableViewCustomers.getSelectionModel().getSelectedItem();

        int selectedCust = removeCustomer.getId();


        if (DBAppointments.hasAppointmentCheck(selectedCust)) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting customer that has appointment, would you like to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DBCustomers.deleteCustomer(selectedCust);
                Customers.removeCustomer(removeCustomer);
                custRemove = true;
                ObservableList<Appointments>  deletedAppts = FXCollections.observableArrayList();
                for (Appointments a: Appointments.getAllAppointments()) {

                    if (removeCustomer.getId() == a.getCustomerID()) {
                        deletedAppts.add(a);
                    }
                }
                for (Appointments a: deletedAppts){
                    Appointments.getAllAppointments().remove(a);
                }
//
//                if (custRemove) {
//
//                    String sql = "ALTER TABLE CUSTOMERS AUTO_INCREMENT = 1";
//                    PreparedStatement ps = JBDC.connection.prepareStatement(sql);
//
//                    ps.executeUpdate();
//
//                }


                //EVALUATOR WANTS TO DELETE ASSOCIATED APPTS WITH CUSTOMER
                //Appointments.deleteAppt();
                //
            } else {

                DBCustomers.deleteCustomer(selectedCust);
                Customers.removeCustomer(removeCustomer);
                custRemove = true;

                if (custRemove) {

                    String sql = "ALTER TABLE CUSTOMERS AUTO_INCREMENT = 1";
                    PreparedStatement ps = JBDC.connection.prepareStatement(sql);

                    ps.executeUpdate();

                }
            }

        }
    }


        //MOVES TO UPDATE CUSTOMER FORM - BUTTON ACTION

        /**
         * moves to update customer form
         * @param actionEvent
         */
        public void updateCustomerAction (ActionEvent actionEvent){

            //Try / Catch: make the attempt to update the customer.
            //if no customer is accepted, error will occur.
            try {
                //launches update scene
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CustomerUpdateForm.fxml"));
                loader.load();

                //references CustomerUpdate customerRetrieve
                CustomerUpdate custUpdate = loader.getController();
                custUpdate.customerRetrieve((Customers) TableViewCustomers.getSelectionModel().getSelectedItem());
                custUpdate.tableIndex(TableViewCustomers.getSelectionModel().getSelectedIndex());


                //brings forward the update customer scene
                Parent root = loader.getRoot();
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Customer Update");
                stage.setScene(new Scene(root));
                stage.show();

                //if exception happens, this block of code will handle and display error
            } catch (NullPointerException | IOException e) {

                //FXML ERROR DIAG
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setHeaderText("No Selection to Update");
                errorMessage.setContentText("Please select customer to update");
                errorMessage.show();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }


        //
        //TABLE VIEW MAIN CUSTOMER FORM
        public TableColumn<Customers, String> custPhoneCol;
        public TableColumn<Customers, String> custZipCodeCol;
        public TableColumn<Customers, String> custCountryCol;
        public TableColumn<Customers, String> custCityStateCol;
        public TableColumn<Customers, String> custStreetCol;
        public TableColumn<Customers, String> custNameCol;
        public TableColumn<Customers, Integer> custIDCol;


        /**
         * loads customer data in to the form
         * @param url
         * @param resourceBundle
         */
        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){

            TableViewCustomers.setItems(Customers.getAllCustomers());
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            custZipCodeCol.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
            custCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
            custCityStateCol.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
            custStreetCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            custIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        }
    }

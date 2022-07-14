package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionMenu implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //Brings forward the report scene when the button is clicked

    /**
     *Brings forward the report scene when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void reportForm(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportsForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Report Form");
        stage.setScene(new Scene(root));
        stage.show();
    }


    //Brings forward the appt scene when the button is clicked

    /**
     *Brings forward the appt scene when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void apptForm(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appt Form");
        stage.setScene(new Scene(root));
        stage.show();
    }


    //Brings forward the customers scene when the button is clicked

    /**
     *Brings forward the customers scene when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void customerForm(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Form");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

package controller;

import DBconnect.*;
import Lambda.OutputZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static DBconnect.DataBaseLogin.successfulLogIn;

public class UserLogIn implements Initializable {

    //buttons/textfields/labels
    public Button submitButton;
    public Label loginLabel;
    public Label userLabel;
    public Label passwordLabel;
    public TextField userNameField;
    public TextField passwordField;
    public Label userLocationLabel;




    private boolean successfulLogIn = false;
    private boolean userLanguageFrench = false;
    String logInErrorMessage = "Your user credentials are invalid";
    String logInErrorTitle = "Failed Login";


    //A.1
    //setting the user location
    private static Locale localeOfUser = Locale.getDefault();




    //A.1
    //setting the users zone ID - retrieving from system
    private static ZoneId userZoneID = ZoneId.systemDefault();

    //A.1
    //French language

    /**
     * A.1 French language
     */
    public void languageTextChange() {

        //french language
        submitButton.setText("nous faire parvenir");
        loginLabel.setText("connexion");
        userLabel.setText("ID de l'utilisateur");
        passwordLabel.setText("le mot de passe");

    }

    //creates variable to use for errors and logins

    /**
     * used for testing
     */
    public void languageCheck() {
        if (localeOfUser.getLanguage().equals("fr")) {
            userLanguageFrench = true;
        } else {
            userLanguageFrench = false;
        }
    }



    //Translates the text based on the user locale

    /**
     * translates the text based on the user locale
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //checks to see if the user locale is french
        if (localeOfUser.getLanguage().equals("fr")) {
            languageTextChange();
        }


        //A.1 updates the user location label on the login form
        //Lambda Expression 2
        System.out.println("Program Initiated");

        OutputZone notification = currentZone -> {

            String result = currentZone.toString();
            return result;
        };


        userLocationLabel.setText(notification.getMessage(ZoneId.systemDefault()));



    }


    //A.1 @param actionEvent submit log-in
    //get username
    //get user password
    //authorize user info

    /**
     *
     *A.1
     *get username
     *get user password
     *authorize user info
     * checks for 15min appt
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void validateLogIn(ActionEvent event) throws IOException, SQLException {

        //get the username and password
        String userName = userNameField.getText();
        String password = passwordField.getText();

        //updates the variable based on the method checking the username/pass in database
        successfulLogIn = successfulLogIn(userName, password);

        //C. Track User Activity
        String fileName = "login_activity.txt";

        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.systemDefault());
        zonedNow = zonedNow.withZoneSameInstant(ZoneOffset.UTC);

        LocalDateTime now = zonedNow.toLocalDateTime();


        FileWriter writer = new FileWriter(fileName, true);
        PrintWriter logFile = new PrintWriter(writer);

//
//        FileWriter writer = new FileWriter(fileName, true);
//        PrintWriter logFile = new PrintWriter(writer);
//        PrintWriter successfulOutputFile = new PrintWriter(writer);


        //if statement determines if login will be a sucess or if error will appear
        if (successfulLogIn == true) {




            //advances to the options menu to view customers,appts, or records

            Parent root = FXMLLoader.load(getClass().getResource("/view/SceneSelectorForm.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Options Menu");
            stage.setScene(new Scene(root));
            stage.show();

            //checks for appt within 15min
            Appointments.nextAppt();

            //C. prints statement to the login_activity.txt
            logFile.println(userName + " Login Successful  at " + now + "UTC");
            logFile.close();
        }

        if (successfulLogIn == true) {
            logFile.println(userName + " Login Successful  at " + now + "UTC");
        }
        //if login is unsuccessful - error is printed in either english or french
        else
        {
            //checks the location being used if french / else english
            if (localeOfUser.getLanguage().equals("fr")) {

                //fxml diag popup
                Alert alert = new Alert(Alert.AlertType.ERROR, "Le pseudo ou mot de passe est incorect");
                alert.showAndWait();

                //C. prints statement to login_activity.txt
                logFile.println(userName + " Login Unsuccessful  at " + now + "UTC");
            }
            else
            {


                //fxml diag popup
                Alert alert = new Alert(Alert.AlertType.ERROR, "The username or password is incorrect");
                alert.showAndWait();


                //C. prints statement to login_activity.txt
                logFile.println(userName + " Login unsuccessful  at " + now + "UTC");
            }
            logFile.close();
        }
    }
}


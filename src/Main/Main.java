package Main;

import DBconnect.JBDC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;

public class Main extends Application {
    /**
     * running the main program
     * commented code is for testing french language
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {

        JBDC.openConnection();
      // Locale france = Locale.FRENCH;
    //Locale.setDefault(france);

        //  ResourceBundle rb = ResourceBundle.getBundle("helper/french_fr", Locale.getDefault());
        // if (Locale.getDefault().getLanguage().equals("fr"))
        //   System.out.println(rb.getString("userNameLabel"));
        //  System.out.println(rb.getString("passwordLabel"));
        launch(args);
        JBDC.closeConnection();
    }
    @Override

    /**
     * start the login form
     */
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/view/LogInForm.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}


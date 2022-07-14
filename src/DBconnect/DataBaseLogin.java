package DBconnect;
import javafx.scene.control.Alert;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;

public class DataBaseLogin {

    private static User loggedInUser;
    private static Locale localeUser;
    private static ZoneId userZoneID;


    //compares the input from the login screen
    //if username and password are correct log in is successful

    /**
     * compares the input from the login screen
     * if username and password are correct log in is successful
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean successfulLogIn(String userName, String password) throws SQLException {

        //SQL query to search the user name and password in database
        String sql = ("SELECT * FROM USERS WHERE User_Name = ? AND Password = ?");
        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);


        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);




        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {


            String userNameLog = resultSet.getString("User_Name");
            int userIdLog = resultSet.getInt("User_ID");
            loggedInUser = new User(userIdLog, userNameLog);
            localeUser = Locale.getDefault();
            userZoneID = ZoneId.systemDefault();

            return true;
        } else {
            new Alert(Alert.AlertType.ERROR, "The username or password entered is incorrect");
            return false;
        }
    }



    /**
     * getter
     * @return
     */
    public static ZoneId getUserZoneID() {
        return userZoneID;
    }


    /**
     * getter for contact id
     * @param contactID
     * @return
     */
    public static String getContactNameID(int contactID) {

        return null;
    }

}

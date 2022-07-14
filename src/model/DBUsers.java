package model;
import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    /**
     * get the user ID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Integer> getUserID() throws SQLException {


        ObservableList<Integer> userID=FXCollections.observableArrayList();
        String SQL = "SELECT DISTINCT User_ID FROM USERS";
        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(SQL);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            userID.add(resultSet.getInt("User_ID"));
        }


        return userID;
    }


    /**
     * get username based from the ID
     * @param userID
     * @return
     * @throws SQLException
     */
    public static String getUser(int userID) throws SQLException {
        String userName = "test";

        String SQL = "SELECT * FROM USERS WHERE USER_ID = ?";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(SQL);
        preparedStatement.setInt(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){

            userName = resultSet.getString("User_Name");
        }

        return userName;
    }

    //retrieving data from the database - gets Users

    /**
     * get all users from the DB
     * @return
     */
    public static ObservableList<User> getAllUsers() {


        ObservableList<User> userList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM USERS";
            PreparedStatement prepState = JBDC.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepState.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                User user = new User(userID, userName);
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;

    }




}
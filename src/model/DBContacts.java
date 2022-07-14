package model;

import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {




    //fixed problem i was having in the appointment add controller

    /**
     * fixed problem i was having in the appointment add controller
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getContactName() throws SQLException {

        ObservableList<String> contactList = FXCollections.observableArrayList();


        String SQLContacts = "SELECT * FROM CONTACTS";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(SQLContacts);

        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {


            String contactName = resultSet.getString("Contact_Name");

            contactList.add(contactName);
        }
        return contactList;
    }


    //get contact ID matching with the contact name

    /**
     * get contact ID matching with the contact name
     * @param contactName
     * @return
     * @throws SQLException
     */
    public static int getContactID(String contactName) throws SQLException {

        int contactID = 0;

        String SQLContactID = "SELECT * FROM CONTACTS WHERE CONTACT_NAME = ?";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(SQLContactID);
        preparedStatement.setString(1, contactName);
        ResultSet resultSet = preparedStatement.executeQuery();


        while (resultSet.next()) {

            contactID = resultSet.getInt("Contact_ID");
        }

        return contactID;
    }


    /**
     * get contact name for the selected contact ID
     * @param ID
     * @return
     * @throws SQLException
     */
    public static String getContactNameID(int ID) throws SQLException {

        String contactName = "";

        String SQLContactName = "SELECT * FROM CONTACTS WHERE CONTACT_ID = ?";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(SQLContactName);

        preparedStatement.setInt(1, ID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            contactName = resultSet.getString("Contact_Name");

        }
        return contactName;
    }
}


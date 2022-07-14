package model;
import DBconnect.DataBaseLogin;
import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBAppointments {


    /**
     * item deleted from DB
     *
     * @param appointmentID selected appt id
     * @throws SQLException
     */
    public static int deleteAppt(int appointmentID) throws SQLException {

        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JBDC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);

        return ps.executeUpdate();
    }


    /**
     * gets all appts from db
     * @param <Appointments>
     * @return
     */
    public static <Appointments> ObservableList<Appointments> getAllAppointments() {







        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT apt.Appointment_ID, apt.Title, apt.Description, apt.Location, apt.Type, apt.Start, apt.End, apt.Customer_ID, u.User_ID, c.Contact_ID, c.Contact_Name" +

                    //gets customer ID and USER ID
                    " FROM APPOINTMENTS as apt JOIN CUSTOMERS as cust on apt.Customer_ID = cust.Customer_ID JOIN USERS as u on apt.User_ID = u.User_ID" +
                    //gets the contact for the appointment
                    " JOIN CONTACTS as c on apt.Contact_ID = c.Contact_ID";


            PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);


            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {


                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");


                Appointments appointment;

                appointment = (Appointments) new model.Appointments(appointmentID, title, description, location, type
                        , startDate, endDate, customerID, userID, contactID, contactName);

                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;
    }


    /**
     * add appt to db
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createdDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createdDate,
                               String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {


        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start , End, Create_Date, Created_By, " +
                " Last_Update, Last_Updated_BY, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ? ,? ,? , ?, ?, ?, ? )";
        PreparedStatement preparedStatement = JBDC.connection.prepareStatement(sql);


        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement.setString(7, String.valueOf(createdDate));
        preparedStatement.setString(8, createdBy);
        preparedStatement.setString(9, String.valueOf(lastUpdate));
        preparedStatement.setString(10, lastUpdatedBy);
        preparedStatement.setInt(11, customerID);
        preparedStatement.setInt(12, userID);
        preparedStatement.setInt(13, contactID);


        preparedStatement.executeUpdate();
    }







    
    /**
     * Updates the selected appointment
     *
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     * @throws SQLException
     */
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start,
                                         LocalDateTime end, LocalDateTime lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) throws SQLException
    {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ? , Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        
        PreparedStatement preparedStatement = JBDC.connection.prepareStatement(sql);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement.setString(7, String.valueOf(lastUpdate));
        preparedStatement.setString(8, lastUpdatedBy);
        preparedStatement.setInt(9, customerId);
        preparedStatement.setInt(10, userId);
        preparedStatement.setInt(11, contactId);
        preparedStatement.setInt(12, appointmentId);

        preparedStatement.executeUpdate();
    }

    
    


    /**
     *auto increment in the DB is reset
     * @throws SQLException
     */
    public static void autoIncrement() throws SQLException {


        String sql = "ALTER TABLE APPOINTMENTS AUTO_INCREMENT = 1";


        PreparedStatement preparedStatement = JBDC.connection.prepareStatement(sql);

        preparedStatement.executeUpdate();
    }


    /**
     *verify cust ID has associated appt
     * @param customerID selcted customer
     * @return = true if cust id has matching appt
     * @throws SQLException
     */
    public static boolean hasAppointmentCheck(int customerID) throws SQLException {

        boolean hasAppointment = false;
        //SQL to check if the cust ID matches * of appts
        String sql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID = ? ";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();

        
        if (resultSet.next()) {
            hasAppointment = true;
        }
        return hasAppointment;
    }





    /**
     * retreives appts by type
     * @param inputType selected type
     * @return number of appointments with that type
     * @throws SQLException
     */
    public static ObservableList<Appointments> getApptByType(String inputType, String inputMonth) throws SQLException {

        ObservableList<Appointments> typeAppointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM APPOINTMENTS WHERE Type = ? AND MONTHNAME(START) = ? ";


        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);
        preparedStatement.setString( 1, inputType);
        preparedStatement.setString(2,inputMonth);

        ResultSet resultSet = preparedStatement.executeQuery();


        while (resultSet.next()) {




            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();


            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = DataBaseLogin.getContactNameID(contactID);


            Appointments appointment = new Appointments(appointmentID, title, description, location, type
                    , startDate, endDate, customerID, userID, contactID, contactName);
            typeAppointmentList.add(appointment);
        }

        return typeAppointmentList;
    }




    /**
     * Used for appointment overlap
     *
     * @param custID new and used
     * @return checks cust id on appts
     */
    public static ObservableList<Appointments> getApptByID(int custID) throws SQLException {
        ObservableList<Appointments> custAppointmentList = FXCollections.observableArrayList();




        String sql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID = ? ";
        PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);
        ps.setInt(1, custID);
        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = DataBaseLogin.getContactNameID(contactID);


            Appointments appointment = new Appointments(appointmentID, title, description, location, type
                    , startDate, endDate, customerID, userID, contactID, contactName);
            custAppointmentList.add(appointment);
        }
        return custAppointmentList;
    }



    /**
     * Gets list of appts assigned to contact
     * @param contact contact select
     * @return list of contact appts
     * @throws SQLException
    */
    public static ObservableList<Appointments> apptByContact(int contact) throws SQLException {
        ObservableList<Appointments> contactAppointmentList = FXCollections.observableArrayList();


        String sql = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ? ";
        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, contact);
        ResultSet resultSet = preparedStatement.executeQuery();



        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startDate = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endDate = resultSet.getTimestamp("End").toLocalDateTime();
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");
            int contactID = resultSet.getInt("Contact_ID");
            String contactName = DataBaseLogin.getContactNameID(contactID);


            Appointments appointment = new Appointments(appointmentID, title, description, location, type
                    , startDate, endDate, customerID, userID, contactID, contactName);
            contactAppointmentList.add(appointment);
        }
        return contactAppointmentList;
    }

    /**
     * Gets all appointment types
     * @return appointment type list
     * @throws SQLException
     */
    public static ObservableList<String> getAllApptType() throws SQLException {


        ObservableList<String> typeAppointmentList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT * FROM APPOINTMENTS";

        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            String type = resultSet.getString("Type");
            typeAppointmentList.add(type);
        }

        return typeAppointmentList;
    }

    /**
     * retrieves the appts with selected month from db
     * @return months that are within certain month
     * @throws SQLException
     */
    public static ObservableList<String> getApptMonths() throws SQLException {
        ObservableList<String> apptMonth = FXCollections.observableArrayList();

        String sql = "SELECT DISTINCT MONTHNAME(START) AS NAME FROM APPOINTMENTS";
        PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);

        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            String month =resultSet.getString("NAME");
            apptMonth.add(month);
        }
        return apptMonth;
    }




    /**
     * this checks for the max appt id in the appointments table
     * @return
     * @throws SQLException
     */
    public static int getMaxID() throws SQLException {
        int nextId = 0;

        String sql = "SELECT max(Appointment_ID) AS Max_Appt_ID FROM appointments";

        PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            nextId = resultSet.getInt("Max_Appt_ID");
        }
        return nextId;
    }
}

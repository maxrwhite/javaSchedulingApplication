package model;

import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBCustomers {


    /**
     * gets customers from DB and puts in array
     * @return
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();


        try {
            String sql = "SELECT cust.Customer_ID, cust.Customer_Name, cust.Address, cust.Postal_Code, cust.Phone, " +
                    " cust.Division_ID, fld.Division, fld.COUNTRY_ID, ctry.Country FROM customers as cust JOIN first_level_divisions " +
                    "as fld on cust.Division_ID = fld.Division_ID JOIN countries as ctry ON fld.COUNTRY_ID = ctry.Country_ID";
            // String sql = "SELECT * FROM CUSTOMERS";
            PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String zipCode = resultSet.getString("Postal_Code");
                String division = resultSet.getString("Division");
                int divisionID = resultSet.getInt("Division_ID");
                String phoneNumber = resultSet.getString("Phone");
                String customerCountry = resultSet.getString("Country");


                Customers customers = new Customers(customerID, customerName, address, zipCode, phoneNumber, division, divisionID, customerCountry);
                customerList.add(customers);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }


    /**
     * add customer to db
     * @param customerName
     * @param address
     * @param zipCode
     * @param phoneNumber
     * @param createdDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     * @throws SQLException
     */
    public static void customerEntry(String customerName, String address, String zipCode, String phoneNumber, LocalDateTime createdDate,
                                     String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID ) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By,Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ? ,? ,?)";
        PreparedStatement ps = JBDC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,zipCode);
        ps.setString(4,phoneNumber);
        ps.setTimestamp(5, Timestamp.valueOf(createdDate));
        ps.setString(6,createdBy);
        ps.setTimestamp(7,Timestamp.valueOf(lastUpdate));
        ps.setString(8,lastUpdatedBy);
        ps.setInt(9,divisionID);

        ps.executeUpdate();
    }


    /**
     * get the max id of customer - for auto increment
     * @return
     * @throws SQLException
     */
    public static int getMaxID() throws SQLException {
        int nextId = 0;
        String sql = "SELECT max(Customer_ID) AS Max_Cust_ID FROM customers";
        PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            nextId = resultSet.getInt("Max_Cust_ID");
        }

        return nextId;
    }


    /**
     * delete customer from db
     * @param customerID
     * @throws SQLException
     */
    public static void deleteCustomer(int customerID) throws SQLException {




        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JBDC.connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerID);
        preparedStatement.executeUpdate();
    }


    /**
     * updates the customer from the inputed values
     * @param customerId
     * @param customerName
     * @param address
     * @param zipCode
     * @param phoneNumber
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID
     * @throws SQLException
     */
    public static void updateCustomer( int customerId, String customerName, String address, String zipCode, String phoneNumber,LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ? ,Phone = ?,Last_Update = ?, Last_Updated_By = ?,Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JBDC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,zipCode);
        ps.setString(4,phoneNumber);
        ps.setTimestamp(5,Timestamp.valueOf(lastUpdate) );
        ps.setString(6,lastUpdatedBy);
        ps.setInt(7,divisionID);
        ps.setInt(8,customerId);
        ps.executeUpdate();
    }


    /**
     * retrieve the customer ID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Integer> getCustID() throws SQLException {


        ObservableList<Integer> customerId=FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Customer_ID FROM CUSTOMERS";
        PreparedStatement ps = JBDC.getConnection().prepareStatement(sql);

        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            customerId.add(resultSet.getInt("Customer_ID"));
        }


        return customerId;
    }

}

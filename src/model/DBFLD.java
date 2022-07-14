package model;

import DBconnect.JBDC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFLD {


    /**
     * matches FLD to country country
     * puts into the combobox
     * @param countryName
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getDivision(String countryName) throws SQLException {
            int countryFLDID;



            if (countryName.equals("U.S")) {

                countryFLDID = 1;
            } else if (countryName.equals("UK")) {
                countryFLDID = 2;
            } else {
                countryFLDID = 3;
            }
            ObservableList<String> countryDivisions = FXCollections.observableArrayList();

            String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";

            PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, countryFLDID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String divisionName = resultSet.getString("Division");
                countryDivisions.add(divisionName);
            }

            return countryDivisions;
        }


    /**
     * searches form match
     * returns id
     * @param divisionName
     * @return
     * @throws SQLException
     */
    public static int getFLDID(String divisionName) throws SQLException {


        int FLDID = 0;




            String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division = ?";

            PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, divisionName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FLDID = resultSet.getInt("Division_ID");
            }
            return FLDID;
        }


    /**
     * gets countries from db
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getAllCountries() throws SQLException {


        ObservableList<String> countryNames = FXCollections.observableArrayList();

        String sql = "SELECT * FROM COUNTRIES ";
        PreparedStatement preparedStatement = JBDC.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            String countryName = resultSet.getString("Country");
            countryNames.add(countryName);
        }
        return countryNames;
    }
    }







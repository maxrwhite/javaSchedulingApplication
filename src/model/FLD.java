package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FLD {


    private int divisionID;
    private static String divisionName;
    private int countryID;


    private static ObservableList<FLD> getAllDivisions = FXCollections.observableArrayList();

    /**
     * fld object
     *
     * @param divisionID
     * @param divisionName
     * @param countryID
     */
    public FLD(int divisionID, String divisionName, int countryID) {


        this.divisionID = divisionID;
        this.countryID = countryID;
        this.divisionName = divisionName;
    }


    /**
     * Get division ID
     *
     * @return division ID
     */
    public int getDivisionID() {

        return divisionID;
    }

    /**
     * Get division country ID
     *
     * @return country ID
     */
    public int getCountryID() {

        return countryID;
    }

    /**
     * return div name
     *
     * @return division name
     */
    public static String getDivisionName() {

        return divisionName;
    }


    /**
     * Set Division ID
     *
     * @param divisionID .
     */
    public void setDivisionID(int divisionID) {

        this.divisionID = divisionID;
    }


    /**
     * Set countryID
     *
     * @param countryID .
     */
    public void setCountryID(int countryID) {

        this.countryID = countryID;
    }

    /**
     * Set Division Name
     *
     * @param divisionName .
     */
    public void setDivisionName(String divisionName) {

        this.divisionName = divisionName;
    }
}

package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customers {


    private int id;
    private String name;
    private String address;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private String customerDivision;
    private int divisionID;
    private String customerCountry;


    private static ObservableList<Customers> customers = DBCustomers.getAllCustomers();


    /**
     * customer object
     * @param id
     * @param name
     * @param address
     * @param zipCode
     * @param phoneNumber
     * @param customerDivision
     * @param divisionID
     * @param customerCountry
     */
    public Customers(int id, String name, String address, String zipCode,
                    String phoneNumber, String customerDivision, int divisionID, String customerCountry){
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.customerDivision = customerDivision;
        this.customerCountry = customerCountry;

    }

    /**
     * gets cust id
     * @return cust id
     */
    public int getId() {

        return id;
    }

    /**
     * Get customer name
     * @return customer name
     */
    public String getName() {

        return name;
    }

    /**
     * Get customer address
     * @return customer address
     */
    public String getAddress() {

        return address;
    }

    /**
     * Get customer country
     * @return customer country
     */
    public String getCountry() {

        return country;
    }

    /**
     * Get FLD
     * @return FLD
     */
    public String getCustomerDivision() {

        return customerDivision;
    }

    /**
     * Get customer phone
     * @return string containing phone number
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }

    /**
     * Get cust zip
     * @return customer zip code
     */
    public String getZipCode() {

        return zipCode;
    }





    /**
     * Set country
     * @param country
     */
    public void setCountry(String country) {

        this.country = country;
    }

    /**
     * Get country
     * @return customer country
     */
    public String getCustomerCountry() {

        return customerCountry;
    }


    /**
     *Set customer address
     * @param address
     */
    public void setAddress(String address) {

        this.address = address;
    }



    /**
     *Set customer division.
     * @param customerDivision
     */
    public void setCustomerDivision(String customerDivision) {

        this.customerDivision = customerDivision;
    }




    /**
     * Set cust ID
     * @param id
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * Set customer Name
     * @param name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Set customer phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    /**
     * Set for cust Zip
     * @param zipCode
     */
    public void setZipCode(String zipCode) {

        this.zipCode = zipCode;
    }

    /**
     * Set division ID
     * @param divisionID
     */
    public void setDivisionID(int divisionID) {

        this.divisionID = divisionID;
    }

    /**
     * Get for division
     * @return division ID
     */
    public int getDivisionID() {

        return divisionID;
    }

    /**
     * Sett for cust country
     * @param customerCountry
     */
    public void setCustomercountry(String customerCountry) {

        this.customerCountry = customerCountry;
    }

    /**
     * Get country
     * @return customer country.
     */
    public String getCustomercountry() {

        return customerCountry;
    }

    /**
     * Add cust - customer array List
     * @param newCustomer
     */
    public static void addCustomers(Customers newCustomer){

        customers.add(newCustomer);
    }

    /**
     * Removes cust - customer array list.
     * @param selectedCustomer
     */
    public static void removeCustomer(Customers selectedCustomer){

        customers.remove(selectedCustomer);
    }

    /**
     * get ALL customers show array
     * @return customer list array
     *
     */
    public static ObservableList<Customers> getAllCustomers(){

        return customers;
    }




    /**
     * UPDATE Table customer
     * @param Index sends from main page
     * @param customer new customer info
     */
    public static void updateCustomer(int Index, Customers customer){
        customers.set(Index, customer);


    }

}

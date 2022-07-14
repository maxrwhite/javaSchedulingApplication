package model;

public class Contacts {

    private static int contactID;
    private static String contactName;
    private static String email;

    /**
     * contact object
     * @param contactID
     * @param contactName
     * @param email
     */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Get contact Id
     * @return ID
     */
    public static int getID() {

        return contactID;
    }



    /**
     * Get contact name
     * @return name
     */
    public static String getContactName() {

        return contactName;
    }

    /**
     * Get contact email
     * @return email
     */
    public String getEmail() {

        return email;
    }

}

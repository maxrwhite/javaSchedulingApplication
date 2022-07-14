package model;

public class User {


    private static String userName;
    private static int userID;
    private static String password;


    //constructor for user

    /**
     * user object
     * @param userID
     * @param userName
     */
    public User(int userID, String userName) {

        this.userID = userID;
        this.userName = userName;
        this.password = password;

    }


    //GETTERS

    /**
     * get userid
     * @return
     */
    public static int getUserID() {
        return userID;
    }

    /**
     * get username
     * @return
     */
    public static String getUserName() {

        return userName;
    }

    /**
     * get password
     * @return
     */
    public static String getPassword() {

        return password;
    }

    //SETTERS

    /**
     * set userid
     * @param userID
     */
    public void setUserID(int userID) {

        this.userID = userID;
    }

    /**
     * set username
     * @param userName
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {

        this.password = password;
    }


}

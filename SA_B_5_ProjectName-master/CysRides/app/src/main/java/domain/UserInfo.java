package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserInfo {

    private String netID;
    private String password;
    private String confirmationCode;
    private String firstName;
    private String lastName;
    private String venmoName;
    private String profileDescription;
    private UserType userType;
    private float userRating;
    private List<Offer> offers;
    private List<Request> requests;
    private DateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private String dateJoined = df.format(Calendar.getInstance().getTime());
    private boolean isBanned;
    private String banReason;

    /**
     * Default Constructor
     */
    public UserInfo() {

    }

    /**
     * returns user info as a string
     * @return user info as a string
     */
    @Override
    public String toString() {
        return "UserInfo{" +
                "netID='" + netID + '\'' +
                ", password='" + password + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", venmoName='" + venmoName + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", userType=" + userType +
                ", userRating=" + userRating +
                ", offers=" + offers +
                ", requests=" + requests +
                ", dateJoined='" + dateJoined + '\'' +
                '}';
    }

    /**
     * Constructor that creates user info
     * @param netID - user's netID
     * @param password - user's password
     * @param confirmationCode - account confirmation code
     * @param firstName - user's first name
     * @param lastName - user's last name
     * @param venmoName - user's venmo account name
     * @param profileDescription - user's profile description
     * @param userType - user's type
     * @param userRating - user's rating
     * @param offers - list of user's offers
     * @param requests - list of user's requests
     */
    public UserInfo(String netID, String password, String confirmationCode, String firstName, String lastName, String venmoName,
                    String profileDescription, UserType userType, float userRating, List<Offer> offers, List<Request> requests) {
        this.netID = netID;
        this.password = password;
        this.confirmationCode = confirmationCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.venmoName = venmoName;
        this.profileDescription = profileDescription;
        this.userType = userType;
        this.userRating = userRating;
        this.offers = offers;
        this.requests = requests;
        isBanned = false;
        banReason = null;
    }

    /**
     * return the date this user joined
     * @return user's join date
     */
    public String getDateJoined() {
        return dateJoined;
    }

    /**
     * sets the date the user joined
     * @param dateJoined - date user joined
     */
    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    /**
     * returns the user's rating as a string
     * @param userRating - user's rating
     * @return - user's rating as a string
     */
    public String ratingToString(float userRating) {
        return Float.toString(userRating);
    }

    /**
     * returns user's netID
     * @return user's netID
     */
    public String getNetID() {
        return netID;
    }

    /**
     * sets user's netID
     * @param netID - user's netID
     */
    public void setNetID(String netID) {
        this.netID = netID;
    }

    /**
     * returns user's password
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets user's password
     * @param password - user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * returns user's confirmation code
     * @return user's confirmation code
     */
    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * sets account confirmation code
     * @param confirmationCode - account's confirmation code
     */
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    /**
     * returns user's first name
     * @return user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets user's first name
     * @param firstName - user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * returns user's last name
     * @return user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets user's last name
     * @param lastName - user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * return's user's venmo name
     * @return user's venmo name
     */
    public String getVenmoName() {
        return venmoName;
    }

    /**
     * sets user's venmo name
     * @param venmoName - user's venmo name
     */
    public void setVenmoName(String venmoName) {
        this.venmoName = venmoName;
    }

    /**
     * returns user's profile description
     * @return user's profile description
     */
    public String getProfileDescription() {
        return profileDescription;
    }

    /**
     * sets user's profile description
     * @param profileDescription - user's profile description
     */
    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    /**
     * return's user's user type
     * @return user's user type
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * set's user's user type
     * @param userType
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * returns user's rating
     * @return user's rating
     */
    public float getUserRating() {
        return userRating;
    }

    /**
     * sets user's rating
     * @param userRating - user's rating
     */
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    /**
     * returns list of user's ride offers
     * @return list of user's ride offers
     */
    public List<Offer> getOffers() {
        return offers;
    }

    /**
     * sets list of user's ride offers
     * @param offers - user's ride offers
     */
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    /**
     * returns list of user's ride requests
     * @return list of user's ride requests
     */
    public List<Request> getRequests() {
        return requests;
    }

    /**
     * sets list of user's ride requests
     * @param requests - list of user's ride requests
     */
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    /**
     * return true if user is banned
     * @return true if user is banned
     */
    public boolean getIsBanned() {
        return isBanned;
    }

    /**
     * set if user is banned
     * @param isBanned - true if user is banned
     */
    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    /**
     * get reason user was banned
     * @return reason user was banned
     */
    public String getBanReason() {
        return banReason;
    }

    /**
     * sets reason user was banned
     * @param banReason - reason user was banned
     */
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

}

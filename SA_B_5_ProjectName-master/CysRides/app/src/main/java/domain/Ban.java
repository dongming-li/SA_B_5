package domain;

public class Ban {
    private String email;
    private String reason;

    /**
     * Default constructor
     */
    public Ban() {

    }

    /**
     * Constructor that sets user's email and ban reason
     * @param email - user's email
     * @param reason - reason they were banned
     */
    public Ban(String email, String reason) {
        this.email = email;
        this.reason = reason;
    }

    /**
     * Returns user's email
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set's user's email
     * @param email - user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * returns reason user was banned
     * @return reason user was banned
     */
    public String getReason() {
        return reason;
    }
}

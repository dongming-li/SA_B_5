package domain;

import java.util.ArrayList;

public class Group {
    private ArrayList<String> groupMembers = new ArrayList<>();
    private String driver;
    private int offerID;
    private int groupID;
    private int requestID;
    private String type;

    /**
     * empty constructor
     */
    public Group() {

    }

    /**
     * Constructor that creates a group with what is needed to put one in the database
     * @param user - String netID of the first group member
     * @param type - type of ride
     */
    public Group(String user, String type){

        this.type = type;
        //if this is an offer, add the user as a driver
        if(type.equals("OFFER")) {
            groupMembers.add(user);
            driver = user;
        }
        //if this is a request, add a null
        if(type.equals("REQUEST")){
            groupMembers.add(null);
            groupMembers.add(user);
        }

    }

    /**
     * Constructor that creates a group with what is needed to pull one from the database
     * @param groupID - int ID of the Group
     * @param groupMembers - ArrayList<String> members of group
     * @param offerID = int ID of the Offer
     * @param requestID - int ID of the request
     */
    public Group(int groupID, ArrayList<String> groupMembers, int offerID, int requestID){
        this.groupMembers = groupMembers;
        this.groupID = groupID;
        this.offerID = offerID;
        this.requestID = requestID;
        this.driver = groupMembers.get(0);

        if(this.offerID == Integer.MIN_VALUE){
            this.type = "REQUEST";
        }else{
            this.type = "OFFER";
        }

    }

    /**
     * Check if a user is in a Group
     * @param netID - netID to be checked
     * @return boolean inGroup
     */
    public boolean inGroup(String netID) {
        return groupMembers.contains(netID);
    }

    /**
     * Get the members in a Group
     * @return ArrayList<String> groupMembers
     */
    public ArrayList<String> getGroupMembers(){
        return groupMembers;
    }

    /**
     * Set the members of a Group
     * @param groupMembers - ArrayList<String> members of group
     */
    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    /**
     * Get the type of a Group
     * @return String type
     */
    public String getType(){return type;}

    /**
     * Get the driver of a Group
     * @return String driver
     */
    public String getDriver(){return driver;}

    /**
     * Get the id of a Group in the database (only to be used with
     * Groups that have been pulled from the database)
     * @return int groupID
     */
    public int getId(){return groupID;}

    /**
     * Get the id of the Offer associated to a Group
     * @return int offerID
     */
    public int getOfferId(){return offerID;}

    /**
     * Sets the ID of the offer in the Group
     * @param offerID - ID of the offer
     */
    public void setOfferID(int offerID){
        this.offerID = offerID;
    }

    /**
     * Sets the ID of the request in the Group
     * @param requestID - ID of the request
     */
    public void setRequestID(int requestID){
        this.requestID = requestID;
    }

    /**
     * Get the id of the Request associated to a Group
     * @return int requestID
     */
    public int getRequestID(){return this.requestID;}

    /**
     * Get the number of members of a Group
     * @return int size
     */
    public int getSize(){return this.getGroupMembers().size();}



}

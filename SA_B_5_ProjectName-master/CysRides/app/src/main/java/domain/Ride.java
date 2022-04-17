package domain;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import service.Callback;
import volley.GroupVolleyImpl;

public abstract class Ride {

    private int id;
    private String email;
    private String destination;
    private String start;
    private LatLng destCoordinates;
    private LatLng startCoordinates;
    private String description;
    private Date date;
    private Group group;
    private int groupID;

    /**
     * Default constructor
     */
    public Ride(){

    }

    /**
     * Constructor setting all data. Doesn't include id or context
     * @param email of user
     * @param destination of ride
     * @param destCoordinates - coordinates of destination
     * @param start - starting point
     * @param startCoordinates - coordinates of start
     * @param description - description of ride
     * @param date - leave date
     * @param type - type of ride
     */
    public Ride(String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, String type) {
        this.email = email;
        this.destination = destination;
        this.destCoordinates = destCoordinates;
        this.start = start;
        this.startCoordinates = startCoordinates;
        this.description = description;
        this.date = date;

        group = new Group(email, type);
    }

    /**
     * Constructor setting all data. Includes ride ID
     * @param id of ride
     * @param email of user
     * @param destination of ride
     * @param destCoordinates - coordinates of destination
     * @param start - starting point
     * @param startCoordinates - coordinates of start
     * @param description - description of ride
     * @param date - leave date
     * @param type - type of ride
     */
    public Ride(int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, String type) {
        this.id = id;
        this.email = email;
        this.destination = destination;
        this.destCoordinates = destCoordinates;
        this.start = start;
        this.startCoordinates = startCoordinates;
        this.description = description;
        this.date = date;

        group = new Group(email, type);
    }

    /**
     * Constructor setting all data. Includes groupID
     * @param email of user
     * @param destination of ride
     * @param destCoordinates - coordinates of destination
     * @param start - starting point
     * @param startCoordinates - coordinates of start
     * @param description - description of ride
     * @param date - leave date
     * @param groupID - assigned group
     */
    public Ride(String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        this.email = email;
        this.destination = destination;
        this.destCoordinates = destCoordinates;
        this.description = description;
        this.start = start;
        this.startCoordinates = startCoordinates;
        this.date = date;
        this.groupID = groupID;
        //USE CALLBACK SOMEHOW TO GET THE GROUP BACK
        pullGroup(context, this.groupID);
    }

    /**
     * Constructor setting all data. Includes groupID and ride ID
     * @param id of ride
     * @param email of user
     * @param destination of ride
     * @param destCoordinates - coordinates of destination
     * @param start - starting point
     * @param startCoordinates - coordinates of start
     * @param description - description of ride
     * @param date - leave date
     * @param groupID - assigned group
     */
    public Ride(int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        this.id = id;
        this.email = email;
        this.destination = destination;
        this.destCoordinates = destCoordinates;
        this.description = description;
        this.start = start;
        this.startCoordinates = startCoordinates;
        this.date = date;
        this.groupID = groupID;
        //USE CALLBACK SOMEHOW TO GET THE GROUP BACK
        pullGroup(context, this.groupID);
    }

    /**
     * Get group from the database
     * @param context of app
     * @param groupID - id of group
     */
    private void pullGroup(Context context, int groupID){
        GroupVolleyImpl gvi = new GroupVolleyImpl(context, new Callback() {
            @Override
            public void call(ArrayList<?> result) {
                try{
                    if(result.get(0) instanceof  Group){
                        group = (Group) result.get(0);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        gvi.getGroup(context, groupID);
    }

    /**
     * Accesses ride ID
     * @return ride id
     */
    public int getId() {
        return id;
    }

    /**
     * sets ride id
     * @param id of ride
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns user email
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets user email
     * @param email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * returns destination
     * @return destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * sets destination
     * @param destination of ride
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * gets starting position
     * @return starting position
     */
    public String getStart() {
        return start;
    }

    /**
     * sets starting position
     * @param start position
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * returns destination coordinates
     * @return destination coordinates
     */
    public LatLng getDestCoordinates() {
        return destCoordinates;
    }

    /**
     * returns starting coordinates
     * @return starting coordinates
     */
    public LatLng getStartCoordinates() {
        return startCoordinates;
    }

    /**
     * returs ride description
     * @return ride description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets ride description
     * @param description of ride
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns leave date
     * @return leave date
     */
    public Date getDate() {
        return date;
    }

    /**
     * sets ride date
     * @param date of ride
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * returns group
     * @return group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * sets group
     * @param group of ride
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}

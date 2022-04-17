package domain;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Request extends Ride {

    private int numBags;

    /**
     * default constructor
     */
    public Request() {

    }

    /**
     * Constructor for new requests
     * @param numBags - number of bags
     * @param id - ride ID
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     */
    public Request(int numBags, int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date) {
        super(id, email, destination, destCoordinates, start, startCoordinates, description, date, "REQUEST");
        this.numBags = numBags;
    }

    /**
     * Constructor for new requests
     * @param numBags - number of bags
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     */
    public Request(int numBags, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date) {
        super(email, destination, destCoordinates, start, startCoordinates, description, date, "REQUEST");
        this.numBags = numBags;
    }

    //constructor for pulling requests from the database

    /**
     * constructor for pulling requests from database
     * @param id - ride ID
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     * @param groupID - ID of ride group
     * @param context - context of app
     */
    public Request(int numBags, int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        super(id, email, destination, destCoordinates, start, startCoordinates, description, date, groupID, context);
        this.numBags = numBags;
    }

    /**
     * constructor for pulling requests from database
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     * @param groupID - ID of ride group
     * @param context - context of app
     */
    public Request(int numBags, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        super(email, destination, destCoordinates, start, startCoordinates, description, date, groupID, context);
        this.numBags = numBags;
    }

    /**
     * Returns request data as a string
     * @return request data as a string
     */
    @Override
    public String toString() {
        return "num bags=" + numBags +
                "\nemail=" + super.getEmail() +
                "\ndescription=" + super.getDescription() +
                "\ndate=" + super.getDate();
    }

    /**
     * returns number of bags for ride
     * @return number of bags
     */
    public int getNumBags() {
        return numBags;
    }
}

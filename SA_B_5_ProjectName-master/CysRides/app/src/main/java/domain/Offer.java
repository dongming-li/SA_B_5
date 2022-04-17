package domain;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Offer extends Ride {

    private double cost;

    /**
     * Default constructor
     */
    public Offer() {

    }

    /**
     * Constructor used for creating offers
     * @param cost - ride cost
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - ride starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     */
    public Offer(double cost, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date) {
        super(email, destination, destCoordinates, start, startCoordinates, description, date, "OFFER");
        this.cost = cost;
    }

    /**
     * Constructor used for creating offers
     * @param cost - cost of ride
     * @param id - ride id
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - ride starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     */
    public Offer(double cost, int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date) {
        super(id, email, destination, destCoordinates, start, startCoordinates, description, date, "OFFER");
        this.cost = cost;
    }

    /**
     * Constructor used to pull offers
     * @param cost - cost of ride
     * @param id - ride id
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - ride starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     * @param groupID - ID of ride group
     * @param context - context of app
     */
    public Offer(double cost, int id, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        super(id, email, destination, destCoordinates, start, startCoordinates, description, date, groupID, context);
        this.cost = cost;
    }

    /**
     * Constructor used to pull offers
     * @param cost - cost of ride
     * @param email - user's email
     * @param destination - ride destination
     * @param destCoordinates - destination coordinates
     * @param start - ride starting location
     * @param startCoordinates - starting coordinates
     * @param description - ride description
     * @param date - ride date
     * @param groupID - ID of ride group
     * @param context - context of app
     */
    public Offer(double cost, String email, String destination, LatLng destCoordinates, String start, LatLng startCoordinates, String description, Date date, int groupID, Context context) {
        super(email, destination, destCoordinates, start, startCoordinates, description, date, groupID, context);
        this.cost = cost;
    }

    /**
     * returns ride data in string form
     * @return ride data as a string
     */
    @Override
    public String toString() {
        return  "destination=" + super.getDestination() +
                "\nstart=" + super.getStart() +
                "\ncost=$" + cost +
                "\nemail=" + super.getEmail() +
                "\ndescription=" + super.getDescription() +
                "\ndate=" + super.getDate();
    }

    /**
     * returns cost of ride
     * @return cost of ride
     */
    public double getCost() {
        return cost;
    }

    /**
     * sets cost of ride
     * @param cost - ride cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

}

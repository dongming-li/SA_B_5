package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Offer;
import domain.UserInfo;
import volley.OfferVolley;
import volley.OfferVolleyImpl;

public class OfferServiceImpl implements OfferService {

    private OfferVolley offerVolley = new OfferVolleyImpl();

    //Formats the information before passing it into the volley code

    /**
     * creates ride offer
     * @param context - context of app
     * @param offer - offer to be created
     */
    @Override
    public void createOffer(Context context, Offer offer) {
        String destination = String.format("%s %s", offer.getDestination(), offer.getDestCoordinates().toString());
        String start = String.format("%s %s", offer.getStart(), offer.getStartCoordinates().toString());
        offerVolley.createOffer(context, offer, destination, start);
    }

    /**
     * removes offer from database
     * @param context - context of app
     * @param id - id of offer to be deleted
     */
    @Override
    public void deleteOffer(Context context, int id) {
        offerVolley.deleteOffer(context, id);
    }

    /**
     * returns all offers associated with user
     * @param offers - list of ride offers
     * @param userInfo - user's user info
     * @return - list of offers by user
     */
    @Override
    public ArrayList<Offer> findOffersByEmail(ArrayList<Offer> offers, UserInfo userInfo) {
        ArrayList<Offer> emailOffers = new ArrayList<>();
        for(int i=0 ; i<offers.size() ; i++) {
            if(offers.get(i).getEmail().equals(userInfo.getNetID())) {
                emailOffers.add(offers.get(i));
            }
        }
        return emailOffers;
    }
}

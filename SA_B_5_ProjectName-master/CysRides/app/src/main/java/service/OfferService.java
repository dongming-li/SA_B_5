package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Offer;
import domain.UserInfo;

public interface OfferService {
    /**
     * creates ride offer
     * @param context - context of app
     * @param offer - offer to be created
     */
    void createOffer(Context context, Offer offer);

    /**
     * removes offer from database
     * @param context - context of app
     * @param id - id of offer to be deleted
     */
    void deleteOffer(Context context, int id);

    /**
     * returns all offers associated with user
     * @param offers - list of ride offers
     * @param userInfo - user's user info
     * @return - list of offers by user
     */
    ArrayList<Offer> findOffersByEmail(ArrayList<Offer> offers, UserInfo userInfo);
}

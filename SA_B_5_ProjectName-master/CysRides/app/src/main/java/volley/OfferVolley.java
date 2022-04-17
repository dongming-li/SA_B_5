package volley;

import android.content.Context;

import domain.Offer;

public interface OfferVolley {

    /**
     * Add offer to the database
     * @param context of app
     * @param offer data
     * @param destination ride destination
     * @param start ride start location
     */
    void createOffer(Context context, Offer offer, String destination, String start);

    /**
     * Remove offer from database
     * @param context app context
     * @param id of ride offer
     */
    void deleteOffer(final Context context, final int id);

    /**
     * gives specified offer the given groupID
     * @param context of app
     * @param offerId id of ride offer
     * @param groupId id of group
     */
    void giveOfferGroup(Context context, final int offerId, final int groupId);

}

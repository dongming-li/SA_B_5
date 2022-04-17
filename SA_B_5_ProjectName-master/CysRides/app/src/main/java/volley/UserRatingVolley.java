package volley;

import android.content.Context;

public interface UserRatingVolley {

    /**
     * Updates a rating in the database
     * @param context - context of app
     * @param netID - user's netID
     * @param userRating - user's rating
     * @param numberRatings - amount of user ratings
     */
    void addRating(Context context, final String netID, final float userRating, final float numberRatings);
}

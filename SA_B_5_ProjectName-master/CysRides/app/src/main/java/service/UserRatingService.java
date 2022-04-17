package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Group;
import domain.UserInfo;

public interface UserRatingService {

    /**
     * updates user's rating
     * @param context - context of app
     * @param currentRating - user's current rating
     * @param newRating - user's new rating
     * @param numRatings - the amount of ratings for this user
     * @param user - the user's user info
     */
    void updateRating(Context context, float currentRating, float newRating, int numRatings, UserInfo user);

    /**
     * returns a list of groups user is in
     * @param userInfo - user's user info
     * @param groups - list of grous
     * @return list of groups user is a member of
     */
    ArrayList<Group> getGroupsByUser(UserInfo userInfo, ArrayList<Group> groups);

    /**
     * returns a list of members in groups
     * @param groups - list of groups
     * @return members of the groups
     */
    ArrayList<String> getMembersFromGroups(ArrayList<Group> groups);
}

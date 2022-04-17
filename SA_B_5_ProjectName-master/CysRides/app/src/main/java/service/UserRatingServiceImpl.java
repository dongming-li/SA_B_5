package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Group;
import domain.UserInfo;
import volley.UserRatingVolley;
import volley.UserRatingVolleyImpl;

public class UserRatingServiceImpl implements UserRatingService {

    /**
     * updates user's rating
     * @param context - context of app
     * @param currentRating - user's current rating
     * @param newRating - user's new rating
     * @param numRatings - the amount of ratings for this user
     * @param user - the user's user info
     */
    @Override
    public void updateRating(Context context, float currentRating, float newRating, int numRatings, UserInfo user){
        UserRatingVolley userRatingVolley = new UserRatingVolleyImpl();
        user.setUserRating(((currentRating * numRatings) + newRating) / (numRatings + 1));
        userRatingVolley.addRating(context, user.getNetID(), user.getUserRating(), (numRatings + 1));
    }

    /**
     * returns a list of groups user is in
     * @param userInfo - user's user info
     * @param groups - list of grous
     * @return list of groups user is a member of
     */
    @Override
    public ArrayList<Group> getGroupsByUser(UserInfo userInfo, ArrayList<Group> groups) {
        ArrayList<Group> userGroups = new ArrayList<>();
        for(int i=0 ; i<groups.size() ; i++) {
            ArrayList<String> groupMembers = groups.get(i).getGroupMembers();
            for(int j=0 ; j<groupMembers.size() ; j++) {
                if(groupMembers.get(j).equals(userInfo.getNetID())) {
                    userGroups.add(groups.get(i));
                }
            }
        }
        return userGroups;
    }

    /**
     * returns a list of members in groups
     * @param groups - list of groups
     * @return members of the groups
     */
    @Override
    public ArrayList<String> getMembersFromGroups(ArrayList<Group> groups) {
        ArrayList<String> groupMembers = new ArrayList<>();
        for(int i=0 ; i<groups.size() ; i++) {
            ArrayList<String> gM = groups.get(i).getGroupMembers();
            for(int j=0 ; j<gM.size() ; j++) {
                if(!gM.get(j).equals("null")) {
                    groupMembers.add(gM.get(j));
                }
            }
        }
        return groupMembers;
    }
}

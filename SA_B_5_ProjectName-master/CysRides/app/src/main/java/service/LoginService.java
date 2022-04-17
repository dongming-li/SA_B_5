package service;

import java.util.ArrayList;

import domain.Ban;
import domain.UserInfo;

public interface LoginService {

    /**
     * returns user's info
     * @param users - list of users
     * @param netID - user's netID
     * @param enteredPassword - user's entered password
     * @return user info of user
     */
    UserInfo getUserInfo(ArrayList<UserInfo> users, String netID, String enteredPassword);
}

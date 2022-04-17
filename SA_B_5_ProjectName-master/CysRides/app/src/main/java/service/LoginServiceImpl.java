package service;

import java.util.ArrayList;

import domain.Ban;
import domain.UserInfo;

public class LoginServiceImpl implements LoginService {

    /**
     * With the given list of users, and the given net-id and password, it checks that the password
     * entered corresponds to the password in the database from the given user.
     * @param users - list of users
     * @param netID - user's netID
     * @param enteredPassword - user's entered password
     * @return user's user info
     */
    @Override
    public UserInfo getUserInfo(ArrayList<UserInfo> users, String netID, String enteredPassword) {
        UserInfo userInfo;
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getNetID().equals(netID)) {
                    userInfo = users.get(i);
                    if(enteredPassword.equals(userInfo.getPassword())){
                        return userInfo;
                    }
                }
            }
        }
        return null;
    }
}

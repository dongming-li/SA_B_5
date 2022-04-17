package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Request;
import domain.UserInfo;
import volley.RequestVolley;
import volley.RequestVolleyImpl;

public class RequestServiceImpl implements RequestService {

    private RequestVolley requestVolley = new RequestVolleyImpl();

    /**
     * creates ride request
     * @param context - context of app
     * @param request - request to be created
     */
    @Override
    public void createRequest(Context context, Request request) {
        String destName = String.format("%s %s", request.getDestination(), request.getDestCoordinates().toString());
        String startName = String.format("%s %s", request.getStart(), request.getStartCoordinates().toString());
        requestVolley.createRequest(context, request, destName, startName);
    }

    /**
     * removes request from database
     * @param context - context of app
     * @param id - id of request to be deleted
     */
    @Override
    public void deleteRequest(Context context, int id) {
        requestVolley.deleteRequest(context, id);
    }

    //Finds the requests for a specific email

    /**
     * returns a list of all requests by a user
     * @param requests - list of requests
     * @param userInfo - user's user info
     * @return list of ride requests by user
     */
    @Override
    public ArrayList<Request> findRequestsByEmail(ArrayList<Request> requests, UserInfo userInfo) {
        ArrayList<Request> emailRequests = new ArrayList<>();
        for(int i=0 ; i<requests.size() ; i++) {
            if(requests.get(i).getEmail().equals(userInfo.getNetID())) {
                emailRequests.add(requests.get(i));
            }
        }
        return emailRequests;
    }
}

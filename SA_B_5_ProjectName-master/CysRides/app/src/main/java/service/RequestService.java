package service;

import android.content.Context;

import java.util.ArrayList;

import domain.Request;
import domain.UserInfo;

public interface RequestService {
    /**
     * creates ride request
     * @param context - context of app
     * @param request - request to be created
     */
    void createRequest(Context context, Request request);

    /**
     * removes request from database
     * @param context - context of app
     * @param id - id of request to be deleted
     */
    void deleteRequest(Context context, int id);

    /**
     * returns a list of all requests by a user
     * @param requests - list of requests
     * @param userInfo - user's user info
     * @return list of ride requests by user
     */
    ArrayList<Request> findRequestsByEmail(ArrayList<Request> requests, UserInfo userInfo);
    }

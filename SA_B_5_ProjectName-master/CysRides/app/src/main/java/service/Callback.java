package service;

import java.util.ArrayList;

public interface Callback {

    /**
     * Method that returns an arraylist of data to the caller
     * @param result of database pull
     */
    void call(ArrayList<?> result);

}

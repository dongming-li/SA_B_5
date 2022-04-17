package domain;

public class GOR {
    private Group group;
    private Offer offer;
    private Request request;

    /**
     * Creates new GOR Object (Group, Offer, Request
     * @param group - a group
     * @param offer - offer associated with that group (null if a request)
     * @param request - request associated with that group (null if an offer)
     */
    public GOR(Group group, Offer offer, Request request){
        this.group = group;
        this.offer = offer;
        this.request = request;
    }

    /**
     * Get the Group contained by a GOR
     * @return Group contained by a GOR
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Set the Group contained by a GOR
     * @param group - Group to be used by GOR
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Get the offer contained by a GOR
     * @return Offer contained by a GOR
     */
    public Offer getOffer() {
        return offer;
    }

    /**
     * Set the Offer contained by a GOR
     * @param offer - offer to be used by GOR
     */
    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    /**
     * Get the Request contained by a GOR
     * @return Request contained by a GOR
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Set the Request contained by a GOR
     * @param request - Request to be used by GOR
     */
    public void setRequest(Request request) {
        this.request = request;
    }
}

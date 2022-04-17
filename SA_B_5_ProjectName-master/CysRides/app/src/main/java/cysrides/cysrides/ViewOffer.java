package cysrides.cysrides;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import domain.Offer;

public class ViewOffer extends RideFragment {

    private Offer offer;

    /**
     * Required empty public constructor
     */
    public ViewOffer() {
        // Required empty public constructor
    }

    /**
     * initializes data to be displayed
     * @param inflater inflates the fragment
     * @param container fragment view
     * @param savedInstanceState app info
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_ride, container, false);
        setTextInfo(v);
        v.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupService.addRider(context, offer.getGroup(), userInfo.getNetID());

            }
        });

        v.findViewById(R.id.JoinAsDriverButton).setVisibility(View.GONE);

        return v;
    }

    /**
     * Sets fragment data
     * @param offer data
     * @param <T> offer
     */
    @Override
    public <T> void setData(T offer) {
        this.offer = (Offer) offer;
    }

    /**
     * sets text for non-admins
     * @param v view to be set
     */
    @Override
    protected void setNonAdminTextInfo(View v) {
        type = v.findViewById(R.id.ride_type);
        type.setText(R.string.ride_offer);
        type.setPaintFlags(type.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        v.findViewById(R.id.id).setVisibility(View.GONE);

        destination = v.findViewById(R.id.destination);
        destination.setText(String.format("Destination: %s", offer.getDestination()));

        start = v.findViewById(R.id.start);
        start.setText(String.format("Start: %s", offer.getStart()));

        cost = v.findViewById(R.id.cost);
        cost.setText(String.format(Locale.US, "Cost: $%.2f", offer.getCost()));

        v.findViewById(R.id.num_bags).setVisibility(View.GONE);

        email = v.findViewById(R.id.email);
        email.setText(String.format("User: %s", offer.getEmail()));

        description = v.findViewById(R.id.description);
        description.setText(String.format("Description: %s", offer.getDescription()));

        date = v.findViewById(R.id.date);
        date.setText(String.format("Leave Date: %s", super.getDate(offer.getDate())));
    }

    /**
     * sets text for admins
     * @param v view to be set
     */
    @Override
    protected void setAdminTextInfo(View v) {
        type = v.findViewById(R.id.ride_type);
        type.setText(R.string.ride_offer);
        type.setPaintFlags(type.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        id = v.findViewById(R.id.id);
        id.setText(String.format(Locale.US, "Ride ID: %d", offer.getId()));

        destination = v.findViewById(R.id.destination);
        destination.setText(String.format("Destination: %s", offer.getDestination()));

        start = v.findViewById(R.id.start);
        start.setText(String.format("Start: %s", offer.getStart()));

        cost = v.findViewById(R.id.cost);
        cost.setText(String.format(Locale.US, "Cost: $%.2f", offer.getCost()));

        v.findViewById(R.id.num_bags).setVisibility(View.GONE);

        email = v.findViewById(R.id.email);
        email.setText(String.format("User: %s", offer.getEmail()));

        description = v.findViewById(R.id.description);
        description.setText(String.format("Description: %s", offer.getDescription()));

        date = v.findViewById(R.id.date);
        date.setText(String.format("Leave Date: %s", super.getDate(offer.getDate())));
    }
}
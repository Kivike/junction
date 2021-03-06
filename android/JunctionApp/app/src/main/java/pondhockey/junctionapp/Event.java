package pondhockey.junctionapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Class holds all the data for an event
 */

public class Event implements Serializable {
    private int id;
    private String title;
    private String description;

    private SimpleDateFormat startDate;
    private SimpleDateFormat endDate;

    private int sportType;

    private double latitude;
    private double longitude;

    private int participatorCount;

    LatLng mapPoint;

    private boolean userParticipating;

    // Does user want an alert for this event
    private boolean alert;

    // Sport type enum needed?

    public Event(String title, String description, SimpleDateFormat startDate, SimpleDateFormat endDate, int sportType, LatLng location) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sportType = sportType;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public Event(int id, String title, String description, SimpleDateFormat startDate, SimpleDateFormat endDate, int sportType, LatLng location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sportType = sportType;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    /*
     * Participate (or unparticipate) to event
     */
    public void participate(boolean participate) {
        userParticipating = participate;
    }

    public boolean isUserParticipating() {
        return userParticipating;
    }

    public int getId(){return id;}

    public String getTitle(){ return title;}

    public String getDescription(){ return description; }

    public SimpleDateFormat getStartDate() {
        return startDate;
    }

    public SimpleDateFormat getEndDate() {
        return endDate;
    }

    public int getSportType(){ return sportType;}

    public LatLng getLocation(){ return new LatLng(latitude, longitude);}

    public String getLocationString(){
        String str = new DecimalFormat("#.####").format(latitude) + " " + new DecimalFormat("#.####").format(longitude);
        return str;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean getAlert() {
        return this.alert;
    }

    /*
     * Returns event duration in minutes
     */
    public long getDuration() {
        long diff = endDate.getCalendar().getTime().getTime() - startDate.getCalendar().getTime().getTime();

        return diff / (60 * 1000);
    }
}

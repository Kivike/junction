package pondhockey.junctionapp;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Class holds all the data for an event
 */
public class Event {
    public String title;
    public String description;

    private SimpleDateFormat startDate;
    private SimpleDateFormat endDate;

    private int participatorCount;

    LatLng mapPoint;

    private boolean userParticipating;

    // Does user want an alert for this event
    private boolean alert;

    // Sport type enum needed?


    public Event(String title, String description, SimpleDateFormat startDate, SimpleDateFormat endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public SimpleDateFormat getStartDate() {
        return startDate;
    }

    public SimpleDateFormat getEndDate() {
        return endDate;
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
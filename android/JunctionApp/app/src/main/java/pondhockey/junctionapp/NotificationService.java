package pondhockey.junctionapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.os.Handler;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

public class NotificationService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    private ArrayList<Event> lastUpdate = new ArrayList<Event>();

    @Override
    public void onCreate() {
        // The service is being create
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService(

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getEvents();
                //createNotification();
            }
        }, 0, 10, TimeUnit.SECONDS);
        return mStartMode;
    }

    private void getEvents(){
        ArrayList<Event> gottenEvents = HTTP.getInstance().getEvents("test");

        for(int i = 0; i < gottenEvents.size() - lastUpdate.size(); i++){
            //Create new notification
            Event newEvent = gottenEvents.get(lastUpdate.size() + i);
            String title = sportTypeToSport(newEvent.getSportType()) + " - " + newEvent.getTitle();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd hh:mm");
            String description = simpleDateFormat.format(newEvent.getStartDate().getCalendar().getTime()) + " - " + newEvent.getDescription();
            createNotification(title, description);
        }

        lastUpdate = gottenEvents;
    }

    private String sportTypeToSport(int type){
        switch(type){
            case 0:return "Football";
            case 1:return "Basketball";
            case 2:return "Ice-Hockey";
            case 3:return "Jogging";
            case 4:return "Tennis";
        }
        return "";
    }

    private void createNotification(String title, String message){
        Notification notification = new Notification.Builder(this)
               // .setContentTitle("Test")
               // .setContentText("Hello World")
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(this, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }
}

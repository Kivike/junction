package pondhockey.junctionapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.StrictMode;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;

    ArrayList<Event> events;
    Button[] eventButtons;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    TelephonyManager tm;
    public static String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        android_id = "" + tm.getDeviceId().hashCode();

        setContentView(R.layout.activity_glocal);

        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        getEvents();

        Button addEventButton = (Button) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                intent.putExtra("YEAR", selectedYear);
                intent.putExtra("MONTH", selectedMonth);
                intent.putExtra("DAY", selectedDay);
                startActivity(intent);
            }
        });

        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setShownWeekCount(2);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                updateEventList(year, month, dayOfMonth);
            }
        });

        startService(new Intent(this, NotificationService.class));
    }

    /*
     * Find events and create buttons for them
     */
    private void getEvents() {
        events = getSampleEvents();
        eventButtons = new Button[events.size()];
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scrollLayout);

        for(int i = 0; i < events.size(); i++) {
            final Event event = events.get(i);

            eventButtons[i] = new Button(this);
            eventButtons[i].setHeight(70);
            eventButtons[i].setMinWidth(200);
            eventButtons[i].setMinHeight(70);
            eventButtons[i].setTag(i);
            eventButtons[i].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MainActivity.this, EventDetails.class);
                   intent.putExtra("EVENT", event);
                   startActivity(intent);
               }
            });

            String buttonText = event.getTitle() + " 17:15 - 18:15";

            if(event.isUserParticipating()) {
                eventButtons[i].setBackgroundColor(Color.GREEN);
            }

            eventButtons[i].setText(buttonText);

            scroll.addView(eventButtons[i]);
        }
    }

    private ArrayList<Event> getSampleEvents() {
        ArrayList<Event> sampleEvents = new ArrayList<>();
        sampleEvents.add(new Event("Football", "asddsdsaad", null, null, 1, null));
        sampleEvents.add(new Event("Ice hockey", "fdsssssssssssssssssssssss", null, null, 1, null));
        sampleEvents.add(new Event("Running", "sgfdgdgdgddhdhdh", null, null, 1, null));

        Event aaa = new Event("Floorball", "", null, null, 1, null);
        aaa.participate(true);
        sampleEvents.add(aaa);

        return sampleEvents;
    }

    private void updateEventList(int year, int month, int dayOfMonth) {
        //
    }
}

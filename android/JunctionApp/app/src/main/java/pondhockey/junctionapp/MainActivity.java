package pondhockey.junctionapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;

    ArrayList<Event> events;
    ArrayList<Event> filteredEvents;

    Event focusedEvent;

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

        selectedYear = 2015;
        selectedMonth = 11;
        selectedDay = 8;

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                updateEventButtons();

            }
        });

        startService(new Intent(this, NotificationService.class));

        new LongOperation().execute("ABC");
    }

    public void updateEventButtons() {
        filterEvents();

        LinearLayout scroll = (LinearLayout) findViewById(R.id.scrollLayout);
        scroll.removeAllViews();

        eventButtons = new Button[filteredEvents.size()];

        for(int i = 0; i < filteredEvents.size(); i++) {
            final Event event = filteredEvents.get(i);

            eventButtons[i] = new Button(this);
            eventButtons[i].setHeight(70);
            eventButtons[i].setMinWidth(200);
            eventButtons[i].setMinHeight(70);
            eventButtons[i].setTag(i);
            eventButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, EventDetails.class);

                    int eventIndex = (int) v.getTag();
                    focusedEvent = filteredEvents.get(eventIndex);

                    intent.putExtra("EVENT", filteredEvents.get(eventIndex));
                    startActivityForResult(intent, 0);
                }
            });

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            String startTimeString = timeFormat.format(event.getStartDate().getCalendar().getTime());
            String endTimeString = timeFormat.format(event.getEndDate().getCalendar().getTime());

            String buttonText = event.getTitle() + " " + startTimeString + "-" + endTimeString;

            if(event.isUserParticipating()) {
                eventButtons[i].setBackgroundColor(Color.GREEN);
            }

            eventButtons[i].setText(buttonText);

            scroll.addView(eventButtons[i]);
        }
    }

    private void filterEvents() {
        filteredEvents = new ArrayList<>();

        for(int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            Calendar eventCalendar = event.getStartDate().getCalendar();
            int year = eventCalendar.get(Calendar.YEAR);
            int month = eventCalendar.get(Calendar.MONTH);
            int day = eventCalendar.get(Calendar.DAY_OF_MONTH);

            Log.e("ses", year + " " + month + " " + day);
            Log.e("asd", selectedYear + " " + selectedMonth + " " + selectedDay);

            if(year == selectedYear && month == selectedMonth && day == selectedDay) {
                filteredEvents.add(event);
            }
        }
    }

    /*
     * Find events and create buttons for them
     */
    private void getEvents() {
        events = HTTP.getInstance().getEvents(MainActivity.android_id);
    }

    private void updateEventList(int year, int month, int dayOfMonth) {
        //
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            getEvents();

            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            updateEventButtons();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == 0) {
                boolean signed = data.getExtras().getBoolean("SIGNEDUP");
                Log.e("res", String.valueOf(signed));
                focusedEvent.participate(signed);
                updateEventButtons();
            }
        }
    }
}

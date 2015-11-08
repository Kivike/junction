package pondhockey.junctionapp;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glocal);

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
    }

    /*
     * Find events and create buttons for them
     */
    private void getEvents() {
        events = getSampleEvents();
        eventButtons = new Button[events.size()];
        LinearLayout scroll = (LinearLayout) findViewById(R.id.scrollLayout);

        for(int i = 0; i < events.size(); i++) {
            eventButtons[i] = new Button(this);
            eventButtons[i].setHeight(70);
            eventButtons[i].setMinWidth(200);
            eventButtons[i].setMinHeight(70);
            eventButtons[i].setTag(i);
            eventButtons[i].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MainActivity.this, EventDetails.class);
                   intent.putExtra("EVENT_ID", 252);
                   startActivity(intent);
               }
            });

            Event event = events.get(i);
            String buttonText = event.title + " 17:15 - 18:15";

            if(event.isUserParticipating()) {
                eventButtons[i].setBackgroundColor(Color.GREEN);
            }

            eventButtons[i].setText(buttonText);

            scroll.addView(eventButtons[i]);
        }
    }

    private ArrayList<Event> getSampleEvents() {
        ArrayList<Event> sampleEvents = new ArrayList<>();
        sampleEvents.add(new Event("Football", "", null, null));
        sampleEvents.add(new Event("Ice hockey", "", null, null));
        sampleEvents.add(new Event("Running", "", null, null));

        Event aaa = new Event("Floorball", "", null, null);
        aaa.participate(true);
        sampleEvents.add(aaa);

        return sampleEvents;
    }

    private void updateEventList(int year, int month, int dayOfMonth) {
        //
    }
}

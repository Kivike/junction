package pondhockey.junctionapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Calendar;

public class Glocal extends AppCompatActivity{
    CalendarView calendar;
    ListView eventList;

    ArrayList<String> events;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glocal);

        events = new ArrayList<>();
        events.add("Football 17:00 - 18:00");
        events.add("Basketball 18:30 - 19:30");
        events.add("Tennis 19:15 - 20:30");

        eventList = (ListView) findViewById(R.id.eventListView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        eventList.setAdapter(listAdapter);

        Button addEventButton = (Button) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Glocal.this, AddEventActivity.class));
            }
        });

        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setShownWeekCount(2);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateEventList(year, month, dayOfMonth);
            }
        });


    }

    private void getAllEvents() {

    }

    private void updateEventList(int year, int month, int dayOfMonth) {
        //
    }
}

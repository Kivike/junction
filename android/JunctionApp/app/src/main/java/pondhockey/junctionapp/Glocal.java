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

import java.util.ArrayList;
import java.util.Calendar;

public class Glocal extends ListActivity {
    CalendarView calendar;
    ListView eventList;

    ArrayList<String> events;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glocal);

        events = new ArrayList<>();
        events.add("Footbal");
        events.add("Basketball");
        events.add("Tennis");

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

        //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, events);
        //setListAdapter(listAdapter);

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

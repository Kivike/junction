package pondhockey.junctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    enum RequestCode {START_TIME, END_TIME, LOCATION }

    SimpleDateFormat startDate = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    SimpleDateFormat endDate = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    int startHours;
    int startMinutes;

    int endHours;
    int endMinutes;

    float latitude;
    float longitude;

    private int sportType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Button startTimeButton = (Button) findViewById(R.id.startTimeButton);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventActivity.this, TimeSelectActivity.class);
                startActivityForResult(intent, RequestCode.START_TIME.ordinal());
            }
        });

        Button endTimeButton = (Button) findViewById((R.id.endTimeButton));
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventActivity.this, TimeSelectActivity.class);
                startActivityForResult(intent, RequestCode.END_TIME.ordinal());
            }
        });

        Button selectLocationButton = (Button) findViewById(R.id.locationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(AddEventActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.sport_spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Football");
        categories.add("Basketball");
        categories.add("Ice-Hockey");
        categories.add("Jogging");
        categories.add("Tennis");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button createEventButton = (Button) findViewById(R.id.addEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });
    }

    private void addEvent() {
        String title = ((TextView) findViewById(R.id.titleTextBox)).getText().toString();
        String description = ((TextView) findViewById(R.id.descriptionTextBox)).getText().toString();

        startDate.getCalendar().add(Calendar.HOUR_OF_DAY, startHours);
        startDate.getCalendar().add(Calendar.MINUTE, startMinutes);

        endDate.getCalendar().add(Calendar.HOUR_OF_DAY, endHours);
        endDate.getCalendar().add(Calendar.MINUTE, endMinutes);

        Event newEvent = new Event(title, description, startDate, endDate, sportType, MapsActivity.markerLocation);

        // TODO: Send new event to server
        HTTP.getInstance().createEvent(newEvent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == RequestCode.START_TIME.ordinal()) {
                startHours = data.getExtras().getInt("HOURS");
                startMinutes = data.getExtras().getInt("MINUTES");
                updateStartTimeText();
            } else if(requestCode == RequestCode.END_TIME.ordinal()) {
                endHours = data.getExtras().getInt("HOURS");
                endMinutes = data.getExtras().getInt("MINUTES");
                updateEndTimeText();
            } else if(requestCode == RequestCode.LOCATION.ordinal()) {
                latitude = data.getExtras().getFloat("LATITUDE");
                longitude = data.getExtras().getFloat("LONGITUDE");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        sportType = position;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void updateStartTimeText() {
        TextView startTimeText = (TextView) findViewById(R.id.startTimeText);
        startTimeText.setText(String.format("%d:%d", startHours, startMinutes));

        if(endHours < startHours || (endHours == startHours && endMinutes < startMinutes)) {
            endHours = startHours;
            endMinutes = startMinutes;
        }
    }

    private void updateEndTimeText() {
        TextView endTimeText = (TextView) findViewById(R.id.endTimeText);
        endTimeText.setText(String.format("%d:%d", endHours, endMinutes));

    }
}

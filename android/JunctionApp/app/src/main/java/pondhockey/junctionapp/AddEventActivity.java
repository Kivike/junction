package pondhockey.junctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {
    enum RequestCode {START_TIME, END_TIME, LOCATION }

    SimpleDateFormat startDate;
    SimpleDateFormat endDate;

    int startHours;
    int startMinutes;

    int endHours;
    int endMinutes;

    float latitude;
    float longitude;

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

        Event newEvent = new Event(title, description, startDate, endDate);

        // TODO: Send new event to server
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == RequestCode.START_TIME.ordinal()) {
                startHours = data.getExtras().getInt("HOURS");
                startMinutes = data.getExtras().getInt("MINUTES");
            } else if(requestCode == RequestCode.END_TIME.ordinal()) {
                endHours = data.getExtras().getInt("HOURS");
                endMinutes = data.getExtras().getInt("MINUTES");
            } else if(requestCode == RequestCode.LOCATION.ordinal()) {
                latitude = data.getExtras().getFloat("LATITUDE");
                longitude = data.getExtras().getFloat("LONGITUDE");
            }

            updateTimeTexts();
        }
    }

    private void updateTimeTexts() {
        TextView startTimeText = (TextView) findViewById(R.id.startTimeText);
        TextView endTimeText = (TextView) findViewById(R.id.endTimeText);

        startTimeText.setText(String.format("%d:%d", startHours, startMinutes));
        endTimeText.setText(String.format("%d:%d", endHours, endMinutes));
    }

}

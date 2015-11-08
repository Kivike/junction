package pondhockey.junctionapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity {
    Event event;
    Button backButton;
    Button participationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        participationButton = (Button) findViewById(R.id.leaveEventButton);
        participationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.participate(!event.isUserParticipating());
                updateParticipationButton();
            }
        });

        int eventId = getIntent().getExtras().getInt("EVENT_ID");

        setEventData(eventId);

        Button backButton = (Button) findViewById(R.id.detailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEventData(int eventId) {
        // TODO: Get event data from database
        event = getSampleEvent();

        TextView title = (TextView) findViewById(R.id.eventTitle);
        TextView description = (TextView) findViewById(R.id.eventDescription);
        TextView date = (TextView) findViewById(R.id.eventDate);
        TextView time = (TextView) findViewById(R.id.eventTime);
        TextView participants = (TextView) findViewById(R.id.participantCount);

        String eventDate = "15.5.";
        String eventTime = "16:15-17:45";
        int eventParticipants = 10;

        title.setText(event.title);
        description.setText(event.description);
        date.setText(eventDate);
        time.setText(eventTime);
        participants.setText("People participating: " + String.valueOf(eventParticipants));

        updateParticipationButton();

        // TODO: Set map marker
    }

    private Event getSampleEvent() {
        Event sample = new Event("Football", "Playing football", null, null);
        //sample.participate(true);

        return sample;
    }

    private void updateParticipationButton() {
        if(event.isUserParticipating()) {
            participationButton.setText("Leave event");
            participationButton.setBackgroundColor(Color.parseColor("#733939"));
        } else {
            participationButton.setText("Sign up");
            participationButton.setBackgroundColor(Color.parseColor("#6d905f"));
        }
    }
}

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDetails extends AppCompatActivity implements OnMapReadyCallback {
    Event event;
    Button backButton;
    Button participationButton;

    GoogleMap mMap;
    LatLng eventLocation;

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

        event = (Event) getIntent().getExtras().getSerializable("EVENT");

        setEventData(event);

        Button backButton = (Button) findViewById(R.id.detailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.eventMap);
        mapFragment.getMapAsync(this);
    }

    private void setEventData(Event event) {
        TextView title = (TextView) findViewById(R.id.eventTitle);
        TextView description = (TextView) findViewById(R.id.eventDescription);
        TextView date = (TextView) findViewById(R.id.eventDate);
        TextView time = (TextView) findViewById(R.id.eventTime);
        TextView participants = (TextView) findViewById(R.id.participantCount);

        String eventDate = "15.5.";
        String eventTime = "16:15-17:45";
        int eventParticipants = 10;

        eventLocation = event.getLocation();

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        date.setText(eventDate);
        time.setText(eventTime);
        participants.setText("People participating: " + String.valueOf(eventParticipants));

        updateParticipationButton();

        // TODO: Set map marker
    }

    private Event getSampleEvent() {
        Event sample = new Event("Football", "Playing football", null, null, 1, null);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(eventLocation).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 18f));
    }
}

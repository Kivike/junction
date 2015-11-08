package pondhockey.junctionapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;


public class SettingsActivity extends AppCompatActivity {
    TextView seekBarText;

    int seekBarValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBarText = (TextView) findViewById(R.id.seekbarvalue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarText.setText(String.valueOf(i) + " km");
                seekBarValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng markerLocation = MapsActivity.markerLocation;
                String coordinates = new DecimalFormat("#.####").format(markerLocation.latitude) + " " + new DecimalFormat("#.####").format(markerLocation.longitude);

                new CreateAccountAsync().execute(coordinates);
            }
        });

        Button selectLocationButton = (Button) findViewById(R.id.locationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(SettingsActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        //setupActionBar();
    }

    private class CreateAccountAsync extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... params){
            HTTP http = HTTP.getInstance();


            if(http.checkUserExists(MainActivity.android_id)){
                http.changeSettings(MainActivity.android_id, params[0], seekBarValue);
            }else {
                http.createNewUser(MainActivity.android_id, params[0], new int[]{1,2,3}, seekBarValue);
            }

            return 0;
        }
    }
}
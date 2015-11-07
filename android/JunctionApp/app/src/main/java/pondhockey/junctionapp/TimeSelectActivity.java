package pondhockey.junctionapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeSelectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_select);

        Button confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();

                TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
                resultIntent.putExtra("HOURS", timePicker.getCurrentHour());
                resultIntent.putExtra("MINUTES", timePicker.getCurrentMinute());

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}

package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * TimeScheduleFragmentをこのアクティビティの上に乗せます
 */
public class TimeScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_schedule);
    }
}
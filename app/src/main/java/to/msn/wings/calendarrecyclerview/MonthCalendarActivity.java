package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * この第二画面のアクティビティの上に、MonthCalendarFragment
 */
public class MonthCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);
    }
}
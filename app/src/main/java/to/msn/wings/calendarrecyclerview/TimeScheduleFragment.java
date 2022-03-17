package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeScheduleFragment extends Fragment {

    private Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_schedule, container, false);

        Activity parentActivity = getActivity();
        Intent intent = parentActivity.getIntent();  // CalendarAdapterクラスのリスナーで画面遷移するように実装してる

        Bundle extras = intent.getExtras();
        String scheduleDayText ="";
        String todayString = "";
        Date date = null;
        if(extras != null) {
            scheduleDayText = intent.getStringExtra("scheduleDayText");
            todayString = intent.getStringExtra("todayString");
            if (scheduleDayText != null && scheduleDayText != "") {
                try {
                    date = new SimpleDateFormat("yyyy/MM/dd").parse(scheduleDayText);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        TextView day = view.findViewById(R.id.titleText); // fragment_time_schedule.xmlの idが titleTextのもの
        day.setText(scheduleDayText);


        TextView day_today = view.findViewById(R.id.day_today);
        if (!todayString.equals("")) {
            todayString = "今日の予定 " + todayString + " ";
        }
        day_today.setText(todayString);


        // タイムスケジュールを新規登録するボタンにリスナーをつける
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // 最後にreturn viewをすること
        return view;

    }
}
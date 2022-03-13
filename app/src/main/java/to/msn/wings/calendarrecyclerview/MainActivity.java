package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView titleText;
    private Button prevButton, nextButton;
    private CalendarAdapter calendarAdapter;
    private RecyclerView recyclerView;
    DateManager dateManager;

    private int SPAN_COUNT = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateManager = new DateManager();
        List<Date> dates =  dateManager.getDays();
        int count = dates.size();

        SimpleDateFormat format = new SimpleDateFormat("d");  // "dd" だと　　01  02 となってしまう


        ArrayList<CalendarCellItem> data = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            CalendarCellItem item = new CalendarCellItem();
            item.setId((new Random()).nextLong());
            Date date = dates.get(i);

            String display = format.format(date);

             item.setDateText(display);  // セットします
             data.add(item);
        }




        titleText = findViewById(R.id.titleText);
        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上

        // グリッド状にカードを配置する 7つづつ
        GridLayoutManager manager = new GridLayoutManager(this, SPAN_COUNT);
        rv.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new CalendarAdapter(data);
        rv.setAdapter(adapter);
    }
}
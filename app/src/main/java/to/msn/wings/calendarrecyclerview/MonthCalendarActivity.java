package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthCalendarActivity extends AppCompatActivity {

    private TextView titleText;
    private Button prevButton, nextButton;
    private CalendarAdapter calendarAdapter;
    private RecyclerView recyclerView;
    DateManager dateManager;

    private int SPAN_COUNT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);

        Intent intent = getIntent();

        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
        Date prevButtonDate = (Date)intent.getSerializableExtra("prevButtonDate");
        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか どっちかは nullになるので
        Date nextButtonDate = (Date)intent.getSerializableExtra("nextButtonDate");

        dateManager = new DateManager();


        List<Date> dates = null;


        titleText = findViewById(R.id.titleText);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年 MM月");
        // ここで条件分岐します
        String title = "";
        if (prevButtonDate != null) {
           title = format.format(prevButtonDate);
            dates = dateManager.getDays(prevButtonDate);  //引数ありのgetDays(Date date)　を呼び出す

        } else if (nextButtonDate != null) {
            title = format.format(nextButtonDate);
            dates = dateManager.getDays(nextButtonDate); //引数ありのgetDays(Date date)　を呼び出す

        }
        titleText.setText(title);

// フォーマットし直し
        format = new SimpleDateFormat("d");  // "dd" だと　　01  02 となってしまう

        /**
         * 表示だけのリスト
         */
        ArrayList<CalendarCellItem> data = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            CalendarCellItem item = new CalendarCellItem();
            // item.setId((new Random()).nextLong());

            item.setId(i + 1);  // 1から順に通し番号をふる
            Date date = dates.get(i);

            String display = format.format(date);

            item.setDateText(display);  // セットします

            data.add(item);
        }

        // マニフェストファイルによって、activity_month_calendar.xmlとの紐付けができてるので、
        // idとかもこのまま使える メインレイアウトと同じだけど問題なく使える
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上

        // グリッド状にカードを配置する 7つづつ
        GridLayoutManager manager = new GridLayoutManager(this, SPAN_COUNT);
        rv.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new CalendarAdapter(data);
        rv.setAdapter(adapter);

    }
}
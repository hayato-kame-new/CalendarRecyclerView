package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendarFragment extends Fragment {

    private TimeScheduleDatabaseHelper _helper;
    private TextView titleText;
    private Button prevButton, nextButton, currentMonthButton;
//    private CalendarAdapter calendarAdapter;
//    private RecyclerView recyclerView;
    DateManager dateManager;
    private int SPAN_COUNT = 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity parentActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        Intent intent = parentActivity.getIntent();
        _helper = new TimeScheduleDatabaseHelper(parentActivity);  // onDestroy()で helperを解放すること
        Date prevButtonDate = null;
        Date nextButtonDate = null;
        Date specifyDate = null;  // ScheduleFormから遷移してきた時 指定する月は現在の月ジャない時にだけここにくる
        Bundle extras = intent.getExtras();
        if(extras != null) {
            //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
             prevButtonDate = (Date)intent.getSerializableExtra("prevButtonDate");  // nullが入ってるかもしれないです
            //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか どっちかは nullになるので
             nextButtonDate = (Date)intent.getSerializableExtra("nextButtonDate");  // nullが入ってるかもしれないです
            // null かどうかのチェックが必要です
            specifyDate = (Date)intent.getSerializableExtra("specifyDate");
        }

        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
   //     Date prevButtonDate = (Date)intent.getSerializableExtra("prevButtonDate");
        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか どっちかは nullになるので
    //     Date nextButtonDate = (Date)intent.getSerializableExtra("nextButtonDate");

        // 比較をするために本日現在を取得する
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;  // 今日現在の月
        int day = calendar.get(Calendar.DATE);  // 今日現在の日

        dateManager = new DateManager();

        List<Date> dates = null;


        titleText = view.findViewById(R.id.titleText);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年 MM月");
        // ここで条件分岐します
        String title = "";
        if (prevButtonDate != null) {
            title = format.format(prevButtonDate);
            dates = dateManager.getDays(prevButtonDate);  // 引数ありのgetDays(Date date)　を呼び出す

        } else if (nextButtonDate != null) {
            title = format.format(nextButtonDate);
            dates = dateManager.getDays(nextButtonDate); // 引数ありのgetDays(Date date)　を呼び出す

        } else if (specifyDate != null) {  // 指定の日付のカレンダーを表示するならば、指定の日付が現在の月ではないなら、このフラグメントで表示します
            title = format.format(specifyDate);
            dates = dateManager.getDays(specifyDate);  // 引数ありのgetDays(Date date)　を呼び出す
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


        // 最初の土曜日は、その月に必ずなってるから
        Date firstSaturdayDate = dates.get(6);

        // 表示してる月よりも１つ前の月を表示するためのボタン
        prevButton = view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstSaturdayDate);
                calendar.add(Calendar.MONTH, -1);  // -1 をして ひと月前の最初の土曜日の日付にしています

                Date date = new Date();
                // Date型に変換する これを遷移先に送ります これで1月前の最初の土曜日の日付が取得できている
                date = calendar.getTime();

                Intent intent = new Intent(parentActivity, MonthCalendarActivity.class);
                intent.putExtra("prevButtonDate", date);  // 1月前の最初の土曜日の日付を送る Date型情報を渡します
                startActivity(intent);
                // こっちは終了させない自分自身のアクティビティは これはうまくいく　OK　
                // やっぱり書く
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }

        });

        // 次の月を表示するためのボタン
        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstSaturdayDate);
                calendar.add(Calendar.MONTH, 1);  // +1 してる ひと月先に
                Date date = new Date();
                // これで1月先の最初の土曜日の日付が取得できている
                date = calendar.getTime();

                Intent intent = new Intent(parentActivity, MonthCalendarActivity.class);
                    intent.putExtra("nextButtonDate", date);  // 1月先の最初の土曜日の日付を送ってる Date型情報を渡します
                    startActivity(intent);
                    // こっちは終了させない自分自身のアクティビティは  これはうまくいくOK
                // いや、終わらす
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        //  今月の表示に戻る MainActivityに戻る  自分自身が所属するアクティビティを終了させます
         currentMonthButton = view.findViewById(R.id.currentMonthButton);
        currentMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(parentActivity, MainActivity.class);
                startActivity(intent);
                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });



        // マニフェストファイルによって、activity_month_calendar.xmlとの紐付けができてるので、
        // idとかもこのまま使える メインレイアウトと同じだけど問題なく使える
        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上

        // グリッド状にカードを配置する 7つづつ
      //   GridLayoutManager manager = new GridLayoutManager(this, SPAN_COUNT);
        GridLayoutManager manager = new GridLayoutManager(parentActivity, SPAN_COUNT);
        rv.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new CalendarAdapter(data);
        rv.setAdapter(adapter);

        return view;

    }

    @Override
    public void onDestroy() {
        _helper.close();  // フラグメントの消滅の前に DBヘルパーオブジェクトの解放をすること
        super.onDestroy();
    }
}
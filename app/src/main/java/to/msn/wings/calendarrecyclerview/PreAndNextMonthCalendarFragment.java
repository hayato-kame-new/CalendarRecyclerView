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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PreAndNextMonthCalendarFragment extends Fragment {


    private TextView titleText;
    private Button prevButton, nextButton, currentMonthButton;
    private CalendarAdapter calendarAdapter;
    private RecyclerView recyclerView;
    DateManager dateManager;

    private int SPAN_COUNT = 7;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       //  return inflater.inflate(R.layout.fragment_pre_and_next_month_calendar, container, false);

        Activity parentActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_pre_and_next_month_calendar, container, false);

        Intent intent = parentActivity.getIntent();

        Date prevButtonDate = null;
        Date nextButtonDate = null;
        Bundle extras = intent.getExtras();
        if(extras != null) {
            //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
             prevButtonDate = (Date)intent.getSerializableExtra("prevButtonDate");  // nullが入ってるかもしれないです
            //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか どっちかは nullになるので
             nextButtonDate = (Date)intent.getSerializableExtra("nextButtonDate");  // nullが入ってるかもしれないです
        }

        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
   //     Date prevButtonDate = (Date)intent.getSerializableExtra("prevButtonDate");
        //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか どっちかは nullになるので
    //     Date nextButtonDate = (Date)intent.getSerializableExtra("nextButtonDate");

        dateManager = new DateManager();


        List<Date> dates = null;


        titleText = view.findViewById(R.id.titleText);
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

        // 最初の土曜日は、その月に必ずなってるから
        Date firstSaturdayDate = dates.get(6);


        // 表示してる月よりも１つ前の月を表示するためのボタン
        prevButton = view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivityで今、表示をしている月の情報を取得する MainActivityでは、初期の画面 今月のカレンダーを表示するので
                //  アクティビティを新たに生成し、
                //  新しいアクティビティにMainActivityの firstSaturdayDateの情報から、１ヶ月前にした情報を渡す
                // Date型の計算を行いたい場合には、Calendar型に一度変換し、計算を行います。
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstSaturdayDate);
                calendar.add(Calendar.MONTH, -1);  // -1 をして ひと月前に
                Date date = new Date();
                // これで1月前の最初の土曜日の日付が取得できている
                date = calendar.getTime();

              //  Intent intent = new Intent(PreAndNextMonthCalendarActivity.this, PreAndNextMonthCalendarActivity.class);
                Intent intent = new Intent(parentActivity, PreAndNextMonthCalendarActivity.class);

                intent.putExtra("prevButtonDate", date);  // 1月前の最初の土曜日の日付を送る Date型情報を渡します
                startActivity(intent);


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

              //   Intent intent = new Intent(PreAndNextMonthCalendarActivity.this, PreAndNextMonthCalendarActivity.class);
                Intent intent = new Intent(parentActivity, PreAndNextMonthCalendarActivity.class);

                intent.putExtra("nextButtonDate", date);  // 1月先の最初の土曜日の日付を送ってる Date型情報を渡します
                startActivity(intent);


            }
        });

        //  今月の表示に戻る MainActivityに戻る  自分自身が所属するアクティビティを終了させます
        currentMonthButton = view.findViewById(R.id.currentMonthButton);
        currentMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivityで今、表示をしている月の情報を取得する MainActivityでは、初期の画面 今月のカレンダーを表示するので
                //  アクティビティを新たに生成し、
                //  新しいアクティビティにMainActivityの firstSaturdayDateの情報から、１ヶ月前にした情報を渡す
                // Date型の計算を行いたい場合には、Calendar型に一度変換し、計算を行います。
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(firstSaturdayDate);
//                calendar.add(Calendar.MONTH, -1);  // -1 をして ひと月前に
//                Date date = new Date();
//                // これで1月前の最初の土曜日の日付が取得できている
//                date = calendar.getTime();

              //  Intent intent = new Intent(PreAndNextMonthCalendarActivity.this, MainActivity.class);
                Intent intent = new Intent(parentActivity, MainActivity.class);

                //  intent.putExtra("prevButtonDate", date);  // 1月前の最初の土曜日の日付を送る Date型情報を渡します
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
}
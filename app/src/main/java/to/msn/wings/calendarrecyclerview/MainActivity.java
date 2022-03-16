package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * メインアクティビティの上にCurrentMonthFragmentが乗っています そこで、データベースにも接続してデータ取得します
 */
public class MainActivity extends AppCompatActivity {

//    private TextView titleText;
//    private Button prevButton, nextButton;
//    private CalendarAdapter calendarAdapter;
//    private RecyclerView recyclerView;
//    DateManager dateManager;
//
//    private int SPAN_COUNT = 7;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TimeScheduleDatabaseHelper helper = new TimeScheduleDatabaseHelper(this);

        // データベースを取得する
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            Toast.makeText(this, "接続しました", Toast.LENGTH_SHORT).show();
            // ここにデータベースの処理を書く

        }

//
//        dateManager = new DateManager();
//        // getDays()で、現在の月の日にちオブジェクトを取得する 今月だと 引数なしのgetDays()　を呼び出す
//        // 今月を取得する
//        List<Date> dates =  dateManager.getDays();
//        // countいらない？？？
//       //  int count = dates.size();
//
//
//        // タイトルのテキストを設定する
//        titleText = findViewById(R.id.titleText);
//        // 最初の土曜日は、その月に必ずなってるから
//        Date firstSaturdayDate = dates.get(6);
//        SimpleDateFormat format = new SimpleDateFormat("今月のカレンダー yyyy年 MM月");
//        String title = format.format(firstSaturdayDate);
//        titleText.setText(title);
//
//        // フォーマットし直し
//         format = new SimpleDateFormat("d");  // "dd" だと　　01  02 となってしまう
//
//        // 本日
//        Calendar calendar = Calendar.getInstance();
//        Date dateObjToday = calendar.getTime();
//        /**
//         * 表示だけのテキストのリスト
//         */
//        ArrayList<CalendarCellItem> data = new ArrayList<>();
//        for (int i = 0; i < dates.size(); i++) {
//            CalendarCellItem item = new CalendarCellItem();
//            // item.setId((new Random()).nextLong());
//
//            item.setId(i + 1);  // 1から順に通し番号をふる
//            Date date = dates.get(i);
//
//            String display = format.format(date);
//
//             item.setDateText(display);  // セットします
//
//            // Dateクラスでは、compareToメソッドを使って日付の比較をします  compareToメソッドの戻り値は、
//            // メソッドの呼び出し元の値が、引数と等しい場合は0
//            // メソッドの呼び出し元の値が、引数より前の場合は-1
//            // メソッドの呼び出し元の値が、引数より後の場合は1
//            // 0 か 1 か　-1 を返すので if 使う時に 0だったらというふうに書くので 注意
//            if (date.compareTo(dateObjToday) == 0) {
//                item.setTextViewToday("●");
//            }
//
//             data.add(item);
//        }
//
//
//
//
//        // 表示してる月よりも１つ前の月を表示するためのボタン
//        prevButton = findViewById(R.id.prevButton);
//        prevButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // MainActivityで今、表示をしている月の情報を取得する MainActivityでは、初期の画面 今月のカレンダーを表示するので
//               //  アクティビティを新たに生成し、
//                //  新しいアクティビティにMainActivityの firstSaturdayDateの情報から、１ヶ月前にした情報を渡す
//                // Date型の計算を行いたい場合には、Calendar型に一度変換し、計算を行います。
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(firstSaturdayDate);
//                calendar.add(Calendar.MONTH, -1);  // -1 をして ひと月前に
//                Date date = new Date();
//                // これで1月前の最初の土曜日の日付が取得できている
//                date = calendar.getTime();
//
//                Intent intent = new Intent(MainActivity.this, PreAndNextMonthCalendarActivity.class);
//
//                intent.putExtra("prevButtonDate", date);  // 1月前の最初の土曜日の日付を送る Date型情報を渡します
//                startActivity(intent);
//

//
//            }
//        });
//
//        // 次の月を表示するためのボタン
//        nextButton = findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(firstSaturdayDate);
//                calendar.add(Calendar.MONTH, 1);  // +1 してる ひと月先に
//                Date date = new Date();
//                // これで1月先の最初の土曜日の日付が取得できている
//                date = calendar.getTime();
//
//                Intent intent = new Intent(MainActivity.this, PreAndNextMonthCalendarActivity.class);
//
//                intent.putExtra("nextButtonDate", date);  // 1月先の最初の土曜日の日付を送ってる Date型情報を渡します
//                startActivity(intent);
//
//
//            }
//        });
//
//
//
//        RecyclerView rv = findViewById(R.id.rv);
//        rv.setHasFixedSize(true);  // パフォーマンス向上
//
//        // グリッド状にカードを配置する 7つづつ
//        GridLayoutManager manager = new GridLayoutManager(this, SPAN_COUNT);
//        rv.setLayoutManager(manager);
//
//        RecyclerView.Adapter adapter = new CalendarAdapter(data);
//        rv.setAdapter(adapter);
    }
}
package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * このフラグメントで、個々のタイムスケジュールのCardViewを表示させる  fragment_time_schedule.xml の
 */
public class TimeScheduleFragment extends Fragment {

    private Button addButton, returnMonButton, currentMonButton;
    TextView day, day_today;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_schedule, container, false);

        Activity parentActivity = getActivity();
        // 遷移してきたので、遷移先から、データを取得する
        Intent intent = parentActivity.getIntent();  // CalendarAdapterクラスのリスナーで画面遷移するように実装してる

        Bundle extras = intent.getExtras();
        String scheduleDayText ="";
        String todayString = "";
        Date date = null;  // 文字列から　Date型へ変換する  リスナーの匿名クラス(無名クラス インナークラス)で使用するので 後で、finalをつけて定数にする

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

        currentMonButton = view.findViewById(R.id.currentMonButton);

        day = view.findViewById(R.id.titleText); // fragment_time_schedule.xmlの idが titleTextのもの
        day.setText(scheduleDayText);


        day_today = view.findViewById(R.id.day_today);
        if (!todayString.equals("")) {
            todayString = "今日の予定 " + todayString + " ";
        }
        day_today.setText(todayString);

        returnMonButton = view.findViewById(R.id.returnMonButton);
        int year = Integer.parseInt(scheduleDayText.substring(0, 4));
        int month = Integer.parseInt(scheduleDayText.substring(5, 7));
      returnMonButton.setText(year + "年" + month + "月のカレンダーへ戻る");

      // 本日
        LocalDate localdateToday = LocalDate.now();
// returnMonButton は、今月ならば 非表示にしています
      if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
          returnMonButton.setVisibility(View.GONE); // これで表示しない なおかつ 非表示にしたスペースを詰める
      }



      // 表示してる月のカレンダーへ戻るボタンにリスナーをつける  今月なら、このボタンは非表示になっております
        returnMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 画面遷移する
                // フラグメントを乗せてるサブのアクティビティを終わらせてください

            }
        });

      // 今月のカレンダーへ戻ります
        currentMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 画面遷移する
                //  今月の表示に戻る MainActivityに戻る  自分自身が所属するアクティビティを終了させます
                Intent intent = new Intent(parentActivity, MainActivity.class);
                startActivity(intent);
                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });


        // タイムスケジュールを新規登録するボタンにリスナーをつける
        addButton = view.findViewById(R.id.addButton);
        final Date FINALDATE = date;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 小さいスマホサイズなら、画面遷移あり 新しいアクティビティへ画面遷移する その上のフラグメント切り替えるようにして
                // 新規フラグメントや編集フラグメントのフォームを作る

                // タブレットサイズなら、画面遷移なし fragment_time_schedule.xml　の　RecyclerView　の親に、LinearLayoutにして、
                // 左にCardView  右に新規や編集のフォームを作る

                Intent intent = new Intent(parentActivity, ScheduleFormActivity.class); // 新しくintentオブジェクトを作る

                intent.putExtra("date", FINALDATE);  // 日付を送ってる Date型情報を渡します インナークラスで使うので finalにしてる
                startActivity(intent);

                // 小さいスマホサイズなら、画面遷移ありなので 現在のフラグメントを乗せてるサブのアクティビティを終わらせてください
                // 小さいスマホサイズなら 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();


            }
        });


        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上


// タブレットサイズならば　
// GridLyoutManager manager = new GridLayoutManager(parentActivity, 2);  // にするように後でする
        LinearLayoutManager manager = new LinearLayoutManager(parentActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
//        // dataはデータベースから取得してくる
//        RecyclerView.Adapter adapter = new CalendarAdapter(data);
//        rv.setAdapter(adapter);


        // 最後にreturn viewをすること
        return view;

    }
}
package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * このフラグメントで、RecyclerViewを表示する
 */
public class TimeScheduleFragment extends Fragment {
    // 大画面かどうかの判定フラグ インスタンスフィールド onViewStateRestoredコールバックメソッドをオーバーライドする
    private boolean _isLayoutXLarge = true;  // ここでは 初期値は trueにしておく

    private TimeScheduleDatabaseHelper _helper;
    private Button _addButton, _returnMonButton, _currentMonButton;
    private TextView _day, _day_today;

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
        Date date = null;  // 文字列から　Date型へ変換するため  リスナーの匿名クラス(無名クラス インナークラス)で使用するので 後で、finalをつけて定数にする

        if(extras != null) {
            scheduleDayText = intent.getStringExtra("scheduleDayText");
            todayString = intent.getStringExtra("todayString");
            if (scheduleDayText != null && scheduleDayText != "") {
                try {
                    date = new SimpleDateFormat("yyyy/MM/dd").parse(scheduleDayText); // 文字列から　Date型へ変換する

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        // インナークラスで使うために final で定数に
        final Date DATE = date;
        _currentMonButton = view.findViewById(R.id.currentMonButton);
        _returnMonButton = view.findViewById(R.id.returnMonButton);
        _day = view.findViewById(R.id.titleText);
        _day.setText(scheduleDayText);

        _day_today = view.findViewById(R.id.day_today);
        if (!todayString.equals("")) {
            todayString = "今日の予定 " + todayString + " ";
        }
        _day_today.setText(todayString);

        int year = Integer.parseInt(scheduleDayText.substring(0, 4));
        int month = Integer.parseInt(scheduleDayText.substring(5, 7));
        _returnMonButton.setText(year + "年" + month + "月カレンダーへ戻る");

        // 現在を取得して 比較する
        LocalDate localdateToday = LocalDate.now();
        // returnMonButton は、今月ならば 非表示にしています
        if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
            _returnMonButton.setVisibility(View.GONE); // これで表示しない なおかつ 非表示にしたスペースを詰める
        }

        List<Schedule> list = new ArrayList<Schedule>();

        _helper = new TimeScheduleDatabaseHelper(parentActivity);  // _helper.close();  して解放すること
        //  データベースを取得する try-catch-resources構文なので finallyを書かなくても必ず 自動でclose()処理をしてくれる
        try (SQLiteDatabase db = _helper.getWritableDatabase()) {  // SQLiteDatabase dbは きちんとクローズ自動でしてくれます
            // SELECT文 指定の日のだけを 開始時間の順番にして取得する   SELECT * FROM テーブル名 WHERE scheduledate >= '2011-08-20' AND scheduledate < '2011-08-21' ORDER BY カラム名 ASC もしくは DESC
            //  SELECT * FROM テーブル名 WHERE date BETWEEN '2011-08-20' AND '2011-08-27'          BETWEEN  AND でもいいが 1日だけ取得したい場合では使えない ANDは、その日を含んでしまうので
            // scheduleDayText  DATE を使う
            // Date型の日付を加算するには、Calendarクラスに変換後、Calendarクラスのaddメソッドを使用します。
            // Calendarクラスのインスタンスを生成  1日後を取得したいため
            Calendar cal = Calendar.getInstance();
            cal.setTime(DATE);
            cal.add(Calendar.DATE, 1);  // 1日後
            // 1日後を文字列で取得する  "2000/09/09" ではダメなので "2000-09-09" の形にする
            String next = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            // 指定の日を  フォーマットし直す  "2000/09/09" ではダメなので "2000-09-09" の形にする
            String dd = new SimpleDateFormat("yyyy-MM-dd").format(DATE);
            // 注意 指定した日が1日だけでも、このように >= 　< 　を使って期間で指定をする 2006/09/13のデータが欲しいとき Where scheduledate = '2006/09/13' ではなく
            // Where scheduledate >= '2006/09/13' And scheduledate < '2006/09/14'
            String sqlSelect = "SELECT * FROM timeschedule WHERE scheduledate >= ? AND scheduledate < ? ORDER BY starttime ASC";

            String[] params = new String[]{dd, next};

            Cursor cursor = db.rawQuery(sqlSelect, params);  // 第二引数は、配列にすること

            int _id = 0;
            String scheduledate = "";
            String starttime = "";
            String endtime = "";
            String scheduletitle = "";
            String schedulememo = "";

            Schedule schedule;

            while ( cursor.moveToNext()) {
                // SELECT分によって、インデックスは変わってくるので getColumnIndexで、インデックスを取得します
                int index__id = cursor.getColumnIndex("_id");
                int index_scheduledate = cursor.getColumnIndex("scheduledate");  // 引数には カラム名を指定してください
                int index_starttime = cursor.getColumnIndex("starttime");  // 引数には カラム名を指定してください
                int index_endtime = cursor.getColumnIndex("endtime");  // 引数には カラム名を指定してください
                int index_scheduletitle = cursor.getColumnIndex("scheduletitle");  // 引数には カラム名を指定してください
                int index_schedulememo = cursor.getColumnIndex("schedulememo");  // 引数には カラム名を指定してください

                // カラムのインデックスを元に、　実際のデータを取得する
                _id = cursor.getInt(index__id);
                scheduledate = cursor.getString(index_scheduledate);
                starttime = cursor.getString(index_starttime);
                endtime = cursor.getString(index_endtime);
                scheduletitle = cursor.getString(index_scheduletitle);
                schedulememo = cursor.getString(index_schedulememo);

                // インスタンス生成
                schedule = new Schedule(_id, scheduledate,  starttime, endtime, scheduletitle, schedulememo);
                list.add(schedule);
            }
        }
        _helper.close();  // ヘルパーを解放する

        /**
         * 表示だけのテキストのリスト データベースから取得したlistを使用
         */
        ArrayList<TimeScheduleListItem> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TimeScheduleListItem item = new TimeScheduleListItem();
            // これを変更
          //   item.setId(i + 1);  // 1から順に通し番号をふる
            item.setDate(scheduleDayText);

            Schedule schedule = list.get(i);

            item.setStartTime(schedule.getStarttime());
            item.setEndTime(schedule.getEndtime());
            item.setScheduleTitle(schedule.getScheduletitle());
            item.setScheduleMemo(schedule.getSchedulememo());
            // 主キーのデータを文字列にしてセット
            item.setId(schedule.get_id());  // これはアダプタで非表示にしています
            data.add(item);
        }


        // 表示してる月のカレンダーへ戻るボタンにリスナーをつける  今月なら、このボタンは非表示になっております
        _returnMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 画面遷移する
                // インナークラスなので 定数 DATEを使う
                Intent intent = new Intent(parentActivity, MonthCalendarActivity.class);
                // 指定した年と月のカレンダーを表示するために Date型情報を渡します
                intent.putExtra("specifyDate", DATE);  //  Date型情報を渡します
                startActivity(intent);

                // 最後に 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        // 今月のカレンダーへ戻ります
        _currentMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 画面遷移する
                //  今月の表示に戻る MainActivityに戻る
                Intent intent = new Intent(parentActivity, MainActivity.class);
                startActivity(intent);
                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });


        // タイムスケジュールを新規登録するボタンにリスナーをつける
        _addButton = view.findViewById(R.id.addButton);
        final Date FINALDATE = date;  // 内部クラスで使うので final  定数にする
        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 大画面の場合 追加
                // 大画面の場合 同じアクティビティ上 の右に　フラグメントを新たに乗せます FrameLayoutにしてあるので、上に乗せられるのです
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", FINALDATE);  // DATE型
                bundle.putString("action", "add");

                if (_isLayoutXLarge) { // 大画面の場合
                    FragmentManager manager = getParentFragmentManager(); // getFragmentManager() 非推奨になった
                    FragmentTransaction transaction = manager.beginTransaction();
                    // フォームのフラグメント生成
                    ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
                    // 引き継ぎデータをセット
                    scheduleFormFragment.setArguments(bundle);
                    // 生成したフラグメントを、
                    // id が　timeScheduleFrame　の　FrameLayoutの上に乗せます (FrameLayoutは上に追加できます)replaceメソッドで置き換えます

                    transaction.replace(R.id.timeScheduleFrame, scheduleFormFragment); // 第一引数の上に 第二引数を乗せて表示する
                    transaction.commit();
                    // 同じアクティビティ上なので、所属するアクティビティを終了させません

                } else {
                    // 通常画面の場合
                    Intent intent = new Intent(parentActivity, ScheduleFormActivity.class); // 新しくintentオブジェクトを作る

                    intent.putExtra("date", FINALDATE);  // 日付を送ってる Date型情報を渡します インナークラスで使うので finalにしてる
                    intent.putExtra("action", "add");  // 新規ということもわかるようにデータを送る キーが "action"  値が String型の "add"

                    startActivity(intent);
                 // 通常画面の場合は、別のアクティビティを起動させて表示するので 自分自身が所属するアクティビティをfinish()で終わらせます
                    Activity parentActivity = getActivity();
                    parentActivity.finish();
                }
            }
        });

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上

        LinearLayoutManager manager = new LinearLayoutManager(parentActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TimeScheduleListAdapter(data);  //  dataはデータベースから取得

        rv.setAdapter(adapter);

        // 最後にreturn viewをすること
        return view;
    }


    /**
     *  onActivityCreated() メソッドは非推奨になりました。 onViewStateRestored に書いてください
     *  ここでViewの状態を復元する
     *   onCreate  onCreateView  onViewCreated 非推奨のonActivityCreated  推奨のonViewStateRestored  の順で呼ばれる
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
        Activity parentActivity = getActivity();  // このフラグメントの自分　が所属するアクティビティを取得する

        // 自分が所属するアクティビティから、 id が　timeScheduleFrame　の　FrameLayoutを取得する  timeScheduleFrame
        View timeScheduleFrame = parentActivity.findViewById(R.id.timeScheduleFrame);
      // この判定は新規ボタンが押される時に使う
        if (timeScheduleFrame == null) {  // nullならば、大画面ではないので
            // 画面判定フラグを通常画面とする
            _isLayoutXLarge = false;
            // 次にTimeScheduleFragmentで、RecycleViewの CardViewのタップ時の処理(編集や削除) と　新規スケジュールボタンのタップ時の処理で
            // 画面サイズによって分岐する処理を書く
        }
    }

    /**
     * アクセッサ  ゲッターメソッド
     * @return true: 大画面である <br /> false: 通常サイズ（スマホサイズ)である
     */
    public boolean is_isLayoutXLarge() {
        return _isLayoutXLarge;
    }
}

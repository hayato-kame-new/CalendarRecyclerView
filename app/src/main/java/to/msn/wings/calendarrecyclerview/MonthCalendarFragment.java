package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendarFragment extends Fragment {

    private TimeScheduleDatabaseHelper _helper;
    private TextView _titleText;
    private Button _prevButton, _nextButton, _currentMonthButton;
    DateManager _dateManager;
    final private int _SPAN_COUNT = 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity parentActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        Intent intent = parentActivity.getIntent();

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
        _dateManager = new DateManager();

        List<Date> dates = null;

        _titleText = view.findViewById(R.id.titleText);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年 MM月");
        // ここで条件分岐します
        String title = "";
        if (prevButtonDate != null) {  //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
            title = format.format(prevButtonDate);
            dates = _dateManager.getDays(prevButtonDate);  // 引数ありのgetDays(Date date)　を呼び出す

        } else if (nextButtonDate != null) {  //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
            title = format.format(nextButtonDate);
            dates = _dateManager.getDays(nextButtonDate); // 引数ありのgetDays(Date date)　を呼び出す
            //  null かどうかのチェックが必要です どっちのボタンから遷移してきたのか  どっちかは nullになるので
        } else if (specifyDate != null) {  // 指定の日付のカレンダーを表示するならば、指定の日付が現在の月ではないなら、このフラグメントで表示します
            title = format.format(specifyDate);
            dates = _dateManager.getDays(specifyDate);  // 引数ありのgetDays(Date date)　を呼び出す
        }
        _titleText.setText(title);

        Date firstDate = dates.get(0);
        Date lastDate = dates.get(dates.size() - 1);
        // SQLのバインドに使う 期間を指定してSELECTするため
        String firstDatestr = new SimpleDateFormat("yyyy-MM-dd").format(firstDate);
        String lastDatestr = new SimpleDateFormat("yyyy-MM-dd").format(lastDate);

        // 今月分のカレンダー(１週目に表示した前の月や　最後の週に表示してある後ろの月　の分も含む)に表示するリスト
        List<Schedule> list = new ArrayList<Schedule>();

        _helper = new TimeScheduleDatabaseHelper(parentActivity);
//  データベースを取得する try-catch-resources構文 finallyを書かなくても必ず close()処理をしてくれます
        try (SQLiteDatabase db = _helper.getWritableDatabase()) {  // dbはきちんとクローズ自動でしてくれます
            Toast.makeText(parentActivity, "接続しました", Toast.LENGTH_SHORT).show();
            // ここにデータベースの処理を書く SELECT文で取得する 今月分のだけを取得する   SELECT * FROM テーブル名 WHERE date >= '2011-08-20' AND date <= '2011-08-27'
            //  SELECT * FROM テーブル名 WHERE date BETWEEN '2011-08-20' AND '2011-08-27'  開始時間の順番にして取得する
            String sqlSelect = "SELECT * FROM timeschedule WHERE scheduledate >= ? AND scheduledate <= ? ORDER BY starttime ASC";

            String[] params = new String[]{firstDatestr, lastDatestr};

            Cursor cursor = db.rawQuery(sqlSelect, params);

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

                Log.i("SQLITE", "_id : " + _id + " " +
                        "scheduledate : " + scheduledate + " " +
                        "starttime : "+ starttime + " " +
                        "endtime : "+ endtime + " " +
                        "scheduletitle : "+ scheduletitle + " " +
                        "schedulememo : "+ schedulememo + " "
                );
                // インスタンス生成
                schedule = new Schedule(_id, scheduledate,  starttime, endtime, scheduletitle, schedulememo);
                list.add(schedule);
            }
        }
        _helper.close();  // ここでヘルパーを解放すること　  SQLiteDatabase db は、try-catch-resources構文だから finallyを書かなくても必ず close()処理をしてくれます

        // リスト取得できた リサイクラービューで このリストを日付が同じならば、セット指定く    2022-03-25 14:00 16:00 しごと かいもの
//        for( Schedule schedule : list) {
//            Log.i("LIST",
//                    schedule.getScheduledate() + " "  + schedule.getStarttime() + " "  +
//                            schedule.getEndtime() + " "  + schedule.getScheduletitle() + " "  + schedule.getSchedulememo());
//        }


        // 表示用のフォーマットし直し
        format = new SimpleDateFormat("d");  // "dd" だと　　01  02 となってしまう
        // もう一つ必要  ループの中で使うためここでインスタンスを生成しておく  表示はしないけど必要
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");  //  2017/03/02
        // 比較をするために本日現在を取得する
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;  // 今日現在の月
        int day = calendar.get(Calendar.DATE);  // 今日現在の日

        /**
         * 表示だけのテキストのリスト
         */
        ArrayList<CalendarCellItem> data = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            CalendarCellItem item = new CalendarCellItem();
            // item.setId((new Random()).nextLong());  // ランダムにしても構わないが

            item.setId(i + 1);  // 1から順に通し番号をふる
            Date date = dates.get(i);
            // Calendarに変換する
            calendar.setTime(date);
            int y = calendar.get(Calendar.YEAR);  // これは後で使う
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DATE);
            // 今日に印をつける
            if ((month == m) && ( day == d)) {
                String str = "●";
                item.setTextViewToday(str);
            }
            String display = format.format(date);
            item.setDateText(display);  // セットします
            String non_display =  sdFormat.format(date); //  2017/03/02 という形
            item.setTextViewGone(non_display);  // アダプターで非表示にしてるけど、日付の情報は送れてる

            // 追加 listの中にある同じ日付けの
            String display_schedules = "";
            for(Schedule schedule : list) {
                String scheduledate = schedule.getScheduledate();  // "2022-03-25"

                if ( y == Integer.parseInt(scheduledate.substring(0, 4))
                        && m ==  Integer.parseInt(scheduledate.substring(5, 7))
                        && d == Integer.parseInt(scheduledate.substring(8))) {
                    // 同じ日付のものが見つかったら セルの中に表示する
                    String ti = schedule.getScheduletitle();
                    if(ti.length() > 3) {  // 注意エラーに
                        ti = schedule.getScheduletitle().substring(0, 4);
                    }
                    display_schedules +=  schedule.getStarttime() + "~ "  +  ti + "\n";
                }
                item.setSchedules(display_schedules);
            }
            data.add(item);
        }

        // 最初の土曜日は、その月に必ずなってるから
        Date firstSaturdayDate = dates.get(6);

        // 表示してる月よりも１つ前の月を表示するためのボタン
        _prevButton = view.findViewById(R.id.prevButton);
        _prevButton.setOnClickListener(new View.OnClickListener() {
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

                // 所属しているアクティビティを finish()で終了させること
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        // 次の月を表示するためのボタン
        _nextButton = view.findViewById(R.id.nextButton);
        _nextButton.setOnClickListener(new View.OnClickListener() {
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

                // 所属しているアクティビティを finish()で終了させること
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        //  今月の表示に戻る MainActivityに戻る  自分自身が所属するアクティビティを終了させます
        _currentMonthButton = view.findViewById(R.id.currentMonthButton);

        _currentMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity, MainActivity.class);
                startActivity(intent);
                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);  // パフォーマンス向上

        // グリッド状にカードを配置する 7つづつ
        GridLayoutManager manager = new GridLayoutManager(parentActivity, _SPAN_COUNT);
        rv.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new CalendarAdapter(data);
        rv.setAdapter(adapter);

        return view;
    }
}
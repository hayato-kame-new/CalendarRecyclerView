package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleFormFragment extends Fragment {


    TextView _formTitle;
    Button _returnMonButton, _currentMonButton;
    Spinner _spinnerStartHour, _spinnerStartMinutes, _spinnerEndHour, _spinnerEndMinutes;
    EditText _editTextScheTitle, _editTextScheMemo;
    CalendarView _calendarView;
    Button _saveButton;  //  import android.widget.Button;

     private TimeScheduleDatabaseHelper helper;
    private SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TimeSheduleActivity (TimeSheduleFragment)　から  所属するアクティビティのScheduleFormActivityへ画面遷移してくる
        // このフラグメントが  所属するアクティビティのScheduleFormActivity の取得
        Activity parentActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_schedule_form, container, false);

       //  所属するアクティビティから インテントを取得する
        Intent intent = parentActivity.getIntent();
        // インテントから、引き継ぎデータをまとめたものを取得
        Bundle extras = intent.getExtras();

        // 個々のデータを取得 うまく取得できなかった時のために String型は ""で初期化  Date型は nullで初期化
        final Date[] date = {null};
        String action = "";
        if (extras != null) {
            date[0] = (Date)extras.getSerializable("date");  // Date型へキャストが必要です
            action = extras.getString("action");
        }
        // 後でインナークラスで dateを使うので定数にしておく final つける
        final Date DATE = date[0];

        _formTitle = view.findViewById(R.id.formTitle);
        // もし、新規登録ボタンをクリックしてきたら、新規であることをintentでデータで送ってきた action の値によって分岐できるようにする
        if (action.equals("add")) {
            _formTitle.setText(R.string.tvFormTitleAdd);  // 新規の時に　新規スケジュール登録画面　と表示する
        }

        // Date型の getYear getMonth getDay　は　非推奨メソッドなので、SimpleDateFormatを使い、文字列として取得する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月");  // MM に　すると 01 02 03   M にすると 1  2  3
        String str = sdf.format(date[0]);  // ボタンに表示するための
        _returnMonButton = view.findViewById(R.id.returnMonButton);
        _returnMonButton.setText(str + "のカレンダーに戻る");
        // 比較するために フォーマットし直して
         sdf = new SimpleDateFormat("yyyy年MM月"); // MM に　すると 01 02 03
        String strMM = sdf.format(date[0]);
         int year = Integer.parseInt(strMM.substring(0, 4));
         int month = Integer.parseInt(strMM.substring(5, 7));
        // もし、今月ならば returnMonButtonを非表示にする
        // 現在を取得して
        LocalDate localdateToday = LocalDate.now();
        if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {

            _returnMonButton.setVisibility(View.GONE); // これで表示しない なおかつ 非表示にしたスペースを詰める
        }

        _spinnerStartHour = view.findViewById(R.id.spinnerStartHour);
        _spinnerStartMinutes = view.findViewById(R.id.spinnerStartMinutes);
        _spinnerEndHour = view.findViewById(R.id.spinnerEndHour);
        _spinnerEndMinutes = view.findViewById(R.id.spinnerEndMinutes);
        _editTextScheTitle = view.findViewById(R.id.editTextScheTitle);
        _editTextScheMemo = view.findViewById(R.id.editTextScheMemo);
        _saveButton = view.findViewById(R.id.saveButton);


        // スピナー３つは android:entries="@array/spinnerStartHour"を書く スピナー1つは動的にリストを作る


        // スピナーの _spinnerEndMinutes には　動的にリストを作る これだけは  内部クラスの中で、adapterから行を一つ削除するため 動的に作る
        List<String> endMList = new ArrayList<>();
        endMList.add("選択しない");
        endMList.add("00");
        endMList.add("30");
        ArrayAdapter<String> adapterEM = new ArrayAdapter<>(parentActivity, android.R.layout.simple_spinner_item, endMList);
        adapterEM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinnerEndMinutes.setAdapter(adapterEM);





        // 遷移してくる前に表示していた　カレンダーの年と月に戻るために、
        _returnMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        _currentMonButton = view.findViewById(R.id.currentMonButton);
        //  現在(今月)のカレンダーの表示へ遷移する MainActivityに戻る  自分自身が所属するアクティビティを終了させます
        _currentMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity, MainActivity.class);

                startActivity(intent);

                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();

            }
        });

        // CalendarViewに日時を設定します。
        _calendarView = view.findViewById(R.id.calendarView);
        _calendarView.setDate(DATE.getTime());  // 引数には long型
        long setL = _calendarView.getDate();

        java.sql.Date setDaySql = new java.sql.Date(setL);
        // データベースへ登録するための フィールド 内部クラスで使うから final にしておく 初期値は、遷移してきた時に選択してあった日付にしておくので
        final java.sql.Date[] sqlDateArray = {setDaySql};  // 配列の中身なら書き換え可能だから 配列にする
        // new 以降は　無名クラス 匿名クラスなので　　その中で使うなら　定数にするのでDATEを使う
        _calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {  //  CalendarViewで日にちが選択された時に呼び出されるリスナークラス
            @Override   // 注意  引数の　monthは　The month that was set [0-11]
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // データベースへ新規登録するためにデータを取得します

                Calendar c = Calendar.getInstance();  // 現在
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month ); // 月は0始まりだが、引数の monthも0始まりなので同じにして大丈夫です

                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                java.util.Date utilDate = c.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                sqlDateArray[0] = sqlDate;
                Toast.makeText(view.getContext(), sqlDateArray[0].toString(), Toast.LENGTH_LONG).show();

            }
        });


// データベースへ登録するための フィールド 内部クラスで使うから final にしておく
        // 開始時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] START_HOUR_STR_ARRAY = {""};    // データベースに入れるために 後で int型にする

        _spinnerStartHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "データベースに登録する日付は " + sqlDateArray[0].toString(), Toast.LENGTH_SHORT).show();

                START_HOUR_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
                Toast.makeText(getActivity(), "あなたが選んだ開始時間は " + START_HOUR_STR_ARRAY[0] + " 時です", Toast.LENGTH_SHORT).show();
            }


            /**
             * onNothingSelectedメソッドは既に選択された項目をクリックした時に呼び出され、
             * その後onItemSelectedメソッドが呼び出されます。
             * @param adapterView
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 開始の分を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] START_MINUTES_STR_ARRAY = {""};    // データベースに入れるために 後で int型にする

        _spinnerStartMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                START_MINUTES_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
                Toast.makeText(getActivity(), "あなたが選んだ開始 分は " + START_MINUTES_STR_ARRAY[0] + " 分です", Toast.LENGTH_SHORT).show();
            }

            /**
             * onNothingSelectedメソッドは既に選択された項目をクリックした時に呼び出され、
             * その後onItemSelectedメソッドが呼び出されます。
             * @param adapterView
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 選択しない もある NOT NULL　制約をつけない
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] END_HOUR_STR_ARRAY = {""};    // データベースに入れるために 後で int型にする

        _spinnerEndHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if ( i == 0) {
                    // ポジションが 0番目のアイテム(つまり、選択しない)を 選択している時には、下の、終了の分を 見えなくする View.INVISIBLEにする
                    // SpinnerはView.GONEにしてしまうとonItemSelectedイベントが発動しないので注意
                    _spinnerEndMinutes.setVisibility(View.INVISIBLE);  // 分を 入力させないため 場所はそのまま確保したままで 見えなくする

                    _spinnerEndMinutes.setSelection(0);  // 終了の時間が "選択しない”なので  終了の 分も "選択しない" にしておく
               //      Object spinnerEndMinutesObject = _spinnerEndMinutes.getSelectedItem();
                //     Object obj = spinnerEndMinutesObject;
                } else {
                    _spinnerEndMinutes.setVisibility(View.VISIBLE);  // "00" か "30" ならば

                    // 終了の分のアダプターから "選択しない" を削除しておき  終了の分の選択済みを "00"にしておく
                    adapterEM.remove("選択しない");
                    _spinnerEndMinutes.setSelection(0);  // "00" にしておく
                }
                END_HOUR_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
                Toast.makeText(getActivity(), "あなたが選んだ終了時間 は " + END_HOUR_STR_ARRAY[0] + " 分です", Toast.LENGTH_SHORT).show();


            }

            /**
             * onNothingSelectedメソッドは既に選択された項目をクリックした時に呼び出され、
             * その後onItemSelectedメソッドが呼び出されます。
             * @param adapterView
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // _spinnerEndMinutes には　動的にリストを作っています 終了時間の内部クラスのリスナーの中でアイテムを削除しているから
        // 選択しない もある NOT NULL　制約をつけない
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] END_MINUTES_STR_ARRAY = {""};    // データベースに入れるために 後で int型にする

        _spinnerEndMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // データベースに登録するため 情報を取得する
                END_MINUTES_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
                Toast.makeText(getActivity(), "あなたが選んだ終了 分 は " + END_MINUTES_STR_ARRAY[0] + " 分です", Toast.LENGTH_SHORT).show();
            }

            /**
             * onNothingSelectedメソッドは既に選択された項目をクリックした時に呼び出され、
             * その後onItemSelectedメソッドが呼び出されます。
             * @param adapterView
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 保存ボタンないで、登録したらば、所属するアクティビティを終了させ、その月のカレンダーを表示させて、トーストを表示して遷移して終わり


        _saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // _id　は　主キーで autoincrement をつけないけど自動採番するらしい insetの時に書かない 自動採番する
                // SQLite3では、「CREATE TABLE」の際に「AUTO INCREMENT」を指定する必要はありません。つけない方がいいらしい
                //もし主キーを連番のIDにしたい場合、INTEGERで「PRIMARY KEY」を指定するようにします。

                // まず、入力チェック タイトルは ""空文字じゃだめ

                Date date = sqlDateArray[0];  // "2022-03-19"
                String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                String  sh = START_HOUR_STR_ARRAY[0];  // "0"
                String paddingStr = sh.format("%2s", START_HOUR_STR_ARRAY[0]).replace(" ", "0");
                String sm = START_MINUTES_STR_ARRAY[0]; // "00"
                String eh = END_HOUR_STR_ARRAY[0]; // "選択しない"
                String em = END_MINUTES_STR_ARRAY[0]; // "選択しない"
                Toast.makeText(getActivity(),  " 日にちが" + sqlDateArray[0] + " 開始時間が" + START_HOUR_STR_ARRAY[0] + ":" + START_MINUTES_STR_ARRAY[0] + "です 終了時間は　" + END_HOUR_STR_ARRAY[0] + ":" + END_MINUTES_STR_ARRAY[0] + " です" , Toast.LENGTH_LONG).show();


                String insertST = paddingStr + ":" + sm;
                String insertET = "";
                if (!END_HOUR_STR_ARRAY[0].equals("選択しない")) {
                    insertET = eh + ":" + em;
                }


                // Title は　何か書いてもらわないとだめなので、保存ボタンクリックした時にチェックする
                 String etTitle = _editTextScheTitle.getText().toString();  // 何も書いてないと ""空文字になってる
                 String etMemo = _editTextScheMemo.getText().toString(); // 何も書いてないと ""空文字になってる
                Toast.makeText(getActivity(),  "タイトルは　" + etTitle + " です　メモは　" + etMemo + " です　", Toast.LENGTH_LONG).show();

// データベースを取得する try-catch-resources構文にすること finallyを書かなくても必ず close()処理をしてくれます！！
                db = MainActivity.helper.getWritableDatabase();

                String sqlInsert = "INSERT INTO timeschedule (scheduledate, starttime, endtime, scheduletitle, schedulememo) VALUES (?,?,?,?,?)";


                SQLiteStatement stmt = db.compileStatement(sqlInsert);
                stmt.bindString(4, etTitle);
                stmt.bindString(5, etMemo);
                 stmt.bindString(1, strDate);
                stmt.bindString(2, insertST);
                stmt.bindString(3, insertET);


                stmt.executeInsert();


                Toast.makeText(getActivity(),  "スケジュールを新規登録しました", Toast.LENGTH_LONG).show();

                // スケジュールを挿入した年月が、現在の年月なら MainActivityへ　それ以外の月ならMonthCalendarActivityへ遷移する "2022-03-19"
                int year = Integer.parseInt(strDate.substring(0, 4));
                int month = Integer.parseInt(strDate.substring(5, 7));
                // 現在を取得して
                LocalDate localdateToday = LocalDate.now();
                Intent intent = null;
                if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
                   //  returnMonButton.setVisibility(View.GONE); // これで表示しない なおかつ 非表示にしたスペースを詰める
                     intent = new Intent(parentActivity, MainActivity.class);
                } else {
                     intent = new Intent(parentActivity, MonthCalendarActivity.class);
                    intent.putExtra("specifyDate", DATE);  //  Date型情報を渡します
                }

               //  Intent intent = new Intent(parentActivity, MonthCalendarActivity.class);
                // 指定した年と月のカレンダーを表示するために Date型情報を渡します
                // intent.putExtra("specifyDate", DATE);  //  Date型情報を渡します
                startActivity(intent);

                // 最後に 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();


            }
        });


        // 最後ビューをreturnする
        return view;
    }
}
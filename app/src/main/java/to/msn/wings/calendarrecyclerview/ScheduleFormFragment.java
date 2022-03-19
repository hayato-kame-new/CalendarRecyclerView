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

        // Title は　何か書いてもらわないとだめなので、保存ボタンクリックした時にチェックする
        String etTitle = _editTextScheTitle.getText().toString();  // 何も書いてないと ""空文字になってる
        String etMemo = _editTextScheMemo.getText().toString(); // 何も書いてないと ""空文字になってる


        // 取得した日付 sqlDateArray[0]
        java.sql.Date insertDate = sqlDateArray[0];
        // SQLiteならば テキストにして "2022-03-18" にしてデータベースに保存する
        String insertDateStr = new SimpleDateFormat("yyyy-MM-dd").format(insertDate);

        String insertStartTime = "";
        //  SQLiteならばテキストにして保存するので
        if (!(START_HOUR_STR_ARRAY[0].equals("") || START_MINUTES_STR_ARRAY[0].equals(""))) {  // フラグメント作成時の時""だから
            String paddingStr = str.format("%2s", START_HOUR_STR_ARRAY[0]).replace(" ", "0");

            insertStartTime = paddingStr + ":" + START_MINUTES_STR_ARRAY[0];
        }

        String insertEndTime = "";
        if ( !END_HOUR_STR_ARRAY[0].equals("選択しない") && !END_HOUR_STR_ARRAY[0].equals(""))  {
            insertEndTime = END_HOUR_STR_ARRAY[0] + END_HOUR_STR_ARRAY[0];
        }


//        Integer startH = null;
//        Integer startM = null;
//        // 取得した 文字列の時間をintへ変換する  フラグメント立ち上げの時には START_HOUR_STR_ARRAY[0] には ""空文字が入ってるのでエラー
//        if (!(START_HOUR_STR_ARRAY[0].equals("") || START_MINUTES_STR_ARRAY[0].equals(""))) {
//
//             startH = Integer.parseInt(START_HOUR_STR_ARRAY[0]);
//             startM = Integer.parseInt(START_MINUTES_STR_ARRAY[0]);
//        }
//        // 終了は "選択しない"　が入ってる時もある 終了時間が ”選択しない” ならば 終了分も "選択しない" になっているはず 終了は null許可するから
//        Integer endH = null;
//        Integer endM = null;
//        if ( !END_HOUR_STR_ARRAY[0].equals("選択しない") && !END_HOUR_STR_ARRAY[0].equals("")) {
//            // "00" か　"30" が選択されてるならば
//            endH = Integer.parseInt(END_HOUR_STR_ARRAY[0]);
//            endM = Integer.parseInt(END_HOUR_STR_ARRAY[0]);
//        }
//
//        LocalTime starttime = null;
//        LocalTime endtime = null;
//        if (startH != null && startM != null  && endH != null && endM != null) {
//
//            starttime = LocalTime.of(startH, startM);  // 選択しないなら nullなのでここで落ちる
//             endtime = LocalTime.of(endH, endM);
//        }
      // SQLiteならばテキストにして保存する



        // 保存ボタンにリスナーをつけて　ボタンを押した時に、データベースに登録します あり得ないが入力されたときにはToastを表示させたい。
        //  スタート時間が、終了時間よりも後ろだった場合に再入力を促します 入力チェックをします 保存ボタンを押した時に入力チェックを行う
        // "CREATE TABLE timeschedule (" + "_id INTEGER PRIMARY KEY , scheduledate DATE NOT NULL," +
        //                    " starttime DATETIME NOT NULL, endtime DATETIME , scheduletitle TEXT NOT NULL, schedulememo TEXT)"

        // 保存ボタンないで、登録したらば、所属するアクティビティを終了させ、その月のカレンダーを表示させて、トーストを表示して遷移して終わり

        String finalInsertStartTime = insertStartTime;
        String finalInsertEndTime = insertEndTime;
        _saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // _id　は　主キーで autoincrement をつけないけど自動採番するらしい insetの時に書かない 自動採番する
                // SQLite3では、「CREATE TABLE」の際に「AUTO INCREMENT」を指定する必要はありません。つけない方がいいらしい
                //もし主キーを連番のIDにしたい場合、INTEGERで「PRIMARY KEY」を指定するようにします。

                // まず、入力チェック タイトルは ""空文字じゃだめ


                helper = new TimeScheduleDatabaseHelper(getActivity());  // onDestroy()で helperを解放すること
                // MainActivityの上に乗せた CurrentMonthFragmentで、既存のデータを表示させるので、SELECT文で取得する
                // データベースを取得する try-catch-resources構文なのでfinallyを書かなくても必ず close()処理をしてくれます！！
                db = helper.getWritableDatabase();



                String sqlInsert = "INSERT INTO timeschedule (scheduledate, starttime, endtime, scheduletitle, schedulememo) VALUES (?,?,?,?)";



                SQLiteStatement stmt = db.compileStatement(sqlInsert);
                stmt.bindString(1, insertDateStr);
                stmt.bindString(2, finalInsertStartTime);
                stmt.bindString(3, finalInsertEndTime);
                stmt.bindString(4, etTitle);
                stmt.bindString(5, etMemo);

                stmt.executeInsert();

                // intent発行して遷移して　所属するアクティビティを終了させます

            }
        });


        // 最後ビューをreturnする
        return view;
    }
}
package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFormFragment extends Fragment {

    TextView _formTitle, _textViewHourError, _textViewMinutesError;
    Button _returnMonButton, _currentMonButton;
    Spinner _spinnerStartHour, _spinnerStartMinutes, _spinnerEndHour, _spinnerEndMinutes;
    EditText _editTextScheTitle, _editTextScheMemo;
    CalendarView _calendarView;
    Button _saveButton;  //  import android.widget.Button;

    // フラグ
    Map<String, Boolean> _buttonFlagMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TimeSheduleActivity (TimeSheduleFragment)　から  所属するアクティビティのScheduleFormActivityへ画面遷移してくる
        // このフラグメントが  所属するアクティビティのScheduleFormActivity の取得
        Activity parentActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_schedule_form, container, false);

// フラグMap  インスタンス生成
        _buttonFlagMap = new HashMap<String, Boolean>();

        _saveButton = view.findViewById(R.id.saveButton);

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
            _saveButton.setEnabled(false);  // 新規なら最初は保存ボタン押せないようになってる  false
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

        _currentMonButton = view.findViewById(R.id.currentMonButton);
        _calendarView = view.findViewById(R.id.calendarView);
        _textViewHourError = view.findViewById(R.id.textViewHourError);
        _textViewMinutesError = view.findViewById(R.id.textViewMinutesError);

        // スピナーの 　動的にリストを作るやりかた
//        List<String> endMList = new ArrayList<>();
//        endMList.add("00");
//        endMList.add("30");
//        ArrayAdapter<String> adapterEM = new ArrayAdapter<>(parentActivity, android.R.layout.simple_spinner_item, endMList);
//        adapterEM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        _spinnerEndMinutes.setAdapter(adapterEM);

        // 新規ではの状態では　　""になってる  編集では必ず入ってる
        if (_editTextScheTitle.getText().toString().isEmpty() || _editTextScheTitle.getText().toString().equals("")) {  // 新規の時最初は　""　空文字が入ってくる
            _editTextScheTitle.setError("スケジュールのタイトルに文字を入力してください");
           // 保存ボタン押せない 新規登録画面では　最初は押せないようになってる ""になってるから
            _buttonFlagMap.put("scheTitle", false );
        } else {  // 編集
            _buttonFlagMap.put("scheTitle", true );  // 編集では、最初は押せるようになってる
        }


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


        // イベントリスナー タイトルのEditTextに何か入力をしたら、保存ボタンが押せるフラグMapに 値をtrueで登録
        _editTextScheTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String word = editable.toString();
                if(!word.equals("")) {
                    _buttonFlagMap.put("scheTitle", true );  // ""空文字じゃなかったら 保存ボタンが押せる
                }
                // もし、Mapの値が全て trueならばボタンを押せるようにする　メソッド化する
                changeSaveButton(_buttonFlagMap);
            }
        });


        // CalendarViewに日時を設定します。
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
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // 本当は変換必要なかった java.util.Date　のままで良かったです
                sqlDateArray[0] = sqlDate;
               // Toast.makeText(view.getContext(), sqlDateArray[0].toString(), Toast.LENGTH_LONG).show();
            }
        });



// データベースへ登録するための フィールド 内部クラスで使うから final にしておく
        // 開始時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] _START_HOUR_STR_ARRAY = {""};
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
         final String[] _END_HOUR_STR_ARRAY = {""};
        // 開始  時間にリスナーつける
        _spinnerStartHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //   Toast.makeText(getActivity(), "データベースに登録する日付は " + sqlDateArray[0].toString(), Toast.LENGTH_SHORT).show();
                String str = (String)adapterView.getItemAtPosition(i);  // 最初は一番上 "0" が選択された状態になってる

                if ( _END_HOUR_STR_ARRAY[0].equals("")) {   // END_HOUR_STR_ARRAY[0] が""空文字じゃなければ　　Integer.parseIntします
                   // 何もしない
                } else if ( Integer.parseInt((String)adapterView.getItemAtPosition(i)) > Integer.parseInt(_END_HOUR_STR_ARRAY[0])) {
                    _textViewHourError.setError("開始時間と終了時間を確認してください");
                    _textViewHourError.setText("開始時間と終了時間を確認してください");
                   // 押せない
                    _buttonFlagMap.put("startHour", false );
                } else {
                    _buttonFlagMap.put("startHour", true );
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
                }
                _START_HOUR_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
             //   Toast.makeText(getActivity(), "あなたが選んだ開始時間は " + START_HOUR_STR_ARRAY[0] + " 時です", Toast.LENGTH_SHORT).show();
                // ここでボタン変更するメソッドをよぶ
                changeSaveButton(_buttonFlagMap);
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

// 終了時間にリスナーつける
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        //  final String[] END_HOUR_STR_ARRAY = {""};
        _spinnerEndHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                if ( i == 0) {
//                    // ポジションが 0番目のアイテム(つまり、選択しない)を 選択している時には、下の、終了の分を 見えなくする View.INVISIBLEにする
//                    // SpinnerはView.GONEにしてしまうとonItemSelectedイベントが発動しないので注意
//                    _spinnerEndMinutes.setVisibility(View.INVISIBLE);  // 分を 入力させないため 場所はそのまま確保したままで 見えなくする
//                    _spinnerEndMinutes.setSelection(0);  // 終了の時間が "選択しない”なので  終了の 分も "選択しない" にしておく
//                } else {
//                    _spinnerEndMinutes.setVisibility(View.VISIBLE);  // 見えるようにする
//                    // 終了の分のアダプターから "選択しない" を削除しておき  終了の分の選択済みを "00"にしておく
//                    adapterEM.remove("選択しない");
//                    _spinnerEndMinutes.setSelection(0);  // "00" にしておく
//                }
                String select = (String)adapterView.getItemAtPosition(i);
                // 入力チェック

                if ( _START_HOUR_STR_ARRAY[0].equals("")) {
                    // 何もしない
                } else if ( Integer.parseInt((String)adapterView.getItemAtPosition(i)) < Integer.parseInt(_START_HOUR_STR_ARRAY[0])) {
                    _textViewHourError.setError("開始時間と終了時間を確認してください");
                    _textViewHourError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endHour", false );
                } else {
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _buttonFlagMap.put("startHour", true );  // 押せる
                }
                _END_HOUR_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
                // ここでボタン変更するメソッドをよぶ
                changeSaveButton(_buttonFlagMap);
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
        final String[] _START_MINUTES_STR_ARRAY = {""};
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] _END_MINUTES_STR_ARRAY = {""};

        // 開始の　分にリスナーつける
        _spinnerStartMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _START_MINUTES_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
             //   Toast.makeText(getActivity(), "あなたが選んだ開始 分は " + START_MINUTES_STR_ARRAY[0] + " 分です", Toast.LENGTH_SHORT).show();

                if ( _END_MINUTES_STR_ARRAY[0].equals("") || _END_HOUR_STR_ARRAY[0].equals("") || _START_HOUR_STR_ARRAY[0].equals("")) {   // _END_MINUTES_STR_ARRAY[0] が""空文字じゃなければ　　Integer.parseIntします
                 //   何もしない
                } else if (( Integer.parseInt(_END_HOUR_STR_ARRAY[0]) == Integer.parseInt(_START_HOUR_STR_ARRAY[0]))  &&  (Integer.parseInt((String)adapterView.getItemAtPosition(i)) > Integer.parseInt(_END_MINUTES_STR_ARRAY[0]))) {
                    _textViewMinutesError.setError("開始時間と終了時間を確認してください");
                    _textViewMinutesError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("startMinutes", false );
                } else {
                    _textViewMinutesError.setError(null);
                    _textViewMinutesError.setText("");
                    _buttonFlagMap.put("startMinutes", true ); //押せる
                }
                // ここでボタン変更するメソッドをよぶ
                changeSaveButton(_buttonFlagMap);
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


        // 終了の分にリスナーをつける
        _spinnerEndMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // データベースに登録するため 情報を取得する
                _END_MINUTES_STR_ARRAY[0] = (String) adapterView.getItemAtPosition(i);  // 選択されたアイテムを　親のアダプタービューから ポジションを指定して取得する
               //  Toast.makeText(getActivity(), "あなたが選んだ終了 分 は " + END_MINUTES_STR_ARRAY[0] + " 分です", Toast.LENGTH_SHORT).show();

                if ( _START_MINUTES_STR_ARRAY[0].equals("") || _END_HOUR_STR_ARRAY[0].equals("") || _START_HOUR_STR_ARRAY[0].equals("")) {
                    //   何もしない
                } else if (( Integer.parseInt(_END_HOUR_STR_ARRAY[0]) == Integer.parseInt(_START_HOUR_STR_ARRAY[0]))  &&  (Integer.parseInt((String)adapterView.getItemAtPosition(i)) < Integer.parseInt(_START_MINUTES_STR_ARRAY[0]))) {
                    _textViewMinutesError.setError("開始時間と終了時間を確認してください");
                    _textViewMinutesError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endMinutes", false );
                } else {
                    _textViewMinutesError.setError(null);
                    _textViewMinutesError.setText("");
                    _buttonFlagMap.put("endMinutes", true ); // 押せる
                }
                // ここでボタン変更するメソッドをよぶ
                changeSaveButton(_buttonFlagMap);
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


        // 保存ボタン
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // _id　は　主キーで autoincrement をつけないけど自動採番するらしい insetの時に書かない 自動採番する
                // SQLite3では、「CREATE TABLE」の際に「AUTO INCREMENT」を指定する必要はありません。つけない方がいいらしい
                //もし主キーを連番のIDにしたい場合、INTEGERで「PRIMARY KEY」を指定するようにします。

                Date date = sqlDateArray[0];  // "2022-03-19"
                String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date); // String型 にしてデータベースへ登録する
                String  sh = _START_HOUR_STR_ARRAY[0];  // "0" や　"1"　
                String paddingStr = sh.format("%2s", _START_HOUR_STR_ARRAY[0]).replace(" ", "0");  // "0" "1" "2" を　"00" "01" "02" へ成形してる
                String sm = _START_MINUTES_STR_ARRAY[0]; // "00" または　"30"　
                String eh = _END_HOUR_STR_ARRAY[0]; // "選択しない" が入ってくる可能性あり "0" や　"1"
                String paddingStr2 = "";
                if (!eh.equals("選択しない")) {
                     paddingStr2 = sh.format("%2s", _END_HOUR_STR_ARRAY[0]).replace(" ", "0");
                }

                String em = _END_MINUTES_STR_ARRAY[0]; // "選択しない" が入ってくる可能性あり  "00" または　"30"
            //    Toast.makeText(getActivity(),  " 日にちが" + sqlDateArray[0] + " 開始時間が" + START_HOUR_STR_ARRAY[0] + ":" + START_MINUTES_STR_ARRAY[0] + "です 終了時間は　" + END_HOUR_STR_ARRAY[0] + ":" + END_MINUTES_STR_ARRAY[0] + " です" , Toast.LENGTH_LONG).show();

                String insertST = paddingStr + ":" + sm;
                String insertET = "";
                if (!_END_HOUR_STR_ARRAY[0].equals("選択しない")) {
                    insertET = paddingStr2 + ":" + em;
                }

                 String etTitle = _editTextScheTitle.getText().toString();
                 String etMemo = _editTextScheMemo.getText().toString(); // 何も書いてないと ""空文字になってる
             //   Toast.makeText(getActivity(),  "タイトルは　" + etTitle + " です　メモは　" + etMemo + " です　", Toast.LENGTH_LONG).show();

                // データベースを取得する try-catch-resources構文 finallyを書かなくても必ず close()処理をしてくれます
                TimeScheduleDatabaseHelper helper =  new TimeScheduleDatabaseHelper(parentActivity);
                //          _db = MainActivity.helper.getWritableDatabase();  // クラス名::フィールド名 で　１つしかない　静的フィールド（クラスフィールド　static)を呼び出して使いまわす

                try (SQLiteDatabase db = helper.getWritableDatabase()) {  // dbはきちんとクローズ自動でしてくれます
            Toast.makeText(parentActivity, "接続しました", Toast.LENGTH_SHORT).show();
            // ここにデータベースの処理を書く

                    String sqlInsert = "INSERT INTO timeschedule (scheduledate, starttime, endtime, scheduletitle, schedulememo) VALUES (?,?,?,?,?)";

                    SQLiteStatement stmt = db.compileStatement(sqlInsert);
                    stmt.bindString(4, etTitle);
                    stmt.bindString(5, etMemo);
                    stmt.bindString(1, strDate);
                    stmt.bindString(2, insertST);
                    stmt.bindString(3, insertET);

                    stmt.executeInsert();
                }

                helper.close();  // ヘルパーを解放する

                // ここで db　をクローズする処理を書く　
                // クラスフィールドのhelperは使い回しするのでまだ　ここで クローズしないで MainActivityの　コールバックメソッドのonDestory()で解放してます

                Toast.makeText(getActivity(),  "スケジュールを新規登録しました", Toast.LENGTH_LONG).show();

                // スケジュールを挿入した年月が、現在の年月なら MainActivityへ　それ以外の月ならMonthCalendarActivityへ遷移する "2022-03-19"
                int year = Integer.parseInt(strDate.substring(0, 4));
                int month = Integer.parseInt(strDate.substring(5, 7));
                // 現在を取得して
                LocalDate localdateToday = LocalDate.now();
                Intent intent = null;
                if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {

                     intent = new Intent(parentActivity, MainActivity.class);
                } else {
                     intent = new Intent(parentActivity, MonthCalendarActivity.class);
                    intent.putExtra("specifyDate", DATE);  //  Date型情報を渡します
                }

                startActivity(intent);

                // 最後に 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();

            }
        });


        // 最後ビューをreturnする
        return view;
    }



    // メソッドメンバ
    public void changeSaveButton(Map<String, Boolean> map) {
        for (Boolean val : map.values()) {
            if (val == false) {
                _saveButton.setEnabled(false);
                // falseが見つかった時点で ボタンを付加にして
                return;  // 即終了その後のループは実行しないで 呼び出し元へ戻る
            } else {
                // 全部 trueだったら
                _saveButton.setEnabled(true);
            }
        }
    }


}
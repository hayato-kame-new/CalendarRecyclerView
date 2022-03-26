package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SpinnerAdapter;
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

/**
 * 新規登録の時と 編集の時にこのフラグメントでデータベースに接続する
 */
public class ScheduleFormFragment extends Fragment {
    // 大画面かどうかの判定フラグを 追加し、 onCreate()メソッドをオーバーライドして処理を記述
    private boolean _isLayoutXLarge = true;  // 追加

    TextView _formTitle, _textViewHourError, _textViewMinutesError;
    Button _returnMonButton, _currentMonButton;
    Spinner _spinnerStartHour, _spinnerStartMinutes, _spinnerEndHour, _spinnerEndMinutes;
    EditText _editTextScheTitle, _editTextScheMemo;
    CalendarView _calendarView;
    Button _saveButton, _deleteButton;  //  import android.widget.Button;

    // フラグ
    Map<String, Boolean> _buttonFlagMap = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意　getFragmentManager() 非推奨になりました
        FragmentManager manager = getParentFragmentManager();
        // フラグメントマネージャーから、 他のフラグメントが取得できる TimeScheduleFragmentを取得する
        TimeScheduleFragment timeScheduleFragment = (TimeScheduleFragment) manager.findFragmentById(R.id.timeScheduleFragment);

        // ScheduleFormFragment自分自身と、　同じアクティビティ上に、 TimeScheduleFragment が乗っていたら 大画面
        if (timeScheduleFragment == null) {
            // 通常画面
            _isLayoutXLarge = false;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TimeSheduleActivity (TimeSheduleFragment)　から  所属するアクティビティのScheduleFormActivityへ画面遷移してくる
        // このフラグメントが  所属するアクティビティのScheduleFormActivity の取得
        Activity parentActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_schedule_form, container, false);

        // 追加 大画面だった時の処理
        Bundle extras;

        if (_isLayoutXLarge) {  // 大画面だった時には、同じアクティビティ上で、フラグメント間でのデータの引き渡しをする
            extras = getArguments() ;  // TimeScheduleフラグメントから自分自身のフラグメントへデータを受け取る
        } else {

            //  所属するアクティビティから インテントを取得する
            Intent intent = parentActivity.getIntent();
            // インテントから、引き継ぎデータをまとめたものを取得
             extras = intent.getExtras();

        }


// フラグMap  インスタンス生成
        _buttonFlagMap = new HashMap<String, Boolean>();

        _saveButton = view.findViewById(R.id.saveButton);
        _deleteButton = view.findViewById(R.id.deleteButton);

        // 個々のデータを取得 うまく取得できなかった時のために String型は ""で初期化  Date型は nullで初期化
        final Date[] date = {null};
        String action = "";
        String timeString = "";  // 新規の時には送られこない 編集の時だけ送られてくる
        String scheduleTitleString = "";
        String scheduleMemoString = "";
        Integer intId = null;

        if (extras != null) {
            date[0] = (Date)extras.getSerializable("date");  // Date型へキャストが必要です
            action = extras.getString("action");  // "add" もしくは "edit" が入ってきます

            // 編集の時にだけ、 時間とタイトルとメモの情報が intentに乗っています 新規の時には送られこないので null が入ってくる
            timeString = extras.getString("timeString");  // 新規の時には送られこないので null が入ってくる 編集の時だけ送られてくる
            scheduleTitleString = extras.getString("scheduleTitleString");  // 新規の時は nullになる
            scheduleMemoString = extras.getString("scheduleMemoString");  // 新規の時は nullになる
            intId = extras.getInt("intId");  // 新規の時は nullになる
        }
        // 後でインナークラスで dateを使うので定数にしておく final つける
        final Date DATE = date[0];
        final String ACTION = action;
        final Integer ID = intId;

        String startH = "";
        String startM = "";
        String endH = "";
        String endM = "";

     //    if (!timeString.equals("")) {
        if (timeString != null) {
            // 編集の時  "[ 09:00 ~ 15:00 ]"  という形になっていますので 取り除きます
        // String result = s.replace("[", "").replace("]", "").replace(" ", "").replace("~", "").replace(":", "");
            // 注意  正規表現 角括弧は　バックスラッシュを2つ書くことで、対応します
            String replaced = timeString.replaceAll("[\\[\\]~ :]", "");  // "09001500"
             startH = replaced.substring(0, 2);  // "09"
            startM = replaced.substring(2,4);  // "00"
             endH = replaced.substring(4, 6); // "15"
             endM = replaced.substring(6); // "00"
            if (startH.substring(0, 1).equals("0")) {  // "0"で始まるならば "0"をとる
                startH = startH.substring(1, 2);  // 再代入
            }
            if (endH.substring(0, 1).equals("0")) { // "0"で始まるならば "0"をとる
                endH = endH.substring(1, 2);  // 再代入
            }
        }

        _formTitle = view.findViewById(R.id.formTitle);
        _spinnerStartHour = view.findViewById(R.id.spinnerStartHour);
        _spinnerStartMinutes = view.findViewById(R.id.spinnerStartMinutes);
        _spinnerEndHour = view.findViewById(R.id.spinnerEndHour);
        _spinnerEndMinutes = view.findViewById(R.id.spinnerEndMinutes);
        _editTextScheTitle = view.findViewById(R.id.editTextScheTitle);
        _editTextScheMemo = view.findViewById(R.id.editTextScheMemo);

        // ACTION の値によって分岐できるようにする
        if (ACTION.equals("add")) {  // 新規の時
            _formTitle.setText(R.string.tvFormTitleAdd);  // 新規の時に　新規スケジュール登録画面　と表示する
            _saveButton.setEnabled(false);  // 新規なら最初は保存ボタン押せないようになってる  false
            _deleteButton.setVisibility(View.GONE);  // 削除ボタン見えない
            // 新規の時には カレンダービューだけは初期値が入っている
        } else {  // 編集の時
            _formTitle.setText(R.string.tvFormTitleEdit);  // 編集の時に　編集-スケジュール登録画面　と表示する
            _saveButton.setEnabled(true);  // 編集なら最初は保存ボタン押せます
            _deleteButton.setVisibility(View.VISIBLE); // 削除ボタン見える
            // 編集の時には、時間フォーム タイトル メモ カレンダービュー に初期値を入れておくので
           ScheduleFormFragment.this.setSelection(_spinnerStartHour, startH);
            setSelection(_spinnerStartMinutes, startM);
            setSelection(_spinnerEndHour, endH);
            setSelection(_spinnerEndMinutes, endM);

//            SpinnerAdapter adapter = _spinnerStartHour.getAdapter();
//        int index = 0;
//        for (int i = 0; i < adapter.getCount(); i++) {
//            if (adapter.getItem(i).equals(startH)) {
//                index = i;
//                break;
//            }
//        }
//            _spinnerStartHour.setSelection(index);
//            String item = (String) _spinnerStartHour.getItemAtPosition(index);

            if (scheduleTitleString != null) {
                _editTextScheTitle.setText(scheduleTitleString);
            }
            if (_editTextScheMemo != null) {
                _editTextScheMemo.setText(scheduleMemoString);
            }
        }
        // カレンダービューに初期値をセット
        _calendarView = view.findViewById(R.id.calendarView);
        // ここで 新規の時も 編集の時にも CalendarViewに  初期値として 送られてきた 日時を設定します。
        _calendarView.setDate(DATE.getTime());  // 引数には long型 カレンダービューに初期値設定

        // ボタンの大きさなどを設定     getColor(int id)を使いたいのだが　非推奨なので API 23 から Deprecated（非推奨）
        //  代わりに public int getColor (int id, Resources.Theme theme) を使います
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // API 23 以上 は 新しいメソッドを使います
            _deleteButton.setBackgroundColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
        } else {
            // API 23 未満 の時には　非推奨メソッドを使用します
            _deleteButton.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        }
        // ボタン を少し小さくするには xmlファイルで設定するには「wrap_content」になってるので一旦0にする
        _deleteButton.setMinimumWidth(0); // ボタンの最小幅がデフォルトで64dipである  一旦0にする
        _deleteButton.setWidth(180);


        // Date型の getYear getMonth getDay　は　非推奨メソッドなので、SimpleDateFormatを使い、文字列として取得する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月");  // MM に　すると 01 02 03   M にすると 1  2  3
        String str = sdf.format(date[0]);  // ボタンに表示するための
        _returnMonButton = view.findViewById(R.id.returnMonButton);
        _returnMonButton.setText(str + "カレンダーに戻る");
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

        _currentMonButton = view.findViewById(R.id.currentMonButton);

        _textViewHourError = view.findViewById(R.id.textViewHourError);
        _textViewMinutesError = view.findViewById(R.id.textViewMinutesError);

        // スピナーの 　動的にリストを作るやりかた
//        List<String> endMList = new ArrayList<>();
//        endMList.add("00");
//        endMList.add("30");
//        ArrayAdapter<String> adapterEM = new ArrayAdapter<>(parentActivity, android.R.layout.simple_spinner_item, endMList);
//        adapterEM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        _spinnerEndMinutes.setAdapter(adapterEM);

        //  SQLite のテーブルのスキーマでは文字列の最大の長さを指定することはできない
        //  タイトルは android:maxLength="30"   メモは android:maxLength="80"  など xmlで入力文字数を制限することができる
        // タイトルは 新規ではの状態では　　""になってる  編集では必ず入ってる  (20文字以内で)
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
     //    _calendarView.setDate(DATE.getTime());  // 引数には long型
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

        // 開始の分を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] _START_MINUTES_STR_ARRAY = {""};
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
        final String[] _END_MINUTES_STR_ARRAY = {""};

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
                    // ここを追加しました！！！
                }else if (Integer.parseInt((String)adapterView.getItemAtPosition(i)) ==  Integer.parseInt(_END_HOUR_STR_ARRAY[0]) &&  (Integer.parseInt(_START_MINUTES_STR_ARRAY[0]) > Integer.parseInt(_END_MINUTES_STR_ARRAY[0]))) {
                    _textViewHourError.setError("開始時間と終了時間を確認してください");
                    _textViewHourError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("startHour", false);
                } else {
                    _buttonFlagMap.put("startHour", true );
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
                    // 追加
                    _textViewMinutesError.setError(null);
                    _textViewMinutesError.setText("");
                    _buttonFlagMap.put("endMinutes", true ); // 押せる
                    _buttonFlagMap.put("startMinutes", true ); // 押せる
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
             //   String select = (String)adapterView.getItemAtPosition(i);
                // 入力チェック

                if ( _START_HOUR_STR_ARRAY[0].equals("") || _START_MINUTES_STR_ARRAY[0].equals("") || _END_MINUTES_STR_ARRAY[0].equals("")) {
                    // 何もしない
                } else if ( Integer.parseInt((String)adapterView.getItemAtPosition(i)) < Integer.parseInt(_START_HOUR_STR_ARRAY[0])) {
                    _textViewHourError.setError("開始時間と終了時間を確認してください");
                    _textViewHourError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endHour", false );
                    // この下を追加しました！！
                } else if (Integer.parseInt((String)adapterView.getItemAtPosition(i)) ==  Integer.parseInt(_START_HOUR_STR_ARRAY[0]) &&  (Integer.parseInt(_START_MINUTES_STR_ARRAY[0]) > Integer.parseInt(_END_MINUTES_STR_ARRAY[0]))){
                    _textViewHourError.setError("開始時間と終了時間を確認してください");
                    _textViewHourError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endHour", false );
                }else {
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _buttonFlagMap.put("startHour", true );  // 押せる
                    // 追加
                    _textViewMinutesError.setError(null);
                    _textViewMinutesError.setText("");
                    _buttonFlagMap.put("endMinutes", true ); // 押せる
                    _buttonFlagMap.put("startMinutes", true ); // 押せる

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
       // final String[] _START_MINUTES_STR_ARRAY = {""};
        // 終了時間を表す文字列の定数　インナークラスで使うから final で定数化しておく必要がある。また、配列にすると、要素を書き換えるようにできる
       //  final String[] _END_MINUTES_STR_ARRAY = {""};

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
                    _buttonFlagMap.put("endMinutes", true ); //押せる

                    _buttonFlagMap.put("startHour", true );
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
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
                } else if ( Integer.parseInt(_END_HOUR_STR_ARRAY[0]) < Integer.parseInt(_START_HOUR_STR_ARRAY[0])) {  // ここ追加した
                    _textViewMinutesError.setError("開始時間と終了時間を確認してください");
                    _textViewMinutesError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endMinutes", false );
                } else if (( Integer.parseInt(_END_HOUR_STR_ARRAY[0]) == Integer.parseInt(_START_HOUR_STR_ARRAY[0]))  &&  (Integer.parseInt((String)adapterView.getItemAtPosition(i)) < Integer.parseInt(_START_MINUTES_STR_ARRAY[0]))) {
                    _textViewMinutesError.setError("開始時間と終了時間を確認してください");
                    _textViewMinutesError.setText("開始時間と終了時間を確認してください");
                    // 押せない
                    _buttonFlagMap.put("endMinutes", false );
                } else {
                    _textViewMinutesError.setError(null);
                    _textViewMinutesError.setText("");
                    _buttonFlagMap.put("endMinutes", true ); // 押せる
                    _buttonFlagMap.put("startMinutes", true ); // 押せる
                    // 追加
                    _buttonFlagMap.put("startHour", true );
                    _buttonFlagMap.put("endHour", true );  // 押せる
                    _textViewHourError.setError(null);
                    _textViewHourError.setText("");
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
                // "0" "1" "2" を　"00" "01" "02" へ成形してる
                String paddingStr = sh.format("%2s", _START_HOUR_STR_ARRAY[0]).replace(" ", "0");
                String sm = _START_MINUTES_STR_ARRAY[0]; // "00" または　"30"　
                String eh = _END_HOUR_STR_ARRAY[0]; //  "0" や　"1"
                String paddingStr2 = sh.format("%2s", _END_HOUR_STR_ARRAY[0]).replace(" ", "0");

                String em = _END_MINUTES_STR_ARRAY[0]; // "00" または　"30"

                String insertST = paddingStr + ":" + sm;
                String insertET =  paddingStr2 + ":" + em;

                 String etTitle = _editTextScheTitle.getText().toString();
                 String etMemo = _editTextScheMemo.getText().toString(); // 何も書いてないと ""空文字になってる

                // データベースを取得する try-catch-resources構文 finallyを書かなくても必ず close()処理をしてくれます
                // 使ったらすぐに helper close()すること 同じメソッド内ですぐに close()する onDestory()には書かないでください
                TimeScheduleDatabaseHelper helper =  new TimeScheduleDatabaseHelper(parentActivity);

                try (SQLiteDatabase db = helper.getWritableDatabase()) {  // dbはきちんとクローズ自動でしてくれます
            Toast.makeText(parentActivity, "接続しました", Toast.LENGTH_SHORT).show();
            // ここにデータベースの処理を書く
                    // もし、action が "add" なら INSERT  "edit"なら UPDATE します
                    if (ACTION.equals("add")) {  // 新規作成なら
                        String sqlInsert = "INSERT INTO timeschedule (scheduledate, starttime, endtime, scheduletitle, schedulememo) VALUES (?,?,?,?,?)";

                        SQLiteStatement stmt = db.compileStatement(sqlInsert);
                        stmt.bindString(4, etTitle);
                        stmt.bindString(5, etMemo);
                        stmt.bindString(1, strDate);
                        stmt.bindString(2, insertST);
                        stmt.bindString(3, insertET);

                        stmt.executeInsert();
                    } else { // 編集なら
                        // 　final 定数の ID　使う  ""では途中で改行しないように書く
                        String sqlUpdate = "UPDATE timeschedule SET scheduledate = ?, starttime = ?, endtime = ?, scheduletitle = ?, schedulememo = ?  WHERE _id = ?";

                         SQLiteStatement stmt = db.compileStatement(sqlUpdate);
                        stmt.bindString(4, etTitle);
                        stmt.bindString(5, etMemo);
                        stmt.bindString(1, strDate);
                        stmt.bindString(2, insertST);
                        stmt.bindString(3, insertET);
                        stmt.bindLong(6, ID);  // 主キーを指定する

                        stmt.executeUpdateDelete();
                    }
                }

                helper.close();  // ヘルパーを解放する  ここで

                if (ACTION.equals("add")) {  // 新規作成なら
                    Toast.makeText(getActivity(), "スケジュールを新規登録しました", Toast.LENGTH_LONG).show();
                } else if (ACTION.equals("edit")) {
                    Toast.makeText(getActivity(), "スケジュールを編集しました", Toast.LENGTH_LONG).show();
                }
                // スケジュールを挿入した年月が、現在の年月なら MainActivityへ　それ以外の月ならMonthCalendarActivityへ遷移する "2022-03-19"
                int year = Integer.parseInt(strDate.substring(0, 4));
                int month = Integer.parseInt(strDate.substring(5, 7));
                // 現在を取得して
                LocalDate localdateToday = LocalDate.now();
                Intent intent = null;
                if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
                    // これおかしいんじゃないかな 自分自身を終わらせるだけでいいんじゃないかな、でもそうするとデータは半円されない
                    intent = new Intent(parentActivity, MainActivity.class);
                    startActivity(intent);  // これ
                } else {
                    intent = new Intent(parentActivity, MonthCalendarActivity.class);
                    intent.putExtra("specifyDate", DATE);  //  Date型情報を渡します
                    startActivity(intent);  // これ
                }


                Activity parentActivity = getActivity();


                // 最後に 自分自身が所属するアクティビティを終了させます
                // Activity parentActivity = getActivity();
                Log.i("finish", "finishを呼びました");
                parentActivity.finish();


            }
        });

        // 削除ボタンにリスナーつける  ダイアログフラグメント DeleteConfirmDialogFragment を表示する
        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = sqlDateArray[0];  // "2022-03-19"
                String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                // ダイアログを表示させます DialogFragmentを継承したダイアログフラグメントクラスを作ったので
                // ここで newして インスタンスを生成する

                DeleteConfirmDialogFragment dialogFragment = new DeleteConfirmDialogFragment();

                Bundle args = new Bundle();


                args.putString("strDate", strDate);  // 日付もつける
                // 　final 定数の ID　使う
                args.putString("strId", String.valueOf(ID));
                args.putString("scheduleTitle", _editTextScheTitle.getText().toString());
                dialogFragment.setArguments(args);

//                dialogFragment.show(getSupportFragmentManager(), "DeleteConfirmDialogFragment");
                Activity parentActivity = getActivity();



              //   削除ボタン押すと  ダイアログフラグメント DeleteConfirmDialogFragment を表示するだけ
                // Activityクラスではダメです FragmentActivityクラスにキャストをしてください。
                FragmentActivity fragmentActivity = (FragmentActivity) parentActivity;
                // 第二引数は任意だから、クラス名にしておいた ダイアログを識別するための 名前を付けている
                dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "DeleteConfirmDialogFragment");
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


    public static void setSelection(Spinner spinner, String item) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(item)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }


}
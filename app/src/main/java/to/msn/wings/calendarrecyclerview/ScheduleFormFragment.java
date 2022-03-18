package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.material.navigation.NavigationBarView;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ScheduleFormFragment extends Fragment {


    TextView _formTitle;
    Button _returnMonButton, _currentMonButton;
    Spinner _spinnerStartHour, _spinnerStartMinutes, _spinnerEndHour, _spinnerEndMinutes;
    EditText _editTextScheTitle, _editTextScheMemo;
    CalendarView _calendarView;




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
        // 初期値は、遷移してきた時に選択してあった日付にしておくので
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


        _spinnerStartHour = view.findViewById(R.id.spinnerStartHour);
        _spinnerStartMinutes = view.findViewById(R.id.spinnerStartMinutes);
        _spinnerEndHour = view.findViewById(R.id.spinnerEndHour);
        _spinnerEndMinutes = view.findViewById(R.id.spinnerEndMinutes);
        // xmlファイルで android:entries="@array/spinnerStartHour"を書くやり方もある こっちでもできる
        // ４つも書くと多くなるので 今回は android:entries="@array/spinnerStartHour"を書く
//        ArrayAdapter<CharSequence> adapterSH = ArrayAdapter.createFromResource(parentActivity,
//                R.array.spinnerStartHour, android.R.layout.simple_spinner_item);
//        adapterSH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerStartHour.setAdapter(adapterSH);

        _spinnerStartHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "データベースに登録する日付は" + sqlDateArray[0].toString(), Toast.LENGTH_SHORT).show();

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


        _spinnerStartMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
        _spinnerEndHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // もし、選択しないを選んだ時にはnullにするように書く、 分の _spinnerEndMinutes　をリセットして nullへ変更にしてから 見えなくする処理を書く
               //  分の _spinnerEndMinutesをすでに選択しているかもしれないから
                if ( i == 0) {  // ポジションが 0番目のアイテムを View.INVISIBLEにする
                    // SpinnerはView.GONEにしてしまうとonItemSelectedイベントが発動しないので注意
                    _spinnerEndMinutes.setVisibility(View.INVISIBLE);  // 場所はそのまま確保したままで 見えなくする
                  //    _spinnerEndMinutes.setSelected(false); // これ何??
                    // 選択しない時には、 下の分 にも nullとしたいのだが

//                    String str = _spinnerEndMinutes.getSelectedItem().toString(); // getSelectedItem()の戻り値は Object型だから
//                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show(); // 確認

                } else {
                    _spinnerEndMinutes.setVisibility(View.VISIBLE);

                }

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
        _spinnerEndMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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








        // 保存ボタンにリスナーをつけて　ボタンを押した時に、データベースに登録します あり得ないが入力されたときにはToastを表示させたい。
        //  スタート時間が、終了時間よりも後ろだった場合に再入力を促します 入力チェックをします 保存ボタンを押した時に入力チェックを行う
        // "CREATE TABLE timeschedule (" + "_id INTEGER PRIMARY KEY , scheduledate DATE NOT NULL," +
        //                    " starttime DATETIME NOT NULL, endtime DATETIME , scheduletitle TEXT NOT NULL, schedulememo TEXT)"


        // 最後ビューをreturnする
        return view;
    }
}
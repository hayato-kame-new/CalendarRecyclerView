package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ScheduleFormFragment extends Fragment {


    TextView formTitle;
    Button returnMonButton, currentMonButton;
    Spinner spinnerStartHour, spinnerStartMinutes, spinnerEndHour, spinnerEndMinutes;
    EditText editTextScheTitle, editTextScheMemo;

    CalendarView calendarView;


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
        Date date = null;
        String action = "";
        if (extras != null) {
            date = (Date)extras.getSerializable("date");  // Date型へキャストが必要です
            action = extras.getString("action");
        }
        // 後でインナークラスで dateを使うので定数にしておく final つける
        final Date DATE = date;

        formTitle = view.findViewById(R.id.formTitle);
        // もし、新規登録ボタンをクリックしてきたら、新規であることをintentでデータで送ってきた action の値によって分岐できるようにする
        if (action.equals("add")) {
            formTitle.setText(R.string.tvFormTitleAdd);  // 新規の時に　新規スケジュール登録画面　と表示する
        }

        // Date型の getYear getMonth getDay　は　非推奨メソッドなので、SimpleDateFormatを使い、文字列として取得する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月");  // MM に　すると 01 02 03   M にすると 1  2  3
        String str = sdf.format(date);  // ボタンに表示するための
        returnMonButton = view.findViewById(R.id.returnMonButton);
        returnMonButton.setText(str + "のカレンダーに戻る");
        // 比較するために フォーマットし直して
         sdf = new SimpleDateFormat("yyyy年MM月"); // MM に　すると 01 02 03
        String strMM = sdf.format(date);
         int year = Integer.parseInt(strMM.substring(0, 4));
         int month = Integer.parseInt(strMM.substring(5, 7));
        // もし、今月ならば returnMonButtonを非表示にする
        // 現在を取得して
        LocalDate localdateToday = LocalDate.now();
        if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
            // 後で、コメントアウトじゃなくする
            // ここ！！　後でコメント外してください！！！！！
          //  returnMonButton.setVisibility(View.GONE); // これで表示しない なおかつ 非表示にしたスペースを詰める
        }

        spinnerStartHour = view.findViewById(R.id.spinnerStartHour);
        spinnerEndHour = view.findViewById(R.id.spinnerEndHour);
        spinnerStartMinutes = view.findViewById(R.id.spinnerStartMinutes);
        spinnerEndMinutes = view.findViewById(R.id.spinnerEndMinutes);
        // xmlファイルで android:entries="@array/spinnerStartHour"を書くやり方もある こっちでもできる
        // ４つも書くと多くなるので 今回は android:entries="@array/spinnerStartHour"を書く
//        ArrayAdapter<CharSequence> adapterSH = ArrayAdapter.createFromResource(parentActivity,
//                R.array.spinnerStartHour, android.R.layout.simple_spinner_item);
//        adapterSH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerStartHour.setAdapter(adapterSH);

        // 遷移してくる前に表示していた　カレンダーの年と月に戻るために、
        returnMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // インナークラスなので 定数 DATEを使う


               // 最後に 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();
            }
        });

        currentMonButton = view.findViewById(R.id.currentMonButton);
        //  現在(今月)のカレンダーの表示へ遷移する MainActivityに戻る  自分自身が所属するアクティビティを終了させます
        currentMonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity, MainActivity.class);

                startActivity(intent);

                // 自分自身が所属するアクティビティを終了させます
                Activity parentActivity = getActivity();
                parentActivity.finish();

            }
        });


        // 保存ボタンにリスナーをつけて　ボタンを押した時に、データベースに登録します あり得ないが入力されたときにはToastを表示させたい。




        // 最後ビューをreturnする
        return view;
    }
}
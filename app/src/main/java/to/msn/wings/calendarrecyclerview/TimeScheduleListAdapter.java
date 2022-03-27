package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
// import android.app.FragmentManager; // こっちではない  androidx  の方を使うこと
// import android.app.FragmentTransaction;  // こっちではない  androidx の方を「使うこと

//import androidx.fragment.app.FragmentManager; // こっちです
//import androidx.fragment.app.FragmentTransaction;　// こっちです
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeScheduleListAdapter extends RecyclerView.Adapter<TimeScheduleListHolder>{
    // フィールド
    private ArrayList<TimeScheduleListItem> data;
    // 大画面かどうかの判定フラグを 追加し、 onCreate()メソッドをオーバーライドして処理を記述
    private boolean _isLayoutXLarge = true;  // 追加
     Context context;  // 追加コンストラクタで引数で渡ってきた値で 初期値をセットする

   // コンストラクタを変更しました
   //  public TimeScheduleListAdapter(ArrayList<TimeScheduleListItem> data) {
//        this.data = data;
//    }

    /**
     * コンストラクタ
     * TimeScheduleFragmentでインスタンスかする時に第二引数にコンテキストを渡しているので、それを第二引数で受け取る
     * コンストラクタの中で、画面が大画面かどうかを判断する フィールドの_isLayoutXLargeに再代入してる
     * @param data
     * @param context
     */
    public TimeScheduleListAdapter(ArrayList<TimeScheduleListItem> data, Context context ) {
        Log.i("Adapter", "コンストラクタが呼ばれました");
        this.data = data;
        this.context = context; // TimeScheduleActivityが引数で渡ってくる
        // Contextをアクティビティへ変換してからgetSupportFragmentManager()を呼び出す
        AppCompatActivity appCompatActivity = (AppCompatActivity) this.context;

        // 注意　getFragmentManager() 非推奨になりました
        // アクティビティからなら getSupportFragmentManager()を呼び出しますし、フラグメントから getParentFragmentManager()を呼び出します
        androidx.fragment.app.FragmentManager fmanager =  appCompatActivity.getSupportFragmentManager();

       //  フラグメントマネージャーから、 他のフラグメントが取得できる TimeScheduleFragmentを取得する
       TimeScheduleFragment timeScheduleFragment = (TimeScheduleFragment)fmanager.findFragmentById(R.id.timeScheduleFragment);

       //   ScheduleFormFragment自分自身と、　同じアクティビティ上に、 TimeScheduleFragment が乗っていたら 大画面
        if (timeScheduleFragment == null) {
            // 通常画面
            _isLayoutXLarge = false;
        }
    }

    @NonNull
    @Override
    public TimeScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.i("Adapter", "onCreateViewHolderが呼ばれました");
        // v は、 CardViewのオブジェクトです
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_schedule_list_item, parent, false);
        // 編集 削除 をするために、フォームを表示するリスナーです
        // 大画面の時と、スマホのサイズの時に、挙動が違いますので注意
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = parent.getContext();
                Activity parentActivity = (Activity) context;
                // クリックしたアイテムの 日にちの情報   内部クラスで取得する
                TextView date = v.findViewById(R.id.date);  // 内部クラスで取得する
                // クリックした時に取得するテキストは 内部クラスで取得する "2022/03/01" とかになってる
                String dateString = date.getText().toString();  // クリックした時に取得するテキストは
                Date editDate = null;
                try {
                    editDate = new SimpleDateFormat("yyyy/MM/dd").parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 時間の情報を送ります
                TextView time = v.findViewById(R.id.time);
                String timeString = time.getText().toString();
                // タイトルの情報を送ります
                TextView scheduleTitle = v.findViewById(R.id.scheduleTitle);
                String scheduleTitleString = scheduleTitle.getText().toString();
                // メモの情報を送ります
                TextView scheduleMemo = v.findViewById(R.id.scheduleMemo);
                String scheduleMemoString = scheduleMemo.getText().toString();

                TextView id = v.findViewById(R.id.id);
                String strId = id.getText().toString();
                // intに変換して送ります
                int intId = Integer.parseInt(strId);


                // 大画面の場合 追加
                androidx.fragment.app.FragmentManager fmanager = null;
                androidx.fragment.app.FragmentTransaction ftransaction = null;
                // 大画面の場合 同じアクティビティ上 の右に　フラグメントを新たに乗せます FrameLayoutにしてあるので、上に乗せられるのです
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", editDate);  // DATE型
                bundle.putString("action", "edit");
                bundle.putString("timeString", timeString);
                bundle.putString("scheduleTitleString", scheduleTitleString);
                bundle.putString("scheduleMemoString", scheduleMemoString);
                bundle.putInt("intId", intId);  // int型

                if (_isLayoutXLarge) { // 大画面の場合 同じアクティビティ上で、フラグメントをreplaceする
                    fmanager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();

                     ftransaction = fmanager.beginTransaction();
                    // フォームのフラグメント生成
                    ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
                    // 引き継ぎデータをセット
                    scheduleFormFragment.setArguments(bundle);
                    // 生成したフラグメントを、
                    // id が　timeScheduleFrame　の　FrameLayoutの上に乗せます (FrameLayoutは上に追加できます)replaceメソッドで置き換えます

                    ftransaction.replace(R.id.timeScheduleFrame, scheduleFormFragment); // 第一引数の上に 第二引数を乗せて表示する
                    ftransaction.commit();
                    // 同じアクティビティ上なので、所属するアクティビティを終了させません

                } else {

                     // 小さいサイズの時
                    Intent intent = new Intent(parentActivity, ScheduleFormActivity.class); // 新しくintentオブジェクトを作る

                    intent.putExtra("date", editDate);  // 日付を送ってる Date型情報を渡します インナークラスで使うので finalにしてる
                    intent.putExtra("action", "edit");  // 編集ということもわかるようにデータを送る キーが "action"  値が String型の "edit"

                    // 編集の時には、新規とは違って、時間やタイトル メモの情報も送ります
                    intent.putExtra("timeString", timeString);
                    intent.putExtra("scheduleTitleString", scheduleTitleString);
                    intent.putExtra("scheduleMemoString", scheduleMemoString);
                    // データベースでは _idカラムで検索するので
                    intent.putExtra("intId", intId);

                    parentActivity.startActivity(intent);  // context.startActivity(intent); でもいい

                  // 小さいスマホサイズなら、画面遷移ありなので 現在のフラグメントを乗せてるサブのアクティビティを終わらせてください
                    parentActivity.finish();// 小さいスマホサイズなら 自分自身が所属するアクティビティを終了させます
                }
            }
        });

        // 最後に ビューを ビューホルダーにセットして ビューホルダーのインスタンスをリターンする
        return new TimeScheduleListHolder(v);
    }

    /**
     * ビューにデータを割り当てて リスト項目を生成
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TimeScheduleListHolder holder, int position) {
        Log.i("Adapter", "onBindViewHolderが呼ばれました");
        // 日付け
        String dateText = this.data.get(position).getDate();
        holder.date.setText(dateText);
        // 開始時間 ~ 終了時間 を表示する
        String timeText = "[ " + this.data.get(position).getStartTime() + " ~ " + this.data.get(position).getEndTime() + " ]";
        holder.time.setText(timeText);
        holder.time.setTextColor(Color.parseColor("#006400"));

        // スケジュールのタイトル
        String title = this.data.get(position).getScheduleTitle();
        if(title.length() > 30) {  // 注意エラーに  制限を後で android:maxLength="30"  つけたので　大丈夫だが一応
            title = title.substring(0, 31);  // 後で変更すること
        }


        holder.scheduleTitle.setText(title);
        // 下線もつけられます  リンクに見せるようにできる
        //  <string name="link"><u>リンク文字列</u></string>  でも下線がつけられる  android:text="@string/link" とすれいい android:clickable="true"
        TextView scheduleTitle = holder.view.findViewById(R.id.scheduleTitle);
        scheduleTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        scheduleTitle.setTextColor(Color.parseColor("blue"));


        // スケジュールのメモ
        String memo = this.data.get(position).getScheduleMemo();
        if(memo.length() > 80) {  // 注意エラーに
            memo = memo.substring(0, 81); // 後で変更すること 制限を後で android:maxLength="80"  つけたので　大丈夫だが一応
        }

        holder.scheduleMemo.setText(memo);
        TextView scheduleMemo = holder.view.findViewById(R.id.scheduleMemo);
        scheduleMemo.setTextColor(Color.parseColor("#333132"));

        // idのTextView  非表示にして、データだけをフォームに送ります 上のonClickで送ってます
        long longId = this.data.get(position).getId();
        // Stringへ変換する
        String strId = String.valueOf(longId);
        holder.id.setText(strId);
        // textViewGone を非表示としたい  大切  View.VISIBLE・・・表示
        // View.INVISIBLE・・・非表示（非表示にしたスペースは詰めない）
        // View.GONE・・・非表示（非表示にしたスペースを詰める）
        TextView id = holder.view.findViewById(R.id.id);
        id.setVisibility(View.GONE);  // これで表示しない なおかつ 非表示にしたスペースを詰める

    }

    @Override
    public int getItemCount() {
        Log.i("Adapter", "getItemCount()が呼ばれました");
        // return 0;
        return this.data.size();
    }



}

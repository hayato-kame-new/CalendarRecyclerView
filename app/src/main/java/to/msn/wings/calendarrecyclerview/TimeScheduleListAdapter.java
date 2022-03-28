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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeScheduleListAdapter extends RecyclerView.Adapter<TimeScheduleListHolder>{
    // フィールド
    private ArrayList<TimeScheduleListItem> _data;
    // 大画面かどうかの判定フラグ インスタンスフィールド   宣言だけをしておき、クリックされた時点での、TimeScheduleFragmentのゲッター画面の状態を取得する
    // onCreateViewHolderでこのインスタンスフィールドに値をセットします！！ 画面サイズの状態を代入します
    // その後で、onBindViewHolderでもインスタンスフィールドから取得して使います
    private boolean _isLayoutXLarge;  // 宣言だけ クラスのインスタンスフィールドの初期値は　falseになっています

    /**
     * コンストラクタ
     * @param data
     */
     public TimeScheduleListAdapter(ArrayList<TimeScheduleListItem> data) {
        this._data = data;
    }

    @NonNull
    @Override
    public TimeScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // parentは　RecyclerViewです
            Log.i("Adapter", "onCreateViewHolderが呼ばれました");
        // v は、 CardViewのオブジェクトです
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_schedule_list_item, parent, false);


        // 大画面の場合 追加

        androidx.fragment.app.FragmentManager fmanager = null;
        final androidx.fragment.app.FragmentTransaction[] ftransaction = {null};
        fmanager = ((FragmentActivity) parent.getContext()).getSupportFragmentManager();
        //  フラグメントマネージャーから、 TimeScheduleActivityに所属してる フラグメントが取得できる TimeScheduleFragmentを取得する timeScheduleFrame
        TimeScheduleFragment timeScheduleFragment = (TimeScheduleFragment)fmanager.findFragmentById(R.id.timeScheduleFragment);
        // このクラスのインスタンスフィールドに値をセットします！！ このあと、onBindViewHolderでもインスタンスフィールドから取得するので
        _isLayoutXLarge = timeScheduleFragment.is_isLayoutXLarge();  // ゲッターメソッドを使う




        // 編集 削除 をするために、フォームを表示するリスナーです


        // 大画面の時と、スマホのサイズの時に、挙動が違いますので注意
        final androidx.fragment.app.FragmentManager  finalFmanager = fmanager;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = parent.getContext(); // context　は　TimeScheduleActivity
                Activity parentActivity = (Activity) context; // TimeScheduleActivity
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
//                androidx.fragment.app.FragmentManager fmanager = null;
//                androidx.fragment.app.FragmentTransaction ftransaction = null;


                // 大画面の場合 同じアクティビティ上 の右に　フラグメントを新たに乗せます FrameLayoutにしてあるので、上に乗せられるのです
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", editDate);  // DATE型
                bundle.putString("action", "edit");
                bundle.putString("timeString", timeString);
                bundle.putString("scheduleTitleString", scheduleTitleString);
                bundle.putString("scheduleMemoString", scheduleMemoString);
                bundle.putInt("intId", intId);  // int型


                // 判定するために TimeScheduleFragmentのフィールドにアクセスして値を調べる必要がある
                // クリックした時点でのフィールドを調べる
            //    fmanager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                //  フラグメントマネージャーから、 TimeScheduleActivityに所属してる フラグメントが取得できる TimeScheduleFragmentを取得する timeScheduleFrame
            //     TimeScheduleFragment timeScheduleFragment = (TimeScheduleFragment)fmanager.findFragmentById(R.id.timeScheduleFragment);
                // このクラスのインスタンスフィールドに値をセットします！！ このあと、onBindViewHolderでもインスタンスフィールドから取得するので
            //    _isLayoutXLarge = timeScheduleFragment.is_isLayoutXLarge();  // ゲッターメソッドを使う



               if (_isLayoutXLarge) { // 大画面の場合 同じアクティビティ上で、フラグメントをreplaceする

                 //    fmanager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();

                     ftransaction[0] = finalFmanager.beginTransaction();
                    // フォームのフラグメント生成
                    ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
                    // 引き継ぎデータをセット
                    scheduleFormFragment.setArguments(bundle);
                    // 生成したフラグメントを、
                    // id が　timeScheduleFrame　の　FrameLayoutの上に乗せます (FrameLayoutは上に追加できます)replaceメソッドで置き換えます

                    ftransaction[0].replace(R.id.timeScheduleFrame, scheduleFormFragment); // 第一引数の上に 第二引数を乗せて表示する
                    ftransaction[0].commit();
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
        Log.i("Adapter", "onBindViewHolderが呼ばれました"); // onCreateViewHolderの後に呼ばれます
        // 日付け
        String dateText = this._data.get(position).getDate();
        holder.date.setText(dateText);
        // 開始時間 ~ 終了時間 を表示する
        String timeText = "[ " + this._data.get(position).getStartTime() + " ~ " + this._data.get(position).getEndTime() + " ]";
        holder.time.setText(timeText);
        holder.time.setTextColor(Color.parseColor("#006400"));
        if(_isLayoutXLarge) {  // 大画面だったら
            holder.time.setTextSize(17);
        } else {  // 通常サイズなら
            holder.time.setTextSize(14);
        }

        // スケジュールのタイトル
        String title = this._data.get(position).getScheduleTitle();
        if(title.length() > 30) {  // 注意エラーに  制限を後で android:maxLength="30"  つけたので　大丈夫だが一応
            title = title.substring(0, 31);  // 制限を android:maxLength="30"  つけたので　大丈夫だが一応
        }

        holder.scheduleTitle.setText(title);
        // 下線もつけられます  リンクに見せるようにできる
        //  <string name="link"><u>リンク文字列</u></string>  でも下線がつけられる  android:text="@string/link" とすれいい android:clickable="true"
        TextView scheduleTitle = holder.view.findViewById(R.id.scheduleTitle);
        scheduleTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        scheduleTitle.setTextColor(Color.parseColor("blue"));
        if(_isLayoutXLarge) {  // 大画面だったら
            holder.scheduleTitle.setTextSize(22);
        } else {  // 通常サイズなら
            holder.scheduleTitle.setTextSize(20);
        }


        // スケジュールのメモ
        String memo = this._data.get(position).getScheduleMemo();
        if(memo.length() > 80) {  // 注意エラーに
            memo = memo.substring(0, 81); // 制限を android:maxLength="80"  つけたので　大丈夫だが一応
        }

        holder.scheduleMemo.setText(memo);
        TextView scheduleMemo = holder.view.findViewById(R.id.scheduleMemo);
        scheduleMemo.setTextColor(Color.parseColor("#333132"));
        if(_isLayoutXLarge) {  // 大画面だったら
            holder.scheduleMemo.setTextSize(15);
        } else {  // 通常サイズなら
            holder.scheduleMemo.setTextSize(13);
        }

        // idのTextView  非表示にして、データだけをフォームに送ります 上のonClickで送ってます
        long longId = this._data.get(position).getId();
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
        return this._data.size();
    }

}

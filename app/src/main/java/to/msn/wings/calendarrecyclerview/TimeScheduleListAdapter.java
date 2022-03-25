package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TimeScheduleListAdapter extends RecyclerView.Adapter<TimeScheduleListHolder>{
    // フィールド
    private ArrayList<TimeScheduleListItem> data;

    /**
     * コンストラクタ
     * @param data
     */
    public TimeScheduleListAdapter(ArrayList<TimeScheduleListItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TimeScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // v は、 CardViewのオブジェクトです
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_schedule_list_item, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 小さいスマホサイズなら、画面遷移あり 新しいアクティビティへ画面遷移する その上のフラグメント切り替えるようにして
                // 新規フラグメントや編集フラグメントのフォームを作る

                // タブレットサイズなら、画面遷移なし fragment_time_schedule.xml　の　RecyclerView　の親に、LinearLayoutにして、
                // 左にCardView  右に新規や編集のフォームを作る
// context  TimeSheduleActivity のこと
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

//                // 小さいスマホサイズなら、画面遷移ありなので 現在のフラグメントを乗せてるサブのアクティビティを終わらせてください
//                // 小さいスマホサイズなら 自分自身が所属するアクティビティを終了させます

                parentActivity.finish();
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
        // return 0;
        return this.data.size();
    }



}

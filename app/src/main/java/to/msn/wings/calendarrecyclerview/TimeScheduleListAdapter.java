package to.msn.wings.calendarrecyclerview;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

    // メソッドを3つオーバーライドすること

    /**
     * R.layout.time_schedule_list_itemレイアウトをインフレートする
     *  カードビューにリスナーをつけたい時にはこのonCreateViewHolderに書ける
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public TimeScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 個々のリストアイテムのレイアウトファイルでインフレートしたビューのインスタンスを生成して
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_schedule_list_item, parent, false);

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
        TextView scheduleTitle = holder.view.findViewById(R.id.scheduleTitle);
        scheduleTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        scheduleTitle.setTextColor(Color.parseColor("blue"));

        // スケジュールのメモ
        String memo = this.data.get(position).getScheduleMemo();
        if(memo.length() > 80) {  // 注意エラーに
            memo = memo.substring(0, 81); // 後で変更すること 制限を後で android:maxLength="80"  つけたので　大丈夫だが一応
        }

        holder.scheduleMemo.setText("メモ: " + memo);

    }

    @Override
    public int getItemCount() {
        // return 0;
        return this.data.size();
    }
}

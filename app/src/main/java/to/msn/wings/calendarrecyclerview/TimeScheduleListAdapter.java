package to.msn.wings.calendarrecyclerview;

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
     * 自分で作った個々のリストアイテムのレイアウトをインフレートする
     * カードビューにリスナーをつけたい時にはこのonCreateViewHolderに書く
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public TimeScheduleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //  return null;

        // 個々のリストアイテムのレイアウトファイルでインフレートしたビューのインスタンスを生成して
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_schedule_list_item, parent, false);
        // CardViewにリスナーをつけたい時には、この onCreateViewHolderメソッドに書く


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

        // 開始時間 ~ 終了時間 を表示するので
        String text = this.data.get(position).getStartTime() + " ~ " + this.data.get(position).getEndTime();
        holder.time.setText(text);
        holder.time.setTextColor(Integer.parseInt("green"));

        // スケジュールのタイトル
        String title = this.data.get(position).getScheduleTitle();
        title = title.substring(0, 10);
        holder.scheduleTitle.setText(title);
        // 下線もつけられます
        TextView scheduleTitle = holder.view.findViewById(R.id.scheduleTitle);
          scheduleTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // スケジュールのメモ
        String memo = this.data.get(position).getScheduleMemo();
        memo = memo.substring(0,10);
        holder.scheduleMemo.setText("メモ: " + memo);

    }

    @Override
    public int getItemCount() {
        // return 0;
        return this.data.size();
    }
}

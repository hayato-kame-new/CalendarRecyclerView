package to.msn.wings.calendarrecyclerview;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * CakendarCellviewHolder型　の　RecyclerView.Adapterアダプタを継承すること
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarCellViewHolder> {

    // フィールド
    private ArrayList<CalendarCellItem> data;

    // コンストラクタ
    public CalendarAdapter(ArrayList<CalendarCellItem> data) {
        this.data = data;
    }

    /**
     * 3つのメソッドをオーバーライドすべき
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public CalendarCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return null;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_cell, parent, false);
        return new CalendarCellViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarCellViewHolder holder, int position) {

        holder.dateText.setText(this.data.get(position).getDateText());

    }

    @Override
    public int getItemCount() {
       // return 0;
        return this.data.size();
    }
}

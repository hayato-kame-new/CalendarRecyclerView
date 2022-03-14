package to.msn.wings.calendarrecyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView.ViewHolderを継承すること
 */
public class CalendarCellViewHolder extends RecyclerView.ViewHolder {

    //　フィールド
    View view;  // ルート要素のビューここだと ConstraintLayoutのことだと思うけど？？

    TextView dateText;

    TextView textViewToday;

    /**
     * コンストラクタ
     * @param itemView
     */
    public CalendarCellViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.dateText = view.findViewById(R.id.dateText);
        this.textViewToday = view.findViewById(R.id.textViewToday);
    }
}

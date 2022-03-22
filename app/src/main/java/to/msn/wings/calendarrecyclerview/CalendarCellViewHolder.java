package to.msn.wings.calendarrecyclerview;

import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView.ViewHolderを継承すること
 */
public class CalendarCellViewHolder extends RecyclerView.ViewHolder {

    //　フィールド
    View view;  // ルート要素のビューここだと ConstraintLayoutのこと


    TextView dateText;  // ルートの配下のウィジェット

    TextView textViewToday; // ルートの配下のウィジェット

    TextView textViewGone;  //  ルートの配下のウィジェット   非表示する(アダプターのクラスで)

    TextView schedules;  // ルートの配下のウィジェット android:inputType="textMultiLine"

    /**
     * コンストラクタ
     * @param itemView
     */
    public CalendarCellViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.dateText = view.findViewById(R.id.dateText);
        this.textViewToday = view.findViewById(R.id.textViewToday);
        this.textViewGone = view.findViewById(R.id.textViewGone);  // 非表示にする
        // 追加
        this.schedules = view.findViewById(R.id.schedules);
    }
}

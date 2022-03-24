package to.msn.wings.calendarrecyclerview;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeScheduleListHolder extends RecyclerView.ViewHolder{

    View view;  // ルート要素のビューグループ ConstraintLayoutのこと

    TextView date;  // ルート要素の配下のウィジェット
    TextView time;  // ルート要素の配下のウィジェット
    TextView scheduleTitle;  // ルート要素の配下のウィジェット
    TextView scheduleMemo;  // ルート要素の配下のウィジェット
    // データベースの主キーの情報をStringにして表示する
    TextView id;  // 追加
    // ボタンを1つ追加  import android.widget.Button;
    Button deleteBtn;


    public TimeScheduleListHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.date = view.findViewById(R.id.date);
        this.time = view.findViewById(R.id.time);
        this.scheduleTitle = view.findViewById(R.id.scheduleTitle);
        this.scheduleMemo = view.findViewById(R.id.scheduleMemo);
        this.id = view.findViewById(R.id.id); // 追加  主キーをString型にして、非表示にして、送りたいため

        this.deleteBtn = view.findViewById(R.id.deleteBtn);  // 追加
    }
}

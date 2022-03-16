package to.msn.wings.calendarrecyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeScheduleListHolder extends RecyclerView.ViewHolder{

    View view;  // ルート要素のビュー
    TextView time;
    TextView scheduleTitle;
    TextView scheduleMemo;

    public TimeScheduleListHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.time = view.findViewById(R.id.time);
        this.scheduleTitle = view.findViewById(R.id.scheduleTitle);
        this.scheduleMemo = view.findViewById(R.id.scheduleMemo);
    }
}

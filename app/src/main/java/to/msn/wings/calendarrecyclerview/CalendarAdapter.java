package to.msn.wings.calendarrecyclerview;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

   // 3つのメソッドをオーバーライドすべき

    /**
     * レイアウトファイルをインフレートする
     *  カードビューにリスナーをつけたい時にはこのonCreateViewHolderに書く
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


        // CardViewにリスナーをつけたい時には、この onCreateViewHolderメソッドに書く

        CardView cardView = v.findViewById(R.id.cardView);
//        cardView.setOnLongClickListener(new View.OnLongClickListener() {  // 長押し
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(v.getContext(), "長押ししますとトーストが出る", Toast.LENGTH_SHORT).show();
//                return false;  // 長押しの時には、その後に普通の onClickをするのかどうかを return false　　return true を書いて終わる
//            }
//        });

        /**
         * CardViewをクリックしたら、新しいアクティビティが起動する そのアクティビティには上にフラグメントが乗せてあるようにする
         */
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return new CalendarCellViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CalendarCellViewHolder holder, int position) {

        holder.dateText.setText(this.data.get(position).getDateText());
        holder.textViewToday.setText(this.data.get(position).getTextViewToday());  // 追加

        // holder.view は　CardViewです
        CardView cardView = holder.view.findViewById(R.id.cardView);
        TextView dateText = holder.view.findViewById(R.id.dateText);
        TextView textViewToday = holder.view.findViewById(R.id.textViewToday);

        for(int i = 0; i < data.size(); i++) {
            if (position == (i*7 )) {  // 日曜日
                cardView.setCardBackgroundColor(null);
                cardView.setCardBackgroundColor(Color.parseColor("#FF0800"));
                dateText.setTextColor(Color.parseColor("#FFFFFF"));
                textViewToday.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (position == (i*7 + 6 )) {  // 土曜日
                cardView.setCardBackgroundColor(null);
                cardView.setCardBackgroundColor(Color.parseColor("#0067c0"));
                dateText.setTextColor(Color.parseColor("#FFFFFF"));
                textViewToday.setTextColor(Color.parseColor("#FFFFFF"));
            }
           // 下線もつけられます
           //  dateText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        }

//        TextView titleText = holder.view.findViewById(R.id.titleText);
//        titleText.setText();


    }

    @Override
    public int getItemCount() {
       // return 0;
        return this.data.size();
    }






}

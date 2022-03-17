package to.msn.wings.calendarrecyclerview;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     *  トースト表示ならこのメソッド内に書いてもいいが、
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();  // MainActivity が取得できてる
                Intent intent = new Intent(context, TimeScheduleActivity.class);
                // 文字列になった 日付の情報を intentに putExtraする

                TextView textViewGone = view.findViewById(R.id.textViewGone);
                String scheduleDayText = textViewGone.getText().toString();  //  "2022/03/16"  などが取れる
//
                intent.putExtra("scheduleDayText" , scheduleDayText ); //  "2022/03/16"  日付の文字列情報を送るのにセットする

                // nullだとエラーになるので注意してください  落ちます nullにはならないはずです

                TextView textViewToday = view.findViewById(R.id.textViewToday);
                String todayString = textViewToday.getText().toString();
                intent.putExtra("todayString" , todayString );

                context.startActivity(intent);
            }
        } );

 // これはOK
    //    CardView cardView = v.findViewById(R.id.cardView);
//        cardView.setOnLongClickListener(new View.OnLongClickListener() {  // 長押し
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(v.getContext(), "長押ししますとトーストが出る", Toast.LENGTH_SHORT).show();
//                return false;  // 長押しの時には、その後に普通の onClickをするのかどうかを return false　　return true を書いて終わる
//            }
//        });


        // これでもOK
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Context context = view.getContext();  // タッチしたビューのコンテキスト(アクティビティクラス)を取得する
//                // インテントオブジェクトの生成
//                Intent intent = new Intent(context, TimeScheduleActivity.class ); // 今のアクティビティから、新しいアクティビティへ
//                    //  nullだと落ちます
////                TextView date = view.findViewById(R.id.date);
////                String dateText =  date.getText().toString();
////
////               intent.putExtra("date",dateText);
//                context.startActivity(intent);
//
//            }
//        });


        return new CalendarCellViewHolder(v);

    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarCellViewHolder holder, int position) {

        holder.dateText.setText(this.data.get(position).getDateText());
        holder.textViewToday.setText(this.data.get(position).getTextViewToday());  // 追加
        // 追加 これは非表示したい
        holder.textViewGone.setText(this.data.get(position).getTextViewGone());

        // holder.view は　CardViewです
        CardView cardView = holder.view.findViewById(R.id.cardView);
        TextView dateText = holder.view.findViewById(R.id.dateText);
        TextView textViewToday = holder.view.findViewById(R.id.textViewToday);
        TextView textViewGone = holder.view.findViewById(R.id.textViewGone);
        // textViewGone を非表示としたい  大切  View.VISIBLE・・・表示
        // View.INVISIBLE・・・非表示（非表示にしたスペースは詰めない）
        // View.GONE・・・非表示（非表示にしたスペースを詰める）
        //  textViewGone.setVisibility(View.GONE);  // これで表示しない なおかつ 非表示にしたスペースを詰める

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

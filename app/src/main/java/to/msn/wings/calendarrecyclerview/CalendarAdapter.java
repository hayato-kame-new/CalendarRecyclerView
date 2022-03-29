package to.msn.wings.calendarrecyclerview;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
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
    private ArrayList<CalendarCellItem> _data;
    // 大画面かどうかの判定フラグ インスタンスフィールド   宣言だけをしておき TimeScheduleFragmentのゲッターを使ってフィールドに代入する
    private boolean _isLayoutXLarge;  // 宣言だけ クラスのインスタンスフィールドの初期値は　falseになっています


    // コンストラクタ
    public CalendarAdapter(ArrayList<CalendarCellItem> data) {
        this._data = data;
    }

    /**
     * レイアウトファイルをインフレートする
     *  カードビューにリスナーをつけたい時にはこのonCreateViewHolderに書く
     *  トースト表示ならこのメソッド内に書いてもいいが、
     * @param parent RecycleView
     * @param viewType 0
     * @return
     */
    @NonNull
    @Override
    public CalendarCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // v　は　CardView
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_cell, parent, false);

        // MainActivity型ではだめですここだめエラー 今月意外だとエラーになる 他の月の場合は parent.getContext()　は MonthCalendarActivityを取得するので
        AppCompatActivity parentActivity = (AppCompatActivity) parent.getContext();  // MainActivity もしくは MonthCalendarActivity
        // 条件分岐してください
        MainActivity mainA = null;
        MonthCalendarActivity monthA = null;
        if (parentActivity.getClass().equals(MainActivity.class)) {
            mainA = (MainActivity)parentActivity;
        } else if (parentActivity.getClass().equals(MonthCalendarActivity.class)) {
             monthA = (MonthCalendarActivity)parentActivity;
        }


        // 大画面の場合 追加  androidx(テン)のパッケージの方ですので注意
        androidx.fragment.app.FragmentManager fmanager = null;
        final androidx.fragment.app.FragmentTransaction[] FINAL_F_TRANSACTION = {null};  // 匿名クラスの中で使うので finalの配列にする

        // 条件分岐
        if (mainA != null) {
            fmanager = ((FragmentActivity) mainA).getSupportFragmentManager();

            //  フラグメントマネージャーから、MainActivityに所属しているフラグメントを取得できます   findFragmentById メソッドを使う
            // MainActivytyには 上に CurrentMonthFragmentが乗ってるので
            CurrentMonthFragment currentMonthFragment = (CurrentMonthFragment)fmanager.findFragmentById(R.id.currentMonthFragment);

            // このクラスのインスタンスフィールドに値をセット このあと、onBindViewHolderでも使うため
            _isLayoutXLarge = currentMonthFragment.is_isLayoutXLarge();  // currentMonthFragmentインスタンスのゲッターメソッドを使う
        } else if (monthA != null) {
            fmanager = ((FragmentActivity) monthA).getSupportFragmentManager();
            // MonthCalendarActivityの 上には MonthCalendarFragment が乗っているので
            MonthCalendarFragment monthCalendarFragment = (MonthCalendarFragment)fmanager.findFragmentById(R.id.monthCalendarFragment);
            _isLayoutXLarge = monthCalendarFragment.is_isLayoutXLarge();
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();  // MainActivity が取得できてる
                Intent intent = new Intent(context, TimeScheduleActivity.class);
                // 文字列になった 日付の情報を intentに putExtraする

                TextView textViewGone = view.findViewById(R.id.textViewGone);
                String scheduleDayText = textViewGone.getText().toString();  //  "2022/03/16"  などが取れる

                intent.putExtra("scheduleDayText" , scheduleDayText ); //  "2022/03/16"  日付の文字列情報を送るのにセットする

                TextView textViewToday = view.findViewById(R.id.textViewToday);
                String todayString = textViewToday.getText().toString();
                intent.putExtra("todayString" , todayString );

                context.startActivity(intent);
            }
        } );
        return new CalendarCellViewHolder(v);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarCellViewHolder holder, int position) {

        holder.dateText.setText(this._data.get(position).getDateText());
        holder.textViewToday.setText(this._data.get(position).getTextViewToday());  // 追加
        // 追加 これは非表示したい　後で textViewGone.setVisibility(View.GONE);
        holder.textViewGone.setText(this._data.get(position).getTextViewGone());
        // 追加
        holder.schedules.setText(this._data.get(position).getSchedules());

        // holder.view は　ルート要素です　ここでは　ConstraintLayoutです
        // ConstraintLayout route = (ConstraintLayout) holder.view;
        CardView cardView = holder.view.findViewById(R.id.cardView);
        TextView dateText = holder.view.findViewById(R.id.dateText);
        TextView textViewToday = holder.view.findViewById(R.id.textViewToday);
        TextView textViewGone = holder.view.findViewById(R.id.textViewGone);
        // textViewGone を非表示としたい  大切  View.VISIBLE・・・表示
        // View.INVISIBLE・・・非表示（非表示にしたスペースは詰めない）
        // View.GONE・・・非表示（非表示にしたスペースを詰める）
         textViewGone.setVisibility(View.GONE);  // これで表示しない なおかつ 非表示にしたスペースを詰める

        // 追加
        TextView schedules = holder.view.findViewById(R.id.schedules);
    //    String s = schedules.getText().toString();

        // 属性をつける
        for(int i = 0; i < _data.size(); i++) {
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

        if (_isLayoutXLarge) {
            // もし画面サイズがタブレットサイズだったら ここで属性を変更できる
            holder.dateText.setTextSize(28);
            holder.schedules.setTextSize(18);
        } else {
            // 通常(スマホサイズ)画面ならば
            // ここで、属性を変更できる
        }
    }

    @Override
    public int getItemCount() {
        return this._data.size();
    }


    @Override
    public void onViewRecycled(@NonNull CalendarCellViewHolder holder) {
        super.onViewRecycled(holder);
        notifyDataSetChanged();
    }
}

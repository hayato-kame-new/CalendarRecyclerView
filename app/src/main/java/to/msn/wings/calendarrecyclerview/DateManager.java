package to.msn.wings.calendarrecyclerview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateManager {

    //フィールド
    Calendar mCalendar;

    /**
     * 現在を取得するコンストラクタ
     */
    public DateManager(){
        mCalendar = Calendar.getInstance();
    }


    /**
     * 当月よ要素を取得する 引数
     * @return
     */
    public List<Date> getDays() {

        // 現在
        Date startDate = mCalendar.getTime();

        // グリッドに表示するマスの合計   getWeeks() はインスタンスメソッドです 下で定義してる
        int count = getWeeks() * 7;

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DATE, 1);  // 今月の1日をセットする
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;  // dayOfWeekは曜日 日曜日は  0

         // カレンダーに載せる日曜日の日付を取得してる
        mCalendar.add(Calendar.DATE, -dayOfWeek);  // 日曜日にする 0にするため -dayOnWeek 火曜日だったら -2すると日曜になる

        List<Date> days = new ArrayList<>();

        for (int i = 0; i < count; i ++){
            days.add(mCalendar.getTime());  // mCalendarは、今、カレンダーに載せる最初の日曜日になってる
            // mCalendarに1日をプラスしていき、リストに加える
            mCalendar.add(Calendar.DATE, 1);
        }

        // このループで、記載する日付Date型の入った リストができてる

        //状態を復元 本日の日付に戻してる
        mCalendar.setTime(startDate);

        return days;

    }

//    public List<Date> getDays(int month) {
//
//
//    }


    //当月かどうか確認
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        String currentMonth = format.format(mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }



    /**
     * 週の数を取得する
     * @return
     */
    public int getWeeks() {
        return mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }


    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //翌月へ
//    public void nextMonth(){
//        mCalendar.add(Calendar.MONTH, 1);
//    }

    //前月へ
//    public void prevMonth(){
//        mCalendar.add(Calendar.MONTH, -1);
//    }

}

package to.msn.wings.calendarrecyclerview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateManager {

    //フィールド
    Calendar _mCalendar;

    public DateManager(){
        // コンストラクタでは生成しないように変更した
       // mCalendar = Calendar.getInstance();
    }


    /**
     * 引数なし getDays()メソッドです 　もう一つ、オーバーロード(多重定義)した 引数ありの同名メソッドがあります
     * 引数なしだと今月要素を取得する MainActivityで使う
     * @return List<Date> 現在の月のカレンダーに表示するためのリスト
     */
    public List<Date> getDays() {

        _mCalendar = Calendar.getInstance();
        // 現在
        Date startDate = _mCalendar.getTime();

        // グリッドに表示するマスの合計
        int count = _mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) * 7;
        // int count = getWeeks() * 7;

        //今月のカレンダーに表示される前月分の日数を計算
        _mCalendar.set(Calendar.DATE, 1);  // 今月の1日をセットする
        // 1日の曜日を取得して、それから -1すれば 前の月の最後の日の曜日が取得できる
        int dayOfWeek = _mCalendar.get(Calendar.DAY_OF_WEEK) - 1;  // dayOfWeekは曜日 日曜日は  0

         // カレンダーに載せる日曜日の日付を取得してる
        _mCalendar.add(Calendar.DATE, -dayOfWeek);  // 日曜日にする 0にするため -dayOnWeek 火曜日だったら -2すると日曜になる

        List<Date> days = new ArrayList<>();

        for (int i = 0; i < count; i ++){
            days.add(_mCalendar.getTime());  // mCalendarは、今、カレンダーに載せる最初の日曜日になってる
            // mCalendarに1日をプラスしていき、リストに加える
            _mCalendar.add(Calendar.DATE, 1);
        }
        // このループで、記載する日付Date型の入った リストができてる
        //状態を復元 本日の日付に戻してる
        _mCalendar.setTime(startDate);
        return days;
    }

    /**
     * 引数ありのメソッド
     * オーバーロード（多重定義)したメソッドです
     * 引数で渡された指定の日付の月の 要素を取得します
     * 引数には、Date型オブジェクトが渡されます。 指定の月の最初の土曜日の日付が入ってます
     * @param date
     * @return List<Date> 指定した月のカレンダーを表示するためのリスト
     */
    public List<Date> getDays(Date date){
        // 指定の日付は引数から取得できる

        _mCalendar = Calendar.getInstance();
        _mCalendar.setTime(date);  // 指定の日付にする
        // 指定の日付の月のグリッドに表示するマスの合計
        int count = _mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) * 7;
        // さらに、1日の日付にする 計算するために
        // 指定をした月のカレンダーに表示される前月分の日数を計算するため
        _mCalendar.set(Calendar.DATE, 1);  // 1日をセットする

        // 1日の曜日を取得して、それから -1すれば 前の月の最後の日の曜日が取得できる
        int dayOfWeek = _mCalendar.get(Calendar.DAY_OF_WEEK) - 1;  // dayOfWeekは曜日 日曜日は  0

        // カレンダーに載せる日曜日の日付を取得してる
        _mCalendar.add(Calendar.DATE, -dayOfWeek);  // 日曜日にする 0にするため -dayOnWeek 火曜日だったら -2すると日曜になる

        List<Date> days = new ArrayList<>();

        for (int i = 0; i < count; i ++){
            days.add(_mCalendar.getTime());  // mCalendarは、今、カレンダーに載せる最初の日曜日になってる
            // mCalendarに1日をプラスしていき、リストに加える
            _mCalendar.add(Calendar.DATE, 1);
        }
        // このループで、記載する日付Date型の入った リストができてる
        //状態を復元 引数で 指定された日付に戻してる
        _mCalendar.setTime(date);  // ちょっと確認する
        return days;
    }
    
    //当月かどうか確認
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        _mCalendar = Calendar.getInstance();  // 後で修正するかも
        String currentMonth = format.format(_mCalendar.getTime());
        if (currentMonth.equals(format.format(date))){
            return true;
        }else {
            return false;
        }
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}

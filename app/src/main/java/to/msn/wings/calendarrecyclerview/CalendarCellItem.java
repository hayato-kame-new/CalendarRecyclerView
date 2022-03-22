package to.msn.wings.calendarrecyclerview;

public class CalendarCellItem {

    private long id = 0;  // 識別するためのID ランダムにつけてる

    private String dateText = null;

    // 追加  textViewToday
    private String textViewToday = null;
    // 追加 非表示のTextViewにする
    private String textViewGone = null;


     // これできるのかな？？ 追加したけど
   // 追加 テーブルに表示する各スケジュールの日付
    private String schedules = null;
//    private String scheduledate = null;
//    private String startime = null;
//    private String endtime = null;
//    private String scheduletitle = null;
//    private String schedulememo = null;



    // ゲッター セッター
    public long getId() {
        return id;
    }

    public String getDateText() {
        return dateText;
    }

    public String getTextViewToday() {
        return textViewToday;
    }

    public String getTextViewGone() {
        return textViewGone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public void setTextViewToday(String textViewToday) {
        this.textViewToday = textViewToday;
    }

    public void setTextViewGone(String textViewGone) {
        this.textViewGone = textViewGone;
    }

// 追加

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }
// 追加したけどいいのかわからない

//    public String getScheduledate() {
//        return scheduledate;
//    }
//
//    public String getStartime() {
//        return startime;
//    }
//
//    public String getEndtime() {
//        return endtime;
//    }
//
//    public String getScheduletitle() {
//        return scheduletitle;
//    }
//
//    public String getSchedulememo() {
//        return schedulememo;
//    }
//
//    public void setScheduledate(String scheduledate) {
//        this.scheduledate = scheduledate;
//    }
//
//    public void setStartime(String startime) {
//        this.startime = startime;
//    }
//
//    public void setEndtime(String endtime) {
//        this.endtime = endtime;
//    }
//
//    public void setScheduletitle(String scheduletitle) {
//        this.scheduletitle = scheduletitle;
//    }
//
//    public void setSchedulememo(String schedulememo) {
//        this.schedulememo = schedulememo;
//    }
}

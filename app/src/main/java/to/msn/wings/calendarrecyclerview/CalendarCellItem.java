package to.msn.wings.calendarrecyclerview;

public class CalendarCellItem {

    private long id = 0;  // 識別するためのID

    private String dateText = null;

    private String textViewToday = null;
    //  非表示のTextViewにする
    private String textViewGone = null;

   // 表示する各スケジュールを文字列で連結したもの
    private String schedules = null;

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

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }

}

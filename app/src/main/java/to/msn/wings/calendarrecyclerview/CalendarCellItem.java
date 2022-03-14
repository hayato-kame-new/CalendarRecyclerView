package to.msn.wings.calendarrecyclerview;

public class CalendarCellItem {

    private long id = 0;  // 識別するためのID ランダムにつけてる

    private String dateText = null;

    // 追加  textViewToday
    private String textViewToday = null;


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

    public void setId(long id) {
        this.id = id;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public void setTextViewToday(String textViewToday) {
        this.textViewToday = textViewToday;
    }
}

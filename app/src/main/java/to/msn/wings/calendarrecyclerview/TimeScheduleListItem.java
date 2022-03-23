package to.msn.wings.calendarrecyclerview;

public class TimeScheduleListItem {

    private long id = 0;  // データベースからの _id カラムの値を
     private String date = null;
    private String startTime = null;
    private String endTime = null;
    private String scheduleTitle = null;
    private String scheduleMemo = null;

    // アクセッサ ゲッター
    public long getId() {
        return id;
    }

   public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getScheduleTitle() {
        return scheduleTitle;
    }

    public String getScheduleMemo() {
        return scheduleMemo;
    }

    // セッター
    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
    }

    public void setScheduleMemo(String scheduleMemo) {
        this.scheduleMemo = scheduleMemo;
    }
}

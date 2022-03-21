package to.msn.wings.calendarrecyclerview;

import android.util.Log;

public class Schedule {
    // フィールド
    private int _id;
    private String scheduledate;
    private String starttime;
    private String endtime;
    private String scheduletitle;
    private String schedulememo;

    /**
     * コンストラクタ
     * @param _id
     * @param scheduledate
     * @param starttime
     * @param endtime
     * @param scheduletitle
     * @param schedulememo
     */
    public Schedule(int _id, String scheduledate, String starttime, String endtime, String scheduletitle, String schedulememo) {
        this._id = _id;
        this.scheduledate = scheduledate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.scheduletitle = scheduletitle;
        this.schedulememo = schedulememo;
    }

    public int get_id() {
        return _id;
    }

    public String getScheduledate() {
        return scheduledate;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getScheduletitle() {
        return scheduletitle;
    }

    public String getSchedulememo() {
        return schedulememo;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setScheduledate(String scheduledate) {
        this.scheduledate = scheduledate;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setScheduletitle(String scheduletitle) {
        this.scheduletitle = scheduletitle;
    }

    public void setSchedulememo(String schedulememo) {
        this.schedulememo = schedulememo;
    }
}

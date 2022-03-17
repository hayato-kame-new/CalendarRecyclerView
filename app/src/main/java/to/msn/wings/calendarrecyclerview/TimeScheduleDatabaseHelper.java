package to.msn.wings.calendarrecyclerview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TimeScheduleDatabaseHelper extends SQLiteOpenHelper {

    // 定数フィールド
    static final private String DBNAME = "timeschedule.sqlite";
    static final private int VERSION = 1;


    /**
     * コンストラクタ 第三引数は　null
     * @param context
     */
    public TimeScheduleDatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // SQLiteでは、列が INTEGER型 かつ PRIMARY KEY（主キー）の場合に、AutoIncrement を指定をすることができます。しかし、
        // SQLiteで AutoIncrement を使用する主な用途は、
        // ROWIDの選択アルゴリズムを増加のみにしたい 場合で、この制約をつける必要がない場合は、AutoIncrement を使用する必要はないと思います
        // AutoIncrement を使用すると、自動採番で削除されたROWIDが再利用されないため、一度頭打ちになるとそれ以上データが登録出来ないということも起こり得ます。
        //
        //なので、AutoIncrement を使用する場合は、少々注意する必要があります。AutoIncrementはつけないことをおすすめする
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("CREATE TABLE timeschedule (" + "_id INTEGER PRIMARY KEY , scheduledate DATE NOT NULL," +
                    " starttime DATETIME NOT NULL, endtime DATETIME , scheduletitle TEXT NOT NULL, schedulememo TEXT)");
        }

    }

    // データベースをバージョンアップした時、テーブルを再作成
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS timeschedule");
            onCreate(sqLiteDatabase);
        }


    }
}

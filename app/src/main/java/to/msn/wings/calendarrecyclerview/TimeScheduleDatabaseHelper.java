package to.msn.wings.calendarrecyclerview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TimeScheduleDatabaseHelper extends SQLiteOpenHelper {

    // 定数フィールド static クラスフィールドです クラスフィールドだから　変数名に _ アンダーバーはつけない
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

        // SQLiteでは、列が INTEGER型 かつ PRIMARY KEY（主キー）の場合に、AutoIncrement を指定をすることができますが。つけない方がいいらしい
        // SQLite3では、「CREATE TABLE」の際に「AUTO INCREMENT」を指定する必要はありません。つけない方がいいらしい
        //もし主キーを連番のIDにしたい場合、INTEGERで「PRIMARY KEY」を指定するようにします。
       //  SQLite のテーブルのスキーマでは文字列の最大の長さを指定することはできない
        //  タイトルは android:maxLength="30"   メモは android:maxLength="80"  など xmlで入力文字数を制限することができる
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("CREATE TABLE timeschedule (_id INTEGER PRIMARY KEY  , scheduledate TEXT NOT NULL," +
                    " starttime TEXT NOT NULL, endtime TEXT NOT NULL, scheduletitle TEXT NOT NULL, schedulememo TEXT)");
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

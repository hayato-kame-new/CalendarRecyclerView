package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * メインアクティビティの上にCurrentMonthFragmentが乗っています そこで、データベースにも接続してデータ取得します
 */
public class MainActivity extends AppCompatActivity {


    // public で staticな　フィールドにすれば、他のフラグメントなどでも使い回しできるらしい
    // private TimeScheduleDatabaseHelper helper;
    // １つしかない　　他のアクティビティなどで呼び出せる クラス名::フィールド名 で呼び出せる
    public static TimeScheduleDatabaseHelper helper;  // 変更してみた staticな　クラスフィールドに変更 静的フィールド　１つしかない　　他のアクティビティなどで呼び出せる

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ちなみに　バックキーで戻ることのできる、MainActivityは　メインのスレッドでもあるので サブアクティビティのような終了finish()はさせません
         helper = new TimeScheduleDatabaseHelper(MainActivity.this);  // onDestroy()で helperを解放すること

        // MainActivityの上に乗せた CurrentMonthFragmentで、既存のデータを表示させるので、SELECT文で取得する
        // データベースを取得する try-catch-resources構文なのでfinallyを書かなくても必ず db を　close()処理をしてくれます！！
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            Toast.makeText(this, "接続しました", Toast.LENGTH_SHORT).show();
            // ここにデータベースの処理を書く SELECT文で取得する CurrentMonthFragmentで表示

        }

    }

    @Override
    protected void onDestroy() {

        // ここで解放じゃなくて、フラグメント移動した時に解放したほうがいいのかも
        helper.close();  // アクティビティの消滅の前に DBヘルパーオブジェクトの解放をすること
        super.onDestroy();
    }


}
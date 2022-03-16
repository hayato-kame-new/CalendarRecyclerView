package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TimeScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       //  return inflater.inflate(R.layout.fragment_time_schedule, container, false);
        View view = inflater.inflate(R.layout.fragment_time_schedule, container, false);


        // リストに表示するアイテムを準備する 固定なら、ここに配列を書きますが、データーベースから取得するなら
        // まずは、SQLiteOpenHelper抽象クラスを extendsした ヘルパークラスを定義してください
        // そして、アクティビティクラスもしくはその上に乗せたフラグメントクラスで newして インスタンスを生成します
        // ヘルパーのインスタンスを準備する
        // まず、所属するアクティビティを取得する
//        Activity parentActivity = getActivity();
//        TimeScheduleDatabaseHelper helper = new TimeScheduleDatabaseHelper(parentActivity);
//
//        // データベースを取得する
//       try (SQLiteDatabase db = helper.getWritableDatabase()) {
//           Toast.makeText(parentActivity, "接続しました", Toast.LENGTH_SHORT).show();
//           // ここにデータベースの処理を書く
//
//       }


        // 最後にreturn viewをすること
        return view;

    }
}
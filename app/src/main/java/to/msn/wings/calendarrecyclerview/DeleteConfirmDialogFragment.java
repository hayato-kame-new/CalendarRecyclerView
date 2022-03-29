package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


public class DeleteConfirmDialogFragment extends DialogFragment {

    private TimeScheduleDatabaseHelper _helper;
    String _strId = "";
    String _scheduleTitle = "";
    String _strDate = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // フォームで削除ボタンを押した時に、Bundleでデータを引き渡してるので、取得する
        Bundle args = requireArguments();
        // String型のデータを渡しているので
        _strId = args.getString("strId");  // onCreateDialogメソッドで取得しておく
        _scheduleTitle = args.getString("scheduleTitle");
        _strDate = args.getString("strDate");

        // 既存のダイアログに TextViewを 付け足すには
        Activity parentActivity = getActivity();
       // TextViewを準備する
        TextView textViewStrId = new TextView(parentActivity);  // 既存のダイアログに 追加するTextView インスタンスを生成する
        TextView textViewScheduleTitle = new TextView(parentActivity);

        textViewStrId.setText(_strId);  // ここに動的に _idカラムの値をString型にしたものをセットして、非表示にする
        textViewScheduleTitle.setText("タイトル: " + _scheduleTitle );  // こっちが上に重なって乗ってid見えない
        textViewScheduleTitle.setGravity(Gravity.CENTER);

        // textViewStrId.setVisibility(View.VISIBLE);
        textViewStrId.setVisibility(View.GONE);  // textViewScheduleTitleが 上に乗ってるから見えないけど
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogTitle);
        builder.setMessage(R.string.dialogMsg);
        builder.setPositiveButton(R.string.dialogBtnOk, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogBtnNg, new DialogButtonClickListener());
        // 追加
        builder.setView(textViewStrId);
        builder.setView(textViewScheduleTitle);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    // リスナークラスを クラスメンバとして定義する ダイアログのボタンをクリックした時のリスナー
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            switch(i) {
                case DialogInterface.BUTTON_POSITIVE:
                    // ここで、削除の処理を実行するので データベースへ接続をします。

                    Activity parentActivity = getActivity();
                    _helper = new TimeScheduleDatabaseHelper(parentActivity);  // _helper は解放すること
                    //  データベースを取得する try-catch-resources構文を使うので  SQLiteDatabase db は　finallyを書かなくても必ず close()処理をしてくれます
                    try (SQLiteDatabase db = _helper.getWritableDatabase()) {  // dbはきちんとクローズ自動でしてくれます

                        String sqlDelete = "DELETE FROM timeschedule WHERE _id = ?";

                        SQLiteStatement stmt = db.compileStatement(sqlDelete);
                        stmt.bindLong(1, Long.parseLong(_strId));
                        stmt.executeUpdateDelete();

                    }
                    _helper.close();  // クローズしておくこと

                    Toast.makeText(getActivity(),"削除しました", Toast.LENGTH_LONG).show();
                    // 削除したら、このフラグメントが所属するアクティビティを終了させる
                    // strDate と同じ　年月の月カレンダーへいく
                    // スケジュールを挿入した年月が、現在の年月なら MainActivityへ　それ以外の月ならMonthCalendarActivityへ遷移する "2022-03-19"
                    Date date = null;
                    try {
                         date = new SimpleDateFormat("yyyy/MM/dd").parse(_strDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int year = Integer.parseInt(_strDate.substring(0, 4));
                    int month = Integer.parseInt(_strDate.substring(5, 7));
                    // 現在を取得して
                    LocalDate localdateToday = LocalDate.now();
                    Intent intent = null;
                    if (year == localdateToday.getYear() && month == localdateToday.getMonthValue()) {
                        // 現在と同じなので MainActivityへ遷移する
                        intent = new Intent(parentActivity, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // 指定の日付のカレンダーを表示するため MonthCalendarActivityへ遷移する
                        intent = new Intent(parentActivity, MonthCalendarActivity.class);
                        intent.putExtra("specifyDate", date);  //  Date型情報を渡します
                        startActivity(intent);
                    }

                    // 最後に 自分自身が所属するアクティビティを終了させます
                    parentActivity.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // 元の日付の タイムスケジュール一覧画面へ戻ります ダイアログ消えるだけ
                    // 自分自身の所属するアクティビティの終了はしない。フラグメントからフラグメントを生成しただけなので
                    // 何にも処理はしない
                break;
            }
        }
    }
}
